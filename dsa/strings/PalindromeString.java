public class PalindromeString {
    public static boolean isPalindrome(String s) {
        if (s == null) return false;
        int i = 0, j = s.length() - 1;
        while (i < j) {
            while (i < j && !Character.isLetterOrDigit(s.charAt(i))) i++;
            while (i < j && !Character.isLetterOrDigit(s.charAt(j))) j--;
            char a = Character.toLowerCase(s.charAt(i));
            char b = Character.toLowerCase(s.charAt(j));
            if (a != b) return false;
            i++; j--;
        }
        return true;
    }

    public static boolean isPalindromeSimple(String s) {
        if (s == null) return false;
        int i = 0, j = s.length() - 1;
        while (i < j) {
            if (s.charAt(i) != s.charAt(j)) return false;
            i++; j--;
        }
        return true;
    }

    public static void main(String[] args) {
        String[] tests = {
            "racecar",
            "A man, a plan, a canal: Panama",
            "hello",
            "",
            "Aba"
        };
        for (String t : tests) {
            System.out.println('"' + t + '"' + " -> normalized: " + isPalindrome(t) + ", simple: " + isPalindromeSimple(t));
        }
    }
}
