import java.util.*;

/**
 * LeetCode 49 — Group Anagrams
 *
 * Given an array of strings strs, group the anagrams together.
 * You can return the answer in any order.
 *
 * Example 1:
 * Input: strs = ["eat","tea","tan","ate","nat","bat"]
 * Output: [["bat"],["nat","tan"],["ate","eat","tea"]]
 *
 * Example 2:
 * Input: strs = [""]
 * Output: [[""]]
 *
 * Example 3:
 * Input: strs = ["a"]
 * Output: [["a"]]
 */
public class GroupAnagrams {

    /**
     * Approach 1: Categorize by Sorted String (Standard & Most Intuitive)
     *
     * Logic:
     * - Two strings are anagrams if and only if their sorted strings are equal.
     * - Use the sorted string as the Map key: "eat" -> "aet", "tea" -> "aet".
     *
     * Complexity:
     * - Time: O(N * K log K), where N is number of strings and K is max length of a
     * string.
     * - Space: O(N * K) to store the grouped strings in the map.
     */
    public static List<List<String>> groupAnagrams(String[] strs) {
        if (strs == null || strs.length == 0) {
            return new ArrayList<>();
        }

        Map<String, List<String>> map = new HashMap<>();

        for (String s : strs) {
            char[] chars = s.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);

            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }

        List<List<String>> result = new ArrayList<>(map.values());
        // Sort by group size ascending (smallest group first, e.g. size 1, 2, 3...)
        result.sort(Comparator.comparingInt(List::size));
        // Note: For descending (largest group first), use:
        // result.sort((a, b) -> Integer.compare(b.size(), a.size()));

        return result;
    }

    /**
     * Approach 2: Categorize by Arrays.toString(count)
     *
     * Note:
     * - Must use Arrays.toString(count), NOT count.toString() (which returns
     * "[I@hashcode").
     * - Generates key: "[1, 0, 0, ..., 1]".
     */
    public static List<List<String>> groupAnagramsByArraysToString(String[] strs) {
        if (strs == null || strs.length == 0) {
            return new ArrayList<>();
        }

        Map<String, List<String>> map = new HashMap<>();

        for (String s : strs) {
            int[] count = new int[26];
            for (char c : s.toCharArray()) {
                count[c - 'a']++;
            }

            // Simple one-liner: produces "[1, 0, 0, ...]"
            String key = Arrays.toString(count);
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }

        List<List<String>> result = new ArrayList<>(map.values());
        // Sort by group size ascending (smallest group first, e.g. size 1, 2, 3...)
        result.sort(Comparator.comparingInt(List::size));
        // Note: For descending (largest group first), use:
        // result.sort((a, b) -> Integer.compare(b.size(), a.size()));

        return result;
    }

    /**
     * Approach 3: Ultra-Fast Count Key using char[26] (Zero StringBuilder Overhead)
     *
     * Logic:
     * - Use char[26] as the counter array (char behaves as an unsigned 16-bit
     * integer).
     * - 'new String(count)' converts the 26 counts directly to a 26-char string
     * key!
     * - Eliminates StringBuilder, delimiters, and comma formatting completely.
     */
    public static List<List<String>> groupAnagramsFastest(String[] strs) {
        if (strs == null || strs.length == 0) {
            return new ArrayList<>();
        }

        Map<String, List<String>> map = new HashMap<>();

        for (String s : strs) {
            char[] count = new char[26];
            for (char c : s.toCharArray()) {
                count[c - 'a']++;
            }

            String key = new String(count);
            System.out.println("Key: " + key);
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);

        }
        System.out.println("Grouped Anagrams: " + map);
        List<List<String>> result = new ArrayList<>(map.values());
        // Sort by group size ascending (smallest group first, e.g. size 1, 2, 3...)
        result.sort(Comparator.comparingInt(List::size));
        // Note: For descending (largest group first), use:
        // result.sort((a, b) -> Integer.compare(b.size(), a.size()));

        return result;
    }

    public static void main(String[] args) {
        String[] test1 = { "eat", "tea", "tan", "ate", "nat", "bat" };
        System.out.println("Test 1 Input: " + Arrays.toString(test1));
        System.out.println("1. Sorted Key:           " + groupAnagrams(test1));
        System.out.println("2. Arrays.toString Key:   " + groupAnagramsByArraysToString(test1));
        System.out.println("3. char[26] Fast Key:     " + groupAnagramsFastest(test1));
        System.out.println();

        String[] test2 = { "" };
        System.out.println("Test 2 Input: " + Arrays.toString(test2));
        System.out.println("Result: " + groupAnagramsFastest(test2));
        System.out.println();

        String[] test3 = { "a" };
        System.out.println("Test 3 Input: " + Arrays.toString(test3));
        System.out.println("Result: " + groupAnagramsFastest(test3));
        System.out.println();

        String[] test4 = { "listen", "silent", "enlist", "hello", "world" };
        System.out.println("Test 4 Input: " + Arrays.toString(test4));
        System.out.println("Result: " + groupAnagramsFastest(test4));
    }
}
