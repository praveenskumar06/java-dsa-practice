/**
 * FleetStats — Immutable record representing aggregated fleet health statistics
 * for a specific device type.
 *
 * Uses Java Record (introduced in Java 14/16+) for clean, boilerplate-free DTOs.
 */
public record FleetStats(
    int totalCount,
    int activeCount,
    double averageBattery,
    int overdueCount
) {
    // JavaBean-style accessor aliases for compatibility
    public int getTotalCount() { return totalCount; }
    public int getActiveCount() { return activeCount; }
    public double getAverageBattery() { return averageBattery; }
    public int getOverdueCount() { return overdueCount; }
}
