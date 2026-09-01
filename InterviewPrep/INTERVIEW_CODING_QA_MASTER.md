# Interview Coding Q&A Master — Praveen

---

## Day 1 Batch: Arrays & Two-Pointer Patterns (Option-C Format)

---

### Question 1: Two Sum II — Input Array Is Sorted (LeetCode #167)

#### Clarifying Questions
1. Is the array guaranteed to be sorted in non-decreasing order? *(Yes)*
2. Can we use the same element twice? *(No, must use two distinct indices)*
3. Is 1-based or 0-based indexing expected in the output? *(1-based indexing as per standard spec)*

#### Constraints
- `2 <= numbers.length <= 3 * 10^4`
- `-1000 <= numbers[i] <= 1000`
- Guaranteed exactly one valid solution exists.
- Memory limit: Must run in `O(1)` extra space.

#### Brute Force Approach
- **Logic:** Nested loops comparing every pair (i, j) where i < j.
- **Time Complexity:** `O(N^2)`
- **Space Complexity:** `O(1)`

#### Optimized Solution (Java 21)
```java
package InterviewPrep;

public class TwoSumSorted {
    public static int[] twoSum(int[] numbers, int target) {
        int left = 0;
        int right = numbers.length - 1;
        
        while (left < right) {
            int sum = numbers[left] + numbers[right];
            if (sum == target) {
                return new int[]{left + 1, right + 1}; // 1-based index
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        return new int[]{-1, -1};
    }
}
```

#### Edge Cases
- Negative target values (e.g., `numbers = [-5, -3, 0, 2], target = -8`).
- Array with minimum size (`N = 2`).
- Array containing duplicate values (e.g., `[2, 2, 3, 4], target = 4`).

#### Test Cases
- Input: `numbers = [2, 7, 11, 15], target = 9` -> Output: `[1, 2]`
- Input: `numbers = [2, 3, 4], target = 6` -> Output: `[1, 3]`
- Input: `numbers = [-1, 0], target = -1` -> Output: `[1, 2]`

#### Follow-ups
- What if the array is unsorted? *(Use HashMap for `O(N)` time and `O(N)` space)*
- What if we need to return all unique pairs that sum up to target? *(Skip duplicate adjacent values in two-pointer loop)*

#### Microservices Version
In an order payment matching service, match incoming credit transactions with debit transactions that equal a target settlement value.

#### Concurrency Version
Divide large sorted financial transaction logs into partitions and process sub-ranges concurrently using Java `CompletableFuture` or Virtual Threads (`Executors.newVirtualThreadPerTaskExecutor()`).

#### System Design Extension
If financial data is sharded across multiple GCP Bigtable nodes, retrieve pre-sorted range partitions and perform a distributed two-pointer merge search across node boundaries.

#### Time & Space Complexity
- **Time Complexity:** `O(N)` — Single pass over array.
- **Space Complexity:** `O(1)` — Constant auxiliary space.

---

### Question 2: Merge Sorted Array (LeetCode #88)

#### Clarifying Questions
1. Is array `nums1` guaranteed to have sufficient capacity (m + n) to hold elements from `nums2`? *(Yes)*
2. Should the merge be performed in-place on `nums1`? *(Yes)*

#### Constraints
- `nums1.length == m + n`
- `nums2.length == n`
- `0 <= m, n <= 2000`
- `-10^9 <= nums1[i], nums2[j] <= 10^9`

#### Brute Force Approach
- Copy `nums2` into the tail of `nums1` and call `Arrays.sort(nums1)`.
- **Time Complexity:** `O((m+n) * log(m+n))`
- **Space Complexity:** `O(1)`

#### Optimized Solution (Java 21 — Three-Pointer from Back)
```java
package InterviewPrep;

public class MergeSortedArray {
    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        int p1 = m - 1;
        int p2 = n - 1;
        int k = m + n - 1;

        while (p1 >= 0 && p2 >= 0) {
            if (nums1[p1] > nums2[p2]) {
                nums1[k--] = nums1[p1--];
            } else {
                nums1[k--] = nums2[p2--];
            }
        }

        // Fill remaining elements from nums2 if any
        while (p2 >= 0) {
            nums1[k--] = nums2[p2--];
        }
    }
}
```

