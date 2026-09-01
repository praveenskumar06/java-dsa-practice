import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SecondLargestUsingStreams {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(12, 45, 7, 89, 34, 89, 56);

        Integer secondLargest = numbers.stream()
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(1)
                .findFirst()
                .orElse(null);

        if (secondLargest != null) {
            System.out.println("Second largest: " + secondLargest);
        } else {
            System.out.println("There is no second largest element.");
        }
    }
}
