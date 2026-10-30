import java.util.*;
import java.util.stream.*;

/**
 * DeviceLifecycleManager — Manages IoT device registration, status transitions,
 * and querying.
 *
 * Karat / Citi Bank Interview Exercise
 *
 * ==========================================================================
 * PHASE 1 — BUG FIX
 * The updateStatus() method has a bug. Some tests are failing because
 * an invalid status transition is being allowed. Find and fix the bug.
 *
 * PHASE 2 — IMPLEMENT addDevice()
 * Add a new device to the system with proper validation.
 *
 * PHASE 3 — IMPLEMENT getActiveDeviceCountByType()
 * Return a count of "active" devices grouped by type.
 * ==========================================================================
 */
public class DeviceLifecycleManager {

    // ---- Static Constants ----

    public static final Set<String> VALID_TYPES = Set.of(
            "SENSOR", "THERMOSTAT", "CAMERA", "GATEWAY", "ACTUATOR");

    public static final Set<String> VALID_STATUSES = Set.of(
            "REGISTERED", "PENDING", "ACTIVE", "MAINTENANCE", "RETIRED");

    /**
     * Valid status transitions map.
     * Key = current status, Value = set of statuses it can transition TO.
     *
     * If a status is NOT a key in this map, it is a terminal state (no transitions
     * allowed).
     * Terminal states: MAINTENANCE, RETIRED
     *
     * "Active" devices = devices whose current status IS a key in this map
     * (i.e., REGISTERED, PENDING, ACTIVE)
     */
    public static final Map<String, Set<String>> VALID_STATUS_TRANSITIONS = Map.of(
            "REGISTERED", Set.of("ACTIVE"),
            "PENDING", Set.of("REGISTERED", "ACTIVE"),
            "ACTIVE", Set.of("PENDING", "MAINTENANCE", "RETIRED"));

    // ---- Instance Fields ----

    private Map<String, Device> devices;

    public DeviceLifecycleManager(Map<String, Device> devices) {
        this.devices = new HashMap<>(devices);
    }

    // ========================================================================
    // PHASE 1 — FIX THE BUG IN THIS METHOD
    // ========================================================================

    /**
     * Updates the status of an existing device.
     *
     * Rules:
     * - Device must exist in the map.
     * - New status must be a valid status.
     * - The transition from current status → new status must be allowed
     * by VALID_STATUS_TRANSITIONS.
     *
     * @return true if the status was updated successfully, false otherwise.
     */
    public boolean updateStatus(String deviceId, String newStatus) {
        Device device = devices.get(deviceId);
        if (device == null) {
            return false;
        }
        if (!VALID_STATUSES.contains(newStatus)) {
            return false;
        }
        // ⚠️ BUG: Missing validation — should check VALID_STATUS_TRANSITIONS
        // before allowing the status change!
        Set<String> allowedStatuses = VALID_STATUS_TRANSITIONS.get(device.getStatus());
        if (allowedStatuses == null || !allowedStatuses.contains(newStatus)) {
            return false;
        }
        device.setStatus(newStatus);
        return true;
    }

    // ========================================================================
    // PHASE 2 — IMPLEMENT THIS METHOD
    // ========================================================================

    /**
     * Adds a new device to the system.
     *
     * Conditions (return false if ANY fail):
     * - Device ID must NOT already exist in the devices map.
     * - Device status must be a valid status (in VALID_STATUSES).
     * - Device registeredDay must be >= 0.
     *
     * If all conditions pass, add the device to the map and return true.
     *
     * @return true if the device was added, false otherwise.
     */
    public boolean addDevice(Device device) {
        if (devices.keySet().contains(device.getId())) {
            return false;
        }
        if (!VALID_STATUSES.contains(device.getStatus())) {
            return false;
        }
        if (device.getRegisteredDay() < 0) {
            return false;
        }
        if (!VALID_TYPES.contains(device.getType())) {
            return false;
        }
        devices.put(device.getId(), device);
        return true;
    }

    // ========================================================================
    // PHASE 3 — IMPLEMENT THIS METHOD
    // ========================================================================

    /**
     * Returns a count of "active" devices grouped by device type.
     *
     * A device is considered "active" if:
     * 1. Its status is a KEY in VALID_STATUS_TRANSITIONS
     * (i.e., REGISTERED, PENDING, or ACTIVE — NOT MAINTENANCE or RETIRED)
     * 2. Its registeredDay >= 0
     *
     * @return Map where key = device type (e.g., "SENSOR"), value = count of active
     *         devices
     */
    public Map<String, Integer> getActiveDeviceCountByType() {
        // TODO: Implement this method
        Map<String, Integer> map = devices.values().stream()
                .filter(device -> device.getRegisteredDay() >= 0 &&
                        VALID_STATUS_TRANSITIONS.containsKey(device.getStatus()))
                .collect(Collectors.groupingBy(Device::getType, Collectors.summingInt(e -> 1)));
        return map;
    }

    // ========================================================================
    // PHASE 4 — IMPLEMENT THIS METHOD
    // ========================================================================

    /**
     * Removes a device from the system.
     *
     * Conditions:
     * - Device must exist in the devices map.
     * - Device must be in RETIRED status (only retired devices can be removed).
     *
     * If both conditions pass, remove the device and return true.
     * Otherwise return false.
     *
     * @return true if the device was removed, false otherwise.
     */
    public boolean removeDevice(String deviceId) {
        // TODO: Implement this method
        Device device = devices.get(deviceId);
        if (device == null || !device.getStatus().equals("RETIRED")) {
            return false;
        }
        devices.remove(deviceId);
        return true;
    }

    // ========================================================================
    // PHASE 5 — IMPLEMENT THIS METHOD
    // ========================================================================

    /**
     * Returns a list of device IDs that are overdue for maintenance.
     *
     * A device is "due for maintenance" if ALL of the following are true:
     * 1. Device is "active" (status is a KEY in VALID_STATUS_TRANSITIONS)
     * 2. Device registeredDay >= 0
     * 3. (currentDay - maintenanceDay) > threshold
     *
     * The result must be SORTED by overdue amount DESCENDING
     * (most overdue device first — highest (currentDay - maintenanceDay) first).
     *
     * @param currentDay the current day number
     * @param threshold  the maximum allowed days since last maintenance
     * @return sorted list of device IDs that are overdue
     */
    public List<String> getDevicesDueForMaintenance(int currentDay, int threshold) {
        // TODO: Implement this method
        List<String> asdf = devices.values().stream().filter(
                d -> d.getRegisteredDay() >= 0
                        && VALID_STATUS_TRANSITIONS.containsKey(d.getStatus())
                        && (currentDay - d.getMaintenanceDay()) > threshold)
                .sorted(Comparator.comparingInt(Device::getMaintenanceDay)).map(d -> d.getId())
                .collect(Collectors.toList());
        return asdf;
    }

    // ---- Accessor ----

    public Map<String, Device> getDevices() {
        return devices;
    }
}
