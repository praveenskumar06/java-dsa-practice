import java.util.*;

/**
 * Test class for DeviceLifecycleManager
 *
 * Karat / Citi Bank Interview Exercise
 *
 * Run all tests:  javac *.java && java DeviceLifecycleManagerTest
 *
 * Phase 1 tests will FAIL until you fix the bug in updateStatus().
 * Phase 2 tests will FAIL until you implement addDevice().
 * Phase 3 tests will FAIL until you implement getActiveDeviceCountByType().
 */
public class DeviceLifecycleManagerTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║    DeviceLifecycleManager — Interview Tests     ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        // ---- PHASE 1: Bug Fix (updateStatus) ----
        System.out.println("━━━ PHASE 1: Bug Fix — updateStatus() ━━━");
        testUpdateStatus_ValidTransition_RegisteredToActive();
        testUpdateStatus_ValidTransition_ActiveToRetired();
        testUpdateStatus_ValidTransition_ActiveToPending();
        testUpdateStatus_ValidTransition_PendingToActive();
        testUpdateStatus_InvalidTransition_ActiveToRegistered();   // ← THE BUG
        testUpdateStatus_InvalidTransition_RetiredToActive();
        testUpdateStatus_InvalidTransition_MaintenanceToActive();
        testUpdateStatus_NonExistentDevice();
        testUpdateStatus_InvalidStatusValue();

        // ---- PHASE 2: Implement addDevice ----
        System.out.println("\n━━━ PHASE 2: Implement — addDevice() ━━━");
        testAddDevice_Success();
        testAddDevice_DuplicateId();
        testAddDevice_InvalidStatus();
        testAddDevice_NegativeRegisteredDay();
        testAddDevice_ZeroRegisteredDay_ShouldPass();

        // ---- PHASE 3: Implement getActiveDeviceCountByType ----
        System.out.println("\n━━━ PHASE 3: Implement — getActiveDeviceCountByType() ━━━");
        testGetActive_CountsByType();
        testGetActive_ExcludesRetired();
        testGetActive_ExcludesMaintenance();
        testGetActive_ExcludesNegativeRegisteredDay();
        testGetActive_IncludesRegisteredPendingActive();
        testGetActive_EmptyDevices();

        // ---- PHASE 4: Implement removeDevice ----
        System.out.println("\n━━━ PHASE 4: Implement — removeDevice() ━━━");
        testRemoveDevice_RetiredDevice_ShouldSucceed();
        testRemoveDevice_ActiveDevice_ShouldFail();
        testRemoveDevice_RegisteredDevice_ShouldFail();
        testRemoveDevice_MaintenanceDevice_ShouldFail();
        testRemoveDevice_NonExistentDevice_ShouldFail();

        // ---- PHASE 5: Implement getDevicesDueForMaintenance ----
        System.out.println("\n━━━ PHASE 5: Implement — getDevicesDueForMaintenance() ━━━");
        testDueForMaintenance_BasicOverdueSortedDesc();
        testDueForMaintenance_ExcludesRetiredAndMaintenance();
        testDueForMaintenance_ExcludesNegativeRegisteredDay();
        testDueForMaintenance_ExactlyAtThreshold_Excluded();
        testDueForMaintenance_EmptyWhenNoneOverdue();

        // ---- Summary ----
        System.out.println("\n══════════════════════════════════════════════════");
        System.out.println("  RESULTS:  " + passed + " passed,  " + failed + " failed,  " + (passed + failed) + " total");
        System.out.println("══════════════════════════════════════════════════");

        if (failed > 0) {
            System.out.println("  ⚠ Some tests failed. Keep going!\n");
        } else {
            System.out.println("  🎉 All tests passed! Great job!\n");
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    //  PHASE 1 TESTS — updateStatus
    // ═══════════════════════════════════════════════════════════════════

    static void testUpdateStatus_ValidTransition_RegisteredToActive() {
        Map<String, Device> devices = new HashMap<>();
        devices.put("D-1", new Device("D-1", "SENSOR", 5, 0, 80, "REGISTERED"));
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(devices);

        boolean result = mgr.updateStatus("D-1", "ACTIVE");
        assertEqual("REGISTERED → ACTIVE should succeed", true, result);
        assertEqual("Status should now be ACTIVE", "ACTIVE", mgr.getDevices().get("D-1").getStatus());
    }

    static void testUpdateStatus_ValidTransition_ActiveToRetired() {
        Map<String, Device> devices = new HashMap<>();
        devices.put("D-2", new Device("D-2", "CAMERA", 3, 1, 90, "ACTIVE"));
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(devices);

        boolean result = mgr.updateStatus("D-2", "RETIRED");
        assertEqual("ACTIVE → RETIRED should succeed", true, result);
        assertEqual("Status should now be RETIRED", "RETIRED", mgr.getDevices().get("D-2").getStatus());
    }

    static void testUpdateStatus_ValidTransition_ActiveToPending() {
        Map<String, Device> devices = new HashMap<>();
        devices.put("D-3", new Device("D-3", "GATEWAY", 7, 2, 70, "ACTIVE"));
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(devices);

        boolean result = mgr.updateStatus("D-3", "PENDING");
        assertEqual("ACTIVE → PENDING should succeed", true, result);
    }

    static void testUpdateStatus_ValidTransition_PendingToActive() {
        Map<String, Device> devices = new HashMap<>();
        devices.put("D-4", new Device("D-4", "THERMOSTAT", 2, 0, 65, "PENDING"));
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(devices);

        boolean result = mgr.updateStatus("D-4", "ACTIVE");
        assertEqual("PENDING → ACTIVE should succeed", true, result);
    }

    // ★ THIS IS THE TEST THAT EXPOSES THE BUG ★
    static void testUpdateStatus_InvalidTransition_ActiveToRegistered() {
        Map<String, Device> devices = new HashMap<>();
        devices.put("D-5", new Device("D-5", "SENSOR", 10, 3, 85, "ACTIVE"));
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(devices);

        boolean result = mgr.updateStatus("D-5", "REGISTERED");
        assertEqual("ACTIVE → REGISTERED should FAIL (invalid transition)", false, result);
        assertEqual("Status should remain ACTIVE", "ACTIVE", mgr.getDevices().get("D-5").getStatus());
    }

    static void testUpdateStatus_InvalidTransition_RetiredToActive() {
        Map<String, Device> devices = new HashMap<>();
        devices.put("D-6", new Device("D-6", "CAMERA", 8, 4, 50, "RETIRED"));
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(devices);

        boolean result = mgr.updateStatus("D-6", "ACTIVE");
        assertEqual("RETIRED → ACTIVE should FAIL (terminal state)", false, result);
        assertEqual("Status should remain RETIRED", "RETIRED", mgr.getDevices().get("D-6").getStatus());
    }

    static void testUpdateStatus_InvalidTransition_MaintenanceToActive() {
        Map<String, Device> devices = new HashMap<>();
        devices.put("D-7", new Device("D-7", "ACTUATOR", 6, 6, 40, "MAINTENANCE"));
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(devices);

        boolean result = mgr.updateStatus("D-7", "ACTIVE");
        assertEqual("MAINTENANCE → ACTIVE should FAIL (terminal state)", false, result);
        assertEqual("Status should remain MAINTENANCE", "MAINTENANCE", mgr.getDevices().get("D-7").getStatus());
    }

    static void testUpdateStatus_NonExistentDevice() {
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(new HashMap<>());
        boolean result = mgr.updateStatus("D-99", "ACTIVE");
        assertEqual("Non-existent device should return false", false, result);
    }

    static void testUpdateStatus_InvalidStatusValue() {
        Map<String, Device> devices = new HashMap<>();
        devices.put("D-8", new Device("D-8", "SENSOR", 1, 0, 50, "REGISTERED"));
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(devices);

        boolean result = mgr.updateStatus("D-8", "DESTROYED");
        assertEqual("Invalid status value should return false", false, result);
    }

    // ═══════════════════════════════════════════════════════════════════
    //  PHASE 2 TESTS — addDevice
    // ═══════════════════════════════════════════════════════════════════

    static void testAddDevice_Success() {
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(new HashMap<>());
        Device device = new Device("D-10", "SENSOR", 5, 0, 80, "REGISTERED");

        boolean result = mgr.addDevice(device);
        assertEqual("addDevice with valid device should return true", true, result);
        assertEqual("Device should exist in map after add", true, mgr.getDevices().containsKey("D-10"));
    }

    static void testAddDevice_DuplicateId() {
        Map<String, Device> devices = new HashMap<>();
        devices.put("D-10", new Device("D-10", "SENSOR", 5, 0, 80, "REGISTERED"));
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(devices);

        Device duplicate = new Device("D-10", "CAMERA", 3, 0, 90, "ACTIVE");
        boolean result = mgr.addDevice(duplicate);
        assertEqual("addDevice with duplicate ID should return false", false, result);
        // Original device should be unchanged
        assertEqual("Original device type should remain SENSOR", "SENSOR", mgr.getDevices().get("D-10").getType());
    }

    static void testAddDevice_InvalidStatus() {
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(new HashMap<>());
        Device device = new Device("D-11", "SENSOR", 5, 0, 80, "DESTROYED");

        boolean result = mgr.addDevice(device);
        assertEqual("addDevice with invalid status should return false", false, result);
        assertEqual("Device should NOT be in map", false, mgr.getDevices().containsKey("D-11"));
    }

    static void testAddDevice_NegativeRegisteredDay() {
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(new HashMap<>());
        Device device = new Device("D-12", "SENSOR", -1, 0, 80, "REGISTERED");

        boolean result = mgr.addDevice(device);
        assertEqual("addDevice with negative registeredDay should return false", false, result);
        assertEqual("Device should NOT be in map", false, mgr.getDevices().containsKey("D-12"));
    }

    static void testAddDevice_ZeroRegisteredDay_ShouldPass() {
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(new HashMap<>());
        Device device = new Device("D-13", "CAMERA", 0, 0, 90, "ACTIVE");

        boolean result = mgr.addDevice(device);
        assertEqual("addDevice with registeredDay=0 should return true", true, result);
    }

    // ═══════════════════════════════════════════════════════════════════
    //  PHASE 3 TESTS — getActiveDeviceCountByType
    // ═══════════════════════════════════════════════════════════════════

    static void testGetActive_CountsByType() {
        Map<String, Device> devices = new HashMap<>();
        devices.put("D-1", new Device("D-1", "SENSOR",     5, 0, 80, "ACTIVE"));
        devices.put("D-2", new Device("D-2", "SENSOR",     3, 0, 90, "REGISTERED"));
        devices.put("D-3", new Device("D-3", "CAMERA",     7, 2, 70, "PENDING"));
        devices.put("D-4", new Device("D-4", "CAMERA",     1, 0, 60, "ACTIVE"));
        devices.put("D-5", new Device("D-5", "THERMOSTAT", 4, 1, 55, "ACTIVE"));
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(devices);

        Map<String, Integer> result = mgr.getActiveDeviceCountByType();
        assertEqual("SENSOR count should be 2", Integer.valueOf(2), result.get("SENSOR"));
        assertEqual("CAMERA count should be 2", Integer.valueOf(2), result.get("CAMERA"));
        assertEqual("THERMOSTAT count should be 1", Integer.valueOf(1), result.get("THERMOSTAT"));
    }

    static void testGetActive_ExcludesRetired() {
        Map<String, Device> devices = new HashMap<>();
        devices.put("D-1", new Device("D-1", "SENSOR", 5, 0, 80, "ACTIVE"));
        devices.put("D-2", new Device("D-2", "SENSOR", 3, 0, 90, "RETIRED"));
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(devices);

        Map<String, Integer> result = mgr.getActiveDeviceCountByType();
        assertEqual("SENSOR count should be 1 (RETIRED excluded)", Integer.valueOf(1), result.get("SENSOR"));
    }

    static void testGetActive_ExcludesMaintenance() {
        Map<String, Device> devices = new HashMap<>();
        devices.put("D-1", new Device("D-1", "THERMOSTAT", 5, 0, 80, "ACTIVE"));
        devices.put("D-2", new Device("D-2", "THERMOSTAT", 3, 0, 90, "MAINTENANCE"));
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(devices);

        Map<String, Integer> result = mgr.getActiveDeviceCountByType();
        assertEqual("THERMOSTAT count should be 1 (MAINTENANCE excluded)", Integer.valueOf(1), result.get("THERMOSTAT"));
    }

    static void testGetActive_ExcludesNegativeRegisteredDay() {
        Map<String, Device> devices = new HashMap<>();
        devices.put("D-1", new Device("D-1", "GATEWAY", 5,  0, 80, "ACTIVE"));
        devices.put("D-2", new Device("D-2", "GATEWAY", -1, 0, 90, "ACTIVE"));
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(devices);

        Map<String, Integer> result = mgr.getActiveDeviceCountByType();
        assertEqual("GATEWAY count should be 1 (negative registeredDay excluded)", Integer.valueOf(1), result.get("GATEWAY"));
    }

    static void testGetActive_IncludesRegisteredPendingActive() {
        Map<String, Device> devices = new HashMap<>();
        devices.put("D-1", new Device("D-1", "SENSOR", 1, 0, 80, "REGISTERED"));
        devices.put("D-2", new Device("D-2", "SENSOR", 2, 0, 70, "PENDING"));
        devices.put("D-3", new Device("D-3", "SENSOR", 3, 0, 60, "ACTIVE"));
        devices.put("D-4", new Device("D-4", "SENSOR", 4, 0, 50, "MAINTENANCE"));
        devices.put("D-5", new Device("D-5", "SENSOR", 5, 0, 40, "RETIRED"));
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(devices);

        Map<String, Integer> result = mgr.getActiveDeviceCountByType();
        assertEqual("SENSOR count should be 3 (REGISTERED+PENDING+ACTIVE only)", Integer.valueOf(3), result.get("SENSOR"));
    }

    static void testGetActive_EmptyDevices() {
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(new HashMap<>());
        Map<String, Integer> result = mgr.getActiveDeviceCountByType();
        assertEqual("Empty devices should return empty map", true, result.isEmpty());
    }

    // ═══════════════════════════════════════════════════════════════════
    //  PHASE 4 TESTS — removeDevice
    // ═══════════════════════════════════════════════════════════════════

    static void testRemoveDevice_RetiredDevice_ShouldSucceed() {
        Map<String, Device> devices = new HashMap<>();
        devices.put("D-1", new Device("D-1", "SENSOR", 5, 0, 80, "RETIRED"));
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(devices);

        boolean result = mgr.removeDevice("D-1");
        assertEqual("removeDevice on RETIRED should return true", true, result);
        assertEqual("Device D-1 should be removed from map", false, mgr.getDevices().containsKey("D-1"));
    }

    static void testRemoveDevice_ActiveDevice_ShouldFail() {
        Map<String, Device> devices = new HashMap<>();
        devices.put("D-2", new Device("D-2", "CAMERA", 5, 0, 80, "ACTIVE"));
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(devices);

        boolean result = mgr.removeDevice("D-2");
        assertEqual("removeDevice on ACTIVE should return false", false, result);
        assertEqual("Device D-2 should remain in map", true, mgr.getDevices().containsKey("D-2"));
    }

    static void testRemoveDevice_RegisteredDevice_ShouldFail() {
        Map<String, Device> devices = new HashMap<>();
        devices.put("D-3", new Device("D-3", "THERMOSTAT", 5, 0, 80, "REGISTERED"));
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(devices);

        boolean result = mgr.removeDevice("D-3");
        assertEqual("removeDevice on REGISTERED should return false", false, result);
        assertEqual("Device D-3 should remain in map", true, mgr.getDevices().containsKey("D-3"));
    }

    static void testRemoveDevice_MaintenanceDevice_ShouldFail() {
        Map<String, Device> devices = new HashMap<>();
        devices.put("D-4", new Device("D-4", "ACTUATOR", 5, 0, 80, "MAINTENANCE"));
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(devices);

        boolean result = mgr.removeDevice("D-4");
        assertEqual("removeDevice on MAINTENANCE should return false", false, result);
        assertEqual("Device D-4 should remain in map", true, mgr.getDevices().containsKey("D-4"));
    }

    static void testRemoveDevice_NonExistentDevice_ShouldFail() {
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(new HashMap<>());
        boolean result = mgr.removeDevice("D-99");
        assertEqual("removeDevice on non-existent device should return false", false, result);
    }

    // ═══════════════════════════════════════════════════════════════════
    //  PHASE 5 TESTS — getDevicesDueForMaintenance
    // ═══════════════════════════════════════════════════════════════════

    static void testDueForMaintenance_BasicOverdueSortedDesc() {
        Map<String, Device> devices = new HashMap<>();
        // currentDay = 10, threshold = 4
        // D-1: diff = 10 - 2 = 8 (> 4) -> overdue
        devices.put("D-1", new Device("D-1", "SENSOR", 1, 2, 80, "ACTIVE"));
        // D-2: diff = 10 - 7 = 3 (not > 4) -> not overdue
        devices.put("D-2", new Device("D-2", "CAMERA", 1, 7, 70, "ACTIVE"));
        // D-3: diff = 10 - 5 = 5 (> 4) -> overdue
        devices.put("D-3", new Device("D-3", "THERMOSTAT", 1, 5, 60, "ACTIVE"));
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(devices);

        List<String> result = mgr.getDevicesDueForMaintenance(10, 4);
        // Expected order: D-1 (overdue by 8), D-3 (overdue by 5)
        assertEqual("Should return 2 overdue devices", 2, result.size());
        assertEqual("First should be D-1 (most overdue)", "D-1", result.get(0));
        assertEqual("Second should be D-3", "D-3", result.get(1));
    }

    static void testDueForMaintenance_ExcludesRetiredAndMaintenance() {
        Map<String, Device> devices = new HashMap<>();
        // D-1: RETIRED, diff = 10 - 0 = 10 (> 4) -> but RETIRED is terminal!
        devices.put("D-1", new Device("D-1", "SENSOR", 1, 0, 80, "RETIRED"));
        // D-2: MAINTENANCE, diff = 10 - 0 = 10 (> 4) -> but MAINTENANCE is terminal!
        devices.put("D-2", new Device("D-2", "CAMERA", 1, 0, 70, "MAINTENANCE"));
        // D-3: ACTIVE, diff = 10 - 4 = 6 (> 4) -> active!
        devices.put("D-3", new Device("D-3", "ACTUATOR", 1, 4, 60, "ACTIVE"));
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(devices);

        List<String> result = mgr.getDevicesDueForMaintenance(10, 4);
        assertEqual("Should only include ACTIVE device D-3", List.of("D-3"), result);
    }

    static void testDueForMaintenance_ExcludesNegativeRegisteredDay() {
        Map<String, Device> devices = new HashMap<>();
        // D-1: registeredDay = -1 -> invalid, must be excluded
        devices.put("D-1", new Device("D-1", "GATEWAY", -1, 0, 80, "ACTIVE"));
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(devices);

        List<String> result = mgr.getDevicesDueForMaintenance(10, 4);
        assertEqual("Should exclude devices with registeredDay < 0", 0, result.size());
    }

    static void testDueForMaintenance_ExactlyAtThreshold_Excluded() {
        Map<String, Device> devices = new HashMap<>();
        // diff = 10 - 6 = 4. Since threshold is 4 and rule is diff > threshold, 4 is NOT > 4
        devices.put("D-1", new Device("D-1", "SENSOR", 1, 6, 80, "ACTIVE"));
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(devices);

        List<String> result = mgr.getDevicesDueForMaintenance(10, 4);
        assertEqual("Device exactly at threshold (diff == threshold) should NOT be due", 0, result.size());
    }

    static void testDueForMaintenance_EmptyWhenNoneOverdue() {
        Map<String, Device> devices = new HashMap<>();
        devices.put("D-1", new Device("D-1", "SENSOR", 1, 9, 80, "ACTIVE"));
        DeviceLifecycleManager mgr = new DeviceLifecycleManager(devices);

        List<String> result = mgr.getDevicesDueForMaintenance(10, 4);
        assertEqual("Empty list when no devices exceed threshold", 0, result.size());
    }

    // ═══════════════════════════════════════════════════════════════════
    //  HELPER
    // ═══════════════════════════════════════════════════════════════════

    static void assertEqual(String testName, Object expected, Object actual) {
        if (Objects.equals(expected, actual)) {
            System.out.println("  ✓ PASS: " + testName);
            passed++;
        } else {
            System.out.println("  ✗ FAIL: " + testName + "  |  Expected: " + expected + "  Got: " + actual);
            failed++;
        }
    }
}

