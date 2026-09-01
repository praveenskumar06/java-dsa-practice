import java.util.Arrays;
import java.util.List;

public class ContainsDuplicateUsingStreams {
    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 2);

        boolean hasDuplicate = nums.stream()
                .distinct()
                .count() != nums.size();

        System.out.println("Contains duplicate: " + hasDuplicate);
    }
}
