# System Design Daily Notes — Praveen

---

## Day 1: API Gateway Idempotency & Distributed Deduplication in Microservices (GCP Pattern)

### 1. Senior Executive Summary
In high-throughput distributed microservices (e.g., payment processing, order creation, AI vector embedding requests), network retries and duplicate HTTP requests are inevitable. **Idempotency** guarantees that executing an operation multiple times produces the exact same outcome as running it once.

### 2. Core Architecture Pattern: Idempotency Key Pattern
1. **Client Request:** Client sends a unique UUID `Idempotency-Key` header with every mutating request (`POST`/`PATCH`).
2. **Gateway / Middleware Check:** An API Gateway or Spring Boot Filter intercepts the request before reaching the domain business logic.
3. **Atomic Lock & Cache (Redis / MemoryStore on GCP):**
   - Check Redis key: `idempotency:{key}`.
   - If **Key Exists & Status = COMPLETED**: Return the cached HTTP response instantly (HTTP 200/201).
   - If **Key Exists & Status = IN_PROGRESS**: Return HTTP 409 Conflict ("Request currently processing").
   - If **Key Does Not Exist**: Set key atomically with status `IN_PROGRESS` and a TTL (e.g., 24 hours).
4. **Execution & State Update:** Execute backend business logic inside a database transaction.
5. **Response Cache:** Save the final HTTP response payload in Redis, updating key status to `COMPLETED`.

```
[ Client ] ---> ( POST /order + Idempotency-Key: uuid )
                   |
                   v
         [ GCP Cloud API Gateway / Spring Filter ]
                   |
        +----------+----------+
        | Check Key in Redis  |
        +----------+----------+
          /                 \
  (Key Found: COMPLETED)   (Key Not Found)
        |                       |
Return Cached Response     Acquire Lock (IN_PROGRESS)
                                |
                   Execute Microservice Logic
                                |
                   Save Result in Redis & Return
```

### 3. Senior Microservices & Concurrency Guardrails
- **Database Unique Constraints:** In case Redis is unavailable, maintain a fallback database constraint: `UNIQUE (idempotency_key)`.
- **GCP Native Integration:** Combine **GCP Cloud Endpoints / Apigee** with **GCP Memorystore (Redis)** for low-latency (< 2ms) token validation.
- **RAG / AI Extension:** In Vector Search / RAG indexing pipelines, use document content hashes (`SHA-256`) as idempotency keys to avoid re-generating expensive LLM embeddings for identical enterprise documents.

---

## Day 2: Distributed Rate Limiting (Token Bucket / Sliding Window) on GCP

### 1. Senior Executive Summary
Rate limiting protects distributed systems from traffic spikes, brute-force attacks, and abusive API usage. For a microservices architecture, a centralized, distributed rate limiter using Redis ensures consistency across multiple gateway nodes while keeping latency extremely low.

### 2. Core Architecture Pattern: Distributed Rate Limiting
1. **Client Request:** Client sends a request to the API Gateway with an identifier (e.g., API Key, User ID, or IP Address).
2. **Gateway Check:** The gateway (e.g., GCP API Gateway / Spring Cloud Gateway) acts as the enforcement point.
3. **Redis Evaluation (Token Bucket / Sliding Window):**
   - **Token Bucket:** Fast and memory-efficient. A Lua script running on Redis checks current token count and last refill timestamp.
   - **Sliding Window:** Highly accurate but requires more memory (using Redis Sorted Sets / `ZSET` to track timestamps of recent requests).
4. **Action:**
   - If allowed: Route request to the backend microservice.
   - If blocked: Return HTTP 429 Too Many Requests instantly.

```
[ Client ] ---> ( HTTP Request + API Key )
                   |
                   v
         [ GCP Cloud API Gateway / Spring Cloud Gateway ]
                   |
        +----------+----------+
        | Evaluate in Redis   | (using Lua Script for atomicity)
        +----------+----------+
          /                 \
     (Allowed)          (Blocked)
         |                   |
 Route to Backend      Return HTTP 429 (Too Many Requests)
```

### 3. Senior Microservices & Concurrency Guardrails
- **Lua Scripts for Atomicity:** Executing rate limit logic (read + write) via a Lua script in Redis ensures atomicity and prevents race conditions in a highly concurrent environment.
- **GCP Native Integration:** Use GCP Memorystore (Redis) for single-digit millisecond latency.
- **RAG / AI Extension:** LLM API calls are expensive. Implement multi-tier rate limiting based on token usage (e.g., 1000 tokens per minute) instead of just request counts, ensuring fair usage of AI endpoints.

---

## Day 3: Advanced Java & Spring Boot Core Internals