#### Edge Cases
- `m = 0` (first array has no initial elements).
- `n = 0` (second array is empty).
- All elements in `nums2` are strictly smaller than `nums1`.

#### Test Cases
- Input: `nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3` -> Result `nums1 = [1,2,2,3,5,6]`
- Input: `nums1 = [1], m = 1, nums2 = [], n = 0` -> Result `nums1 = [1]`
- Input: `nums1 = [0], m = 0, nums2 = [1], n = 1` -> Result `nums1 = [1]`

#### Follow-ups
- How to merge K sorted arrays? *(Use Min-Heap / PriorityQueue in `O(N * log K)` time)*

#### Microservices Version
Real-time stream merging of two sorted timestamp log streams from microservices before ingestion into GCP Cloud Logging.

#### Concurrency Version
Process chunked log merges across worker threads using Java Parallel Streams or `ForkJoinPool`.

#### System Design Extension
Design a distributed Log-Structured Merge (LSM) Tree compaction worker similar to Apache Cassandra or GCP Bigtable SSTables.

#### Time & Space Complexity
- **Time Complexity:** `O(m + n)`
- **Space Complexity:** `O(1)`

---

### Question 3: Container With Most Water (LeetCode #11)

#### Clarifying Questions
1. Can height be zero? *(Yes, a line of height 0 produces 0 area)*
2. Can we slant the container? *(No, lines are vertical)*

#### Constraints
- `n == height.length`
- `2 <= n <= 10^5`
- `0 <= height[i] <= 10^4`

#### Brute Force Approach
- Check area between every pair (i, j): `area = min(h[i], h[j]) * (j - i)`.
- **Time Complexity:** `O(N^2)`
- **Space Complexity:** `O(1)`

#### Optimized Solution (Java 21 — Two Pointers)
```java
package InterviewPrep;

public class ContainerWithMostWater {
    public static int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int maxArea = 0;

        while (left < right) {
            int currentHeight = Math.min(height[left], height[right]);
            int width = right - left;
            int currentArea = currentHeight * width;
            maxArea = Math.max(maxArea, currentArea);

            // Move the pointer pointing to the shorter line
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxArea;
    }
}
```

#### Edge Cases
- Array with 2 elements only.
- All heights are identical (e.g., `[5, 5, 5, 5]`).
- Strictly increasing or decreasing height arrays.

#### Test Cases
- Input: `height = [1,8,6,2,5,4,8,3,7]` -> Output: `49`
- Input: `height = [1,1]` -> Output: `1`

