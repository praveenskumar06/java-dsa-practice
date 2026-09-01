package oops.modernjava;

// 1. Define the Sealed Interface and specify allowed implementations using 'permits'
sealed interface PaymentEvent permits PaymentSuccess, PaymentFailed {}

// 2. Subclasses must be final, sealed, or non-sealed. 
// Records are implicitly 'final', so they satisfy the sealed class requirement perfectly!
record PaymentSuccess(String transactionId, double amount) implements PaymentEvent {}
record PaymentFailed(String transactionId, String errorCode) implements PaymentEvent {}

public class SealedClassExample {

    // 3. Pattern Matching for Switch (Java 21+)
    public static String handleEvent(PaymentEvent event) {
        // Notice there is NO 'default' case needed!
        // The compiler knows PaymentEvent can ONLY be PaymentSuccess or PaymentFailed.
        return switch (event) {
            case PaymentSuccess success -> 
                "✅ Processed $" + success.amount() + " for TX: " + success.transactionId();
                
            case PaymentFailed failed -> 
                "❌ Alert: TX " + failed.transactionId() + " failed with code " + failed.errorCode();
        };
    }

    public static void main(String[] args) {
        System.out.println("=== Java Sealed Classes & Pattern Matching Example ===");

        PaymentEvent successEvent = new PaymentSuccess("TXN-7890", 250.00);
        PaymentEvent failedEvent = new PaymentFailed("TXN-1234", "ERR_INSUFFICIENT_FUNDS");

        System.out.println(handleEvent(successEvent));
        System.out.println(handleEvent(failedEvent));
    }
}
