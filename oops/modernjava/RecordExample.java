package modernjava;

public class RecordExample {

    // 1. Basic Record: The compiler generates constructor, getters, equals,
    // hashCode, and toString.
    public record UserResponse(String id, String username, String email) {
    }

    // 2. Record with a Compact Constructor for Validation
    public record BankTransaction(String accountId, double amount) {
        // Compact constructor: runs before the implicit assignment of fields
        public BankTransaction {
            if (amount <= 0) {
                throw new IllegalArgumentException("Transaction amount must be positive");
            }
        }

        // You can also add custom behavior to a Record
        public boolean isLargeTransaction() {
            return amount > 10_000.00;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Java Record Example ===");

        // Using Basic Record
        UserResponse user = new UserResponse("101", "praveen", "praveen@example.com");
        System.out.println("User ID: " + user.id()); // Notice no "get" prefix!
        System.out.println("User toString(): " + user); // toString is auto-generated beautifully

        // Using Compact Constructor Record
        BankTransaction tx = new BankTransaction("ACC-999", 5000.50);
        System.out.println("Transaction toString(): " + tx);
        System.out.println("Is Large Transaction? " + tx.isLargeTransaction());

        System.out.println("\nTrying an invalid transaction (uncomment in code to test exception):");
        // BankTransaction invalidTx = new BankTransaction("ACC-999", -100);
        // This will throw: java.lang.IllegalArgumentException: Transaction amount must
        // be positive
    }
}