### Category 1: Concurrency & Multithreading
1. **Virtual Threads (Java 21) vs OS Threads:** Virtual threads are lightweight, JVM-managed threads. Millions can run concurrently because they are detached from heavy OS threads during blocking I/O, reducing memory footprint and context-switching overhead. Use them for high-throughput I/O-bound tasks.
2. **ConcurrentHashMap (Java 8+):** Pre-Java 8 used Segment-level locking. Java 8+ abandoned segments for Node-level locking using Compare-And-Swap (CAS) and `synchronized` blocks only on the head node of a bucket, vastly improving concurrent write performance.
3. **CountDownLatch vs CyclicBarrier:** A `CountDownLatch` allows one or more threads to wait until a set of operations completes (one-time use). A `CyclicBarrier` allows a set of threads to all wait for each other to reach a common barrier point before proceeding (reusable).
4. **ThreadLocal Risks:** `ThreadLocal` variables store data per thread. In web servers using thread pools (like Tomcat), threads are reused. If a `ThreadLocal` is not explicitly cleared (`.remove()`), data bleeds into subsequent requests, causing severe memory leaks and data contamination.

### Category 2: JVM Internals & Memory Management
5. **G1GC vs ZGC:** G1GC divides the heap into regions and targets high-garbage areas first to meet pause-time goals. ZGC (Z Garbage Collector) is a scalable, low-latency collector that performs all expensive work concurrently, keeping pause times under 1ms regardless of heap size (up to terabytes).
6. **Memory Leaks in Java:** Since GC only removes unreferenced objects, leaks occur when objects are unintentionally held in memory. Common causes: Unclosed `ResultSet` or Streams, adding elements to a `static` Map without removing them, and unmanaged `ThreadLocal` variables.
7. **OutOfMemoryError (Non-Heap):** Besides heap exhaustion, OOM can occur due to **Metaspace OOM** (loading too many classes dynamically, e.g., via proxies) or **Unable to create new native thread** (OS thread limits exhausted).

### Category 3: Modern Java
8. **map() vs flatMap():** `map()` transforms each element into another object (1-to-1). `flatMap()` transforms each element into a stream of objects and flattens them into a single stream (1-to-N). **Parallel Stream Pitfall:** Default parallel streams use the common ForkJoinPool; a blocking I/O task inside a parallel stream will freeze the entire JVM pool.
9. **String Immutability:** Strings are immutable for security (database passwords, network URLs), thread-safety, and performance (caching hashcodes). It allows the JVM to use the **String Constant Pool** to save memory by reusing identical string literals.
10. **Records & Sealed Classes (Java 14+ / 21):** `record` is a concise syntax for immutable data carriers (auto-generates getters, equals, hashcode, toString). **Sealed classes** restrict which other classes or interfaces may extend or implement them, enabling exhaustive `switch` pattern matching.

### Category 4: Spring Boot Core
11. **@Transactional & AOP Proxies:** Spring creates an AOP proxy around beans. When an external caller invokes a `@Transactional` method, the proxy starts a database transaction. **Catch:** If you call a `@Transactional` method from *another method within the same class*, it bypasses the proxy and the transaction is **not** created (Self-Invocation Problem).
12. **Bean Scopes (Prototype in Singleton):** A Singleton is created once; a Prototype is created every time it's requested. If you autowire a Prototype into a Singleton, the Prototype is injected only once. **Fix:** Use `@Lookup` method injection or `ObjectProvider<T>` to fetch a new Prototype instance dynamically.

---

## Day 4: Distributed Tracing & Observability in Microservices

### 1. Senior Executive Summary
In a microservices ecosystem, a single client request might traverse dozens of independent services (API Gateway -> Auth -> Order Service -> Payment Service -> Database). When latency spikes or a failure occurs, isolated logs are insufficient. **Distributed Tracing** (via OpenTelemetry / Spring Cloud Sleuth) provides a visual, end-to-end trace of a request's lifecycle across process boundaries.

### 2. Core Architecture Pattern: Distributed Tracing
1. **Trace ID & Span ID:** 
   - A **Trace ID** is a globally unique identifier for a single request across the entire system.
   - A **Span ID** represents a single logical operation within that trace (e.g., a database query, an HTTP call to another service).
2. **Context Propagation:**
   - The API Gateway generates the initial Trace ID and injects it into HTTP Headers (e.g., `traceparent` or `X-B3-TraceId`).
   - Every downstream Spring Boot microservice extracts this header, logs its local Spans, and injects the Trace ID into subsequent outgoing HTTP/gRPC requests.
3. **Telemetry Export:**
   - Instead of writing traces to local disk, microservices asynchronously export span data via the OpenTelemetry Protocol (OTLP) to a centralized collector.
4. **Visualization & APM:**
   - The collector aggregates the data and sends it to an APM tool (GCP Cloud Trace, Jaeger, Datadog) where engineers can view waterfall charts of request latency.

### 3. Senior Microservices & Concurrency Guardrails
- **MDC (Mapped Diagnostic Context) in Java:** Spring automatically injects Trace IDs into SLF4J/Logback MDC. This ensures every log line emitted by a thread includes the `[TraceID, SpanID]`. 
- **Thread Context Loss:** When handing off work to asynchronous threads (e.g., `CompletableFuture` or `@Async`), the MDC context is naturally lost because it uses `ThreadLocal`. **Fix:** Use context-propagating wrappers like `ContextSnapshot` (Micrometer) or manual MDC copying to ensure async background logs still carry the Trace ID.
- **Sampling Rate:** Exporting 100% of traces is too expensive in high-throughput systems. Implement **Tail-based Sampling** (capture only traces that resulted in errors or latency > 1s) or probabilistic sampling (e.g., 5% of all traffic).
