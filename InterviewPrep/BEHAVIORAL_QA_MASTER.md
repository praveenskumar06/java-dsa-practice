# Behavioral Q&A Master — Praveen

---

## Day 1 STAR Story: Resolving a High-Severity Production Outage in a Distributed Microservices Ecosystem

### Question
*"Tell me about a time you faced a critical production incident or technical crisis. How did you handle it and what was the outcome?"*

---

### Situation (S)
During a peak traffic deployment at my previous enterprise position, our microservices order service experienced severe latency degradation (response times spiked from 150ms to >12s), leading to cascading connection pool exhaustion and checkout failures affecting over 20% of active users.

### Task (T)
As the Senior Backend Java Engineer on duty, I needed to immediately stabilize the production environment, identify the root cause without taking down dependent services, coordinate cross-functional communication, and implement a long-term architectural fix.

### Action (A)
1. **Immediate Stabilization (Triage):**
   - Implemented a temporary rate-limiter and gracefully shed non-critical traffic.
   - Isolated the metrics via GCP Cloud Monitoring and APM logs.
2. **Root Cause Analysis (RCA):**
   - Discovered that an un-indexed database query introduced in a recent microservice update was causing table locks under high concurrent read/write transactions.
   - Connection pool threads were blocking indefinitely waiting for DB connections.
3. **Execution & Mitigation:**
   - Rolled back the faulty microservice deployment safely using blue-green deployment routing.
   - Added missing composite database indexes and configured strict thread connection pool timeouts (`HikariCP` `connectionTimeout=3000ms`, `maximumPoolSize=20`).
   - Introduced a Circuit Breaker pattern (`Resilience4j`) around downstream database calls.
4. **Post-Mortem & Preventative Actions:**
   - Authored a transparent Post-Mortem incident report.
   - Added automated SQL query execution plan verification into our CI/CD pipeline to flag un-indexed queries during pull requests.

### Result (R)
- **Recovery:** Restored 100% service health within 22 minutes of the initial alert.
- **Performance:** Reduced average P99 latency by 35% compared to pre-incident baselines.
- **Process Improvement:** Prevented similar database lock regressions, achieving 99.99% uptime over the next 4 quarters.

---

### Key Interview Takeaways for Canada Market
- **Tone:** Collaborative, calm under pressure, data-driven, and focused on blameless post-mortems.
- **Key Buzzwords:** Triage, Latency P99, HikariCP, Circuit Breaker, Blue-Green Rollback, RCA.

---

## Day 2 STAR Story: Pushing Back on Product Deadlines for Technical Debt / System Stability

### Question
*"Describe a time when you had to disagree with a senior stakeholder or Product Manager regarding a technical decision or deadline. How did you handle it?"*

### Situation (S)
At my previous company, our Product Manager wanted to push a major AI-integrated feature to production a week ahead of schedule to meet a marketing campaign deadline. However, the existing monolithic database was already experiencing deadlocks, and adding the new load without refactoring the data access layer would risk a critical production outage.

### Task (T)
I needed to persuade the PM to delay the feature launch by two sprints, allowing the engineering team time to decouple the database calls and implement a caching layer, while ensuring the business understood the risks of moving forward prematurely.

### Action (A)
1. **Gather Data & Risk Assessment:** I profiled the database performance in staging under simulated load using JMeter, which showed an 80% failure rate when the new feature was enabled alongside normal traffic.
2. **Propose Solutions (Not Just Problems):** I created a detailed technical proposal outlining three options:
   - *Option A:* Launch now (High risk of total outage).
   - *Option B:* Launch a limited beta using feature flags (Medium risk).
   - *Option C:* Delay by two sprints to implement Redis caching and resolve technical debt (Low risk, sustainable).
3. **Stakeholder Alignment:** I presented the JMeter metrics to the PM and engineering directors. I explained technical debt in business terms, framing it as "downtime cost" vs "time-to-market."

### Result (R)
- The PM agreed to Option C (two-sprint delay).
- We successfully implemented the Redis cache and refactored the database layer.
- The feature launched flawlessly on the new date, supporting 3x the anticipated traffic without a single database timeout, and the PM later praised the proactive risk management.

### Key Interview Takeaways for Canada Market
- **Tone:** Diplomatic, data-driven, business-oriented, and solution-focused.
- **Key Buzzwords:** JMeter load testing, Feature Flags, Redis Caching, Technical Debt, Risk Mitigation, Business Continuity.

---

## Day 4 STAR Story: Mentoring & Upskilling a Junior Developer / Team Collaboration

### Question
*"Tell me about a time you mentored a junior team member or helped elevate the technical skills of your team."*

### Situation (S)
During a team expansion at my last role, a new junior developer joined our squad. While they were enthusiastic, they were struggling to grasp our microservices architecture and modern Java concurrency paradigms, leading to delayed PRs and some code quality issues during code reviews.

### Task (T)
As a Senior Developer, my goal was to accelerate their onboarding, improve their confidence, and elevate their code quality without micromanaging or making them feel inadequate.

### Action (A)
1. **Pair Programming Sessions:** I set up weekly 1-hour pair programming sessions focused on our core domain logic. I drove the architecture while letting them drive the keyboard, discussing "why" we use certain patterns (like Circuit Breakers or CompletableFutures) rather than just "how".
2. **Constructive Code Reviews:** Instead of just rejecting PRs, I left detailed comments linking to internal documentation or standard Java best practices. If a PR had major architectural flaws, I suggested a quick 15-minute huddle to draw it out on a virtual whiteboard.
3. **Targeted Knowledge Sharing:** I noticed they were struggling with Spring Boot's dependency injection and transactional boundaries. I created a concise wiki page detailing these specific gotchas and presented a 20-minute tech talk to the whole team, so the junior dev wouldn't feel singled out.
4. **Gradual Autonomy:** I initially assigned them well-scoped bugs, gradually increasing the complexity to independent API endpoints as their confidence grew.

### Result (R)
- Within three months, the junior developer was independently delivering complex microservice features and their PR approval rate improved from 40% to 90% on the first pass.
- The wiki documentation I created became the standard onboarding material for all new engineering hires.

### Key Interview Takeaways for Canada Market
- **Tone:** Empathetic, leadership-focused, patient, and collaborative.
- **Key Buzzwords:** Pair Programming, Psychological Safety, Gradual Autonomy, Constructive PR Reviews, Knowledge Sharing.
