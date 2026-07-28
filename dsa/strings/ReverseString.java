public class ReverseString {
    public static void main(String[] args) {
        String word = "java";

        // Build the reversed string character by character.
        String reversed = "";
        for (int i = word.length() - 1; i >= 0; i--) {
            reversed += word.charAt(i);
        }

        System.out.println("Original: " + word);
        System.out.println("Reversed: " + reversed);
    }
}