#### Follow-ups
- Trapping Rain Water (LeetCode #42)? *(Requires keeping track of max left and right boundaries)*

#### Microservices Version
Calculating optimal bandwidth allocation windows across server clusters with variable capacity thresholds.

#### Concurrency Version
Parallel evaluation of height segments using Java 21 Virtual Threads for multi-node analytical processing.

#### System Design Extension
Resource partitioning and dynamic load balancer buffer allocation in high-traffic Cloud API Gateways.

#### Time & Space Complexity
- **Time Complexity:** `O(N)`
- **Space Complexity:** `O(1)`

---

### Question 4: 3Sum (LeetCode #15)

#### Clarifying Questions
1. Should the output contain duplicate triplets? *(No, triplets must be unique)*
2. Does order of triplets matter in output list? *(No)*

#### Constraints
- `3 <= nums.length <= 3000`
- `-10^5 <= nums[i] <= 10^5`

#### Brute Force Approach
- Three nested loops + Set to remove duplicates.
- **Time Complexity:** `O(N^3)`
- **Space Complexity:** `O(N)`

#### Optimized Solution (Java 21 — Sort + Two Pointers)
```java
package InterviewPrep;

import java.util.*;

public class ThreeSum {
    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < nums.length - 2; i++) {
            // Avoid duplicate target values
            if (i > 0 && nums[i] == nums[i - 1]) continue;

            int left = i + 1;
            int right = nums.length - 1;

            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (sum == 0) {
                    result.add(List.of(nums[i], nums[left], nums[right]));
                    while (left < right && nums[left] == nums[left + 1]) left++;
                    while (left < right && nums[right] == nums[right - 1]) right--;
                    left++;
                    right--;
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return result;
    }
}
```

#### Edge Cases
- All zeroes array: `[0, 0, 0, 0]`.
- No valid triplet exists.
- Large negative and positive numbers.

#### Test Cases
- Input: `nums = [-1,0,1,2,-1,-4]` -> Output: `[[-1,-1,2],[-1,0,1]]`
- Input: `nums = [0,1,1]` -> Output: `[]`
- Input: `nums = [0,0,0]` -> Output: `[[0,0,0]]`

#### Follow-ups
- 4Sum? *(Generalize to KSum using recursion + 2Pointer base case)*

#### Microservices Version
Triangulating matching transactions across 3 ledger services to audit zero-sum balancing.

#### Concurrency Version
Distribute initial target iteration (`i`) across thread pool workers with concurrent safe collection (`CopyOnWriteArrayList` or partitioned queues).

#### System Design Extension
Distributed fraud detection engine evaluating multi-party transaction triangles across bank clusters.

#### Time & Space Complexity
- **Time Complexity:** `O(N^2)`
- **Space Complexity:** `O(1)` auxiliary (excluding output storage).

---

### Question 5: Remove Duplicates from Sorted Array (LeetCode #26)

#### Clarifying Questions
1. Is in-place modification mandatory? *(Yes)*
2. What should be returned? *(Integer k representing number of unique elements)*

#### Constraints
- `1 <= nums.length <= 3 * 10^4`
- `-100 <= nums[i] <= 100`
- Array is sorted in non-decreasing order.

#### Brute Force Approach
- Copy to `LinkedHashSet` and write back to array.
- **Time Complexity:** `O(N)`
- **Space Complexity:** `O(N)` (Violates in-place constraint)

#### Optimized Solution (Java 21 — Two Pointers)
```java
package InterviewPrep;

public class RemoveDuplicates {
    public static int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;
        int writeIndex = 1;

        for (int readIndex = 1; readIndex < nums.length; readIndex++) {
            if (nums[readIndex] != nums[readIndex - 1]) {
                nums[writeIndex++] = nums[readIndex];
            }
        }
        return writeIndex;
    }
}
```

#### Edge Cases
- Array with 1 element.
- All elements identical (`[1, 1, 1]`).
- All elements unique (`[1, 2, 3]`).

#### Test Cases
- Input: `nums = [1,1,2]` -> Output: `k = 2, nums = [1,2,...]`
- Input: `nums = [0,0,1,1,1,2,2,3,3,4]` -> Output: `k = 5, nums = [0,1,2,3,4,...]`

#### Follow-ups
- Allow at most two duplicates per element (LeetCode #80)? *(Use condition `nums[read] != nums[write - 2]`)*

#### Microservices Version
Deduplicating ordered database ID sequences before writing into GCP BigQuery tables.

#### Concurrency Version
Deduplicate parallel stream partitions and combine results using lock-free ring buffers.

#### System Design Extension
Streaming log compression and deduplication at network proxy level in Cloud API Gateways.

#### Time & Space Complexity
- **Time Complexity:** `O(N)`
- **Space Complexity:** `O(1)`

---

### Question 6: Move Zeroes (LeetCode #283)

#### Clarifying Questions
1. Must relative order of non-zero elements be maintained? *(Yes)*
2. Must it be done in-place? *(Yes)*

#### Constraints
- `1 <= nums.length <= 10^4`
- `-2^31 <= nums[i] <= 2^31 - 1`

#### Brute Force Approach
- Create extra array, copy non-zeroes, fill remainder with zeroes, copy back.
- **Time Complexity:** `O(N)`
- **Space Complexity:** `O(N)`

#### Optimized Solution (Java 21 — Two Pointers / Swap)
```java
package InterviewPrep;

public class MoveZeroes {
    public static void moveZeroes(int[] nums) {
        int lastNonZeroFoundAt = 0;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                int temp = nums[lastNonZeroFoundAt];
                nums[lastNonZeroFoundAt++] = nums[i];
                nums[i] = temp;
            }
        }
    }
}
```

#### Edge Cases
- All zeroes array: `[0, 0, 0]`.
- No zeroes present: `[1, 2, 3]`.
- Single element array: `[0]`.

#### Test Cases
- Input: `nums = [0,1,0,3,12]` -> Output: `[1,3,12,0,0]`
- Input: `nums = [0]` -> Output: `[0]`

#### Follow-ups
- Minimize total operations when array has many zeroes? *(Swap only when `i != lastNonZeroFoundAt`)*

#### Microservices Version
Compacting sparse user session state arrays in high-memory microservices.

#### Concurrency Version
In-place memory defragmentation across off-heap buffers using Unsafe / Foreign Function API in Java 21.

#### System Design Extension
In-memory cache array eviction and tombstone cleanup worker in distributed Redis nodes.

#### Time & Space Complexity
- **Time Complexity:** `O(N)`
- **Space Complexity:** `O(1)`

---

### Question 7: Minimum Size Subarray Sum (LeetCode #209)

#### Clarifying Questions
1. Are all elements strictly positive integers? *(Yes)*
2. What to return if no subarray sums to >= target? *(Return 0)*

#### Constraints
- `1 <= target <= 10^9`
- `1 <= nums.length <= 10^5`
- `1 <= nums[i] <= 10^4`

#### Brute Force Approach
- Evaluate sum of all possible subarrays (i, j) using nested loops.
- **Time Complexity:** `O(N^2)`
- **Space Complexity:** `O(1)`

#### Optimized Solution (Java 21 — Sliding Window)
```java
package InterviewPrep;

public class MinSubArrayLen {
    public static int minSubArrayLen(int target, int[] nums) {
        int left = 0;
        int currentSum = 0;
        int minLength = Integer.MAX_VALUE;

        for (int right = 0; right < nums.length; right++) {
            currentSum += nums[right];

            while (currentSum >= target) {
                minLength = Math.min(minLength, right - left + 1);
                currentSum -= nums[left++];
            }
        }

        return minLength == Integer.MAX_VALUE ? 0 : minLength;
    }
}
```

#### Edge Cases
- Single element >= target.
- Total array sum is smaller than target.
- Target is 1 and array contains positive numbers.

#### Test Cases
- Input: `target = 7, nums = [2,3,1,2,4,3]` -> Output: `2` (subarray `[4,3]`)
- Input: `target = 4, nums = [1,4,4]` -> Output: `1`
- Input: `target = 11, nums = [1,1,1,1,1,1,1,1]` -> Output: `0`

#### Follow-ups
- Can you solve it in `O(N * log N)` time? *(Use Prefix Sums + Binary Search)*

#### Microservices Version
Calculating shortest time window for batch processing where event count meets or exceeds threshold T.

#### Concurrency Version
Sliding window rate-limiting counter evaluated in thread-safe atomic counters (`AtomicLongArray`).

#### System Design Extension
Distributed sliding-window rate limiter (Token Bucket / Leaky Bucket) pattern across GCP API Gateways.

#### Time & Space Complexity
- **Time Complexity:** `O(N)` — Each element processed at most twice by `left` and `right` pointers.
- **Space Complexity:** `O(1)`

---

## Day 2 Batch: Binary Search & Fast/Slow Pointers (Option-C Format)

---

### Question 8: Binary Search (LeetCode #704)

#### Clarifying Questions
1. Is the array sorted in ascending order? *(Yes)*
2. Are there duplicate elements? *(All elements are unique)*

#### Constraints
- `1 <= nums.length <= 10^4`
- `-10^4 < nums[i], target < 10^4`

#### Brute Force Approach
- Linear scan through the array.
- **Time Complexity:** `O(N)`
- **Space Complexity:** `O(1)`

#### Optimized Solution (Java 21)
```java
package InterviewPrep;

public class BinarySearch {
    public static int search(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2; // Prevents integer overflow
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }
}
```

#### Edge Cases
- Target is smaller than the first element or larger than the last.
- Array with 1 element.

#### Test Cases
- Input: `nums = [-1,0,3,5,9,12], target = 9` -> Output: `4`
- Input: `nums = [-1,0,3,5,9,12], target = 2` -> Output: `-1`

#### Follow-ups
- How would you handle a rotated sorted array? *(See Question 10)*

#### Microservices Version
Finding a specific timestamped log entry in a pre-sorted distributed log file.

#### Concurrency Version
Search across partitioned arrays using `CompletableFuture.anyOf()` in Java.

#### System Design Extension
Efficient index lookup in a B-Tree structure used by PostgreSQL or MySQL databases.

#### Time & Space Complexity
- **Time Complexity:** `O(log N)`
- **Space Complexity:** `O(1)`

---

### Question 9: Search a 2D Matrix (LeetCode #74)

#### Clarifying Questions
1. Is the matrix strictly sorted from top-left to bottom-right? *(Yes, first integer of each row is greater than the last integer of the previous row)*

#### Constraints
- `m == matrix.length`, `n == matrix[i].length`
- `1 <= m, n <= 100`

#### Brute Force Approach
- Iterate through all elements `O(M * N)`.

#### Optimized Solution (Java 21 — Treat as 1D Array)
```java
package InterviewPrep;

public class Search2DMatrix {
    public static boolean searchMatrix(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return false;
        int m = matrix.length;
        int n = matrix[0].length;
        int left = 0, right = m * n - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int midElement = matrix[mid / n][mid % n];
            
            if (midElement == target) {
                return true;
            } else if (midElement < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return false;
    }
}
```

#### Edge Cases
- 1x1 matrix.
- Target not in matrix boundaries.

#### Time & Space Complexity
- **Time Complexity:** `O(log(M * N))`
- **Space Complexity:** `O(1)`

---

### Question 10: Find Minimum in Rotated Sorted Array (LeetCode #153)

#### Clarifying Questions
1. Are elements unique? *(Yes)*
2. How many times is the array rotated? *(1 to N times)*

#### Optimized Solution (Java 21)
```java
package InterviewPrep;

public class FindMinRotated {
    public static int findMin(int[] nums) {
        int left = 0, right = nums.length - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;
            // If mid is greater than right, min is in the right half
            if (nums[mid] > nums[right]) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return nums[left];
    }
}
```

#### System Design Extension
Finding the rotation offset in a consistently hashed ring (e.g., Cassandra or DynamoDB) to locate the primary node for a specific hash key.

#### Time & Space Complexity
- **Time Complexity:** `O(log N)`
- **Space Complexity:** `O(1)`

---

### Question 11: Linked List Cycle (LeetCode #141)

#### Clarifying Questions
1. Can the list be modified? *(Ideally no)*
2. What should be returned? *(A boolean)*

#### Optimized Solution (Java 21 — Fast/Slow Pointers)
```java
package InterviewPrep;

class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; next = null; }
}

public class LinkedListCycle {
    public static boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) return false;
        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return true;
        }
        return false;
    }
}
```

#### System Design Extension
Detecting circular dependencies in microservice deployment pipelines or infinite routing loops in an API Gateway configuration.

#### Time & Space Complexity
- **Time Complexity:** `O(N)`
- **Space Complexity:** `O(1)`

---

### Question 12: Middle of the Linked List (LeetCode #876)

#### Optimized Solution (Java 21 — Fast/Slow Pointers)
```java
package InterviewPrep;

public class MiddleLinkedList {
    public static ListNode middleNode(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }
}
```

#### Time & Space Complexity
- **Time Complexity:** `O(N)`
- **Space Complexity:** `O(1)`

---

### Question 13: Find the Duplicate Number (LeetCode #287)

#### Constraints
- Array contains `N+1` integers in the range `[1, N]`.
- Must not modify the array.
- Space must be `O(1)`.

#### Optimized Solution (Java 21 — Floyd's Cycle Detection)
```java
package InterviewPrep;

public class FindDuplicate {
    public static int findDuplicate(int[] nums) {
        int slow = nums[0];
        int fast = nums[0];

        // Phase 1: Intersection point
        do {
            slow = nums[slow];
            fast = nums[nums[fast]];
        } while (slow != fast);

        // Phase 2: Find entrance to cycle
        slow = nums[0];
        while (slow != fast) {
            slow = nums[slow];
            fast = nums[fast];
        }
        return slow;
    }
}
```

#### Time & Space Complexity
- **Time Complexity:** `O(N)`
- **Space Complexity:** `O(1)`

---

### Question 14: Koko Eating Bananas (LeetCode #875)

#### Clarifying Questions
1. Does Koko stop eating for the hour if she finishes a pile early? *(Yes)*
2. What if K is very large? *(Upper bound for K is the maximum pile size)*

#### Optimized Solution (Java 21 — Binary Search on Answer)
```java
package InterviewPrep;

import java.util.Arrays;

public class KokoEatingBananas {
    public static int minEatingSpeed(int[] piles, int h) {
        int left = 1;
        int right = Arrays.stream(piles).max().getAsInt();

        while (left < right) {
            int mid = left + (right - left) / 2;
            int hours = 0;
            
            for (int pile : piles) {
                hours += Math.ceil((double) pile / mid);
            }
            
            if (hours <= h) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
}
```

#### Microservices Version
Optimizing batch-processing speed for background workers (e.g., Spring Batch) consuming from a message queue (Kafka/RabbitMQ) to complete all jobs within an SLA window.

#### Time & Space Complexity
- **Time Complexity:** `O(N log(Max(P)))` where `Max(P)` is the maximum pile size.
- **Space Complexity:** `O(1)`

---

## Day 4 Batch: Stacks & Sliding Window Advanced (Option-C Format)

---

### Question 15: Valid Parentheses (LeetCode #20)

#### Clarifying Questions
1. Does the string contain only parentheses characters `()[]{}`? *(Yes)*
2. Can the string be empty? *(A string of length 0 could be considered valid, though constraints usually specify length >= 1)*

#### Constraints
- `1 <= s.length <= 10^4`
- `s` consists of parentheses only.

#### Optimized Solution (Java 21 — Stack)
```java
package InterviewPrep;

import java.util.Stack;

public class ValidParentheses {
    public static boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        
        for (char c : s.toCharArray()) {
            if (c == '(') stack.push(')');
            else if (c == '{') stack.push('}');
            else if (c == '[') stack.push(']');
            else if (stack.isEmpty() || stack.pop() != c) {
                return false;
            }
        }
        return stack.isEmpty();
    }
}
```

#### System Design Extension
Ensuring structured log formats (like JSON) are well-formed before ingestion into GCP Cloud Logging, or validating nested HTML/XML structures in a web crawler.

#### Time & Space Complexity
- **Time Complexity:** `O(N)`
- **Space Complexity:** `O(N)`

---

### Question 16: Min Stack (LeetCode #155)

#### Clarifying Questions
1. Does `getMin()` remove the minimum element? *(No, it just retrieves it)*
2. Are all methods expected to run in `O(1)` time? *(Yes)*

#### Optimized Solution (Java 21 — Two Stacks)
```java
package InterviewPrep;

import java.util.Stack;

public class MinStack {
    private Stack<Integer> stack;
    private Stack<Integer> minStack;

    public MinStack() {
        stack = new Stack<>();
        minStack = new Stack<>();
    }
    
    public void push(int val) {
        stack.push(val);
        if (minStack.isEmpty() || val <= minStack.peek()) {
            minStack.push(val);
        }
    }
    
    public void pop() {
        if (stack.pop().equals(minStack.peek())) {
            minStack.pop();
        }
    }
    
    public int top() {
        return stack.peek();
    }
    
    public int getMin() {
        return minStack.peek();
    }
}
```

#### System Design Extension
Maintaining a rolling minimum latency metric in a monitoring agent where data points are constantly pushed and popped from a time window buffer.

#### Time & Space Complexity
- **Time Complexity:** `O(1)` for all operations.
- **Space Complexity:** `O(N)`

---

### Question 17: Daily Temperatures (LeetCode #739)

#### Clarifying Questions
1. What if a warmer temperature is never found? *(Return 0 for that position)*

#### Optimized Solution (Java 21 — Monotonic Decreasing Stack)
```java
package InterviewPrep;

import java.util.Stack;

public class DailyTemperatures {
    public static int[] dailyTemperatures(int[] temperatures) {
        int[] result = new int[temperatures.length];
        Stack<Integer> stack = new Stack<>(); // stores indices
        
        for (int i = 0; i < temperatures.length; i++) {
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                int index = stack.pop();
                result[index] = i - index;
            }
            stack.push(i);
        }
        return result;
    }
}
```

#### System Design Extension
Real-time stock price tick analysis to trigger "sell" events the moment a price rises above historical thresholds in algorithmic trading systems.

#### Time & Space Complexity
- **Time Complexity:** `O(N)`
- **Space Complexity:** `O(N)`

---

### Question 18: Evaluate Reverse Polish Notation (LeetCode #150)

#### Clarifying Questions
1. Are division operations truncating towards zero? *(Yes)*
2. Are inputs guaranteed to be valid RPN expressions? *(Yes)*

#### Optimized Solution (Java 21 — Stack)
```java
package InterviewPrep;

import java.util.Stack;

public class EvaluateRPN {
    public static int evalRPN(String[] tokens) {
        Stack<Integer> stack = new Stack<>();
        
        for (String token : tokens) {
            switch (token) {
                case "+" -> stack.push(stack.pop() + stack.pop());
                case "-" -> {
                    int b = stack.pop();
                    int a = stack.pop();
                    stack.push(a - b);
                }
                case "*" -> stack.push(stack.pop() * stack.pop());
                case "/" -> {
                    int b = stack.pop();
                    int a = stack.pop();
                    stack.push(a / b);
                }
                default -> stack.push(Integer.parseInt(token));
            }
        }
        return stack.pop();
    }
}
```

#### System Design Extension
Rule engines evaluating complex boolean logic conditions for dynamic pricing or discount applicability in e-commerce microservices.

#### Time & Space Complexity
- **Time Complexity:** `O(N)`
- **Space Complexity:** `O(N)`

---

### Question 19: Longest Substring Without Repeating Characters (LeetCode #3)

#### Clarifying Questions
1. Does the string contain only English letters? *(Can contain letters, digits, symbols, spaces)*
2. Is case sensitive? *(Yes)*

#### Optimized Solution (Java 21 — Sliding Window)
```java
package InterviewPrep;

import java.util.HashSet;
import java.util.Set;

public class LongestSubstring {
    public static int lengthOfLongestSubstring(String s) {
        Set<Character> set = new HashSet<>();
        int left = 0, maxLength = 0;
        
        for (int right = 0; right < s.length(); right++) {
            while (set.contains(s.charAt(right))) {
                set.remove(s.charAt(left));
                left++;
            }
            set.add(s.charAt(right));
            maxLength = Math.max(maxLength, right - left + 1);
        }
        return maxLength;
    }
}
```

#### Time & Space Complexity
- **Time Complexity:** `O(N)`
- **Space Complexity:** `O(min(N, M))` where M is character set size.

---

### Question 20: Longest Repeating Character Replacement (LeetCode #424)

#### Clarifying Questions
1. Do we only deal with uppercase English letters? *(Yes)*
2. What if K is larger than the string length? *(Result is string length)*

#### Optimized Solution (Java 21 — Sliding Window)
```java
package InterviewPrep;

public class CharacterReplacement {
    public static int characterReplacement(String s, int k) {
        int[] counts = new int[26];
        int left = 0, maxCount = 0, maxLength = 0;
        
        for (int right = 0; right < s.length(); right++) {
            maxCount = Math.max(maxCount, ++counts[s.charAt(right) - 'A']);
            
            // Current window length minus max frequency character count gives number of chars to replace
            while (right - left + 1 - maxCount > k) {
                counts[s.charAt(left) - 'A']--;
                left++;
            }
            maxLength = Math.max(maxLength, right - left + 1);
        }
        return maxLength;
    }
}
```

#### Time & Space Complexity
- **Time Complexity:** `O(N)`
- **Space Complexity:** `O(1)` (Array of size 26)

---

### Question 21: Permutation in String (LeetCode #567)

#### Clarifying Questions
1. Can strings contain spaces or uppercase letters? *(Assume lowercase English letters)*

#### Optimized Solution (Java 21 — Fixed Sliding Window)
```java
package InterviewPrep;

import java.util.Arrays;

public class PermutationInString {
    public static boolean checkInclusion(String s1, String s2) {
        if (s1.length() > s2.length()) return false;
        
        int[] s1Count = new int[26];
        int[] s2Count = new int[26];
        
        for (int i = 0; i < s1.length(); i++) {
            s1Count[s1.charAt(i) - 'a']++;
            s2Count[s2.charAt(i) - 'a']++;
        }
        
        for (int i = 0; i < s2.length() - s1.length(); i++) {
            if (Arrays.equals(s1Count, s2Count)) return true;
            s2Count[s2.charAt(i + s1.length()) - 'a']++;
            s2Count[s2.charAt(i) - 'a']--;
        }
        
        return Arrays.equals(s1Count, s2Count);
    }
}
```

#### System Design Extension
Anomaly detection scanning fixed-length event windows in a log stream to match malicious signature profiles.

#### Time & Space Complexity
- **Time Complexity:** `O(N)` where N is length of s2.
- **Space Complexity:** `O(1)`
