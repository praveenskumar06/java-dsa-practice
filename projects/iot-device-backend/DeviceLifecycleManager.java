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
        if (!isValidDevice(device)) {
            return false;
        }
        devices.put(device.getId(), device);
        return true;
    }

    public boolean isValidDevice(Device device) {
        if (device == null || device.getId() == null) {
            return false;
        }
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

    // ========================================================================
    // PHASE 6 — IMPLEMENT THIS METHOD
    // ========================================================================

    /**
     * Batch registers new devices into the system.
     *
     * Conditions:
     * - If newDevices is null or empty, return false.
     * - Every device must meet standard addDevice validation:
     * * Device must not be null
     * * Device ID must not be in devices map
     * * Device ID must not be duplicated within newDevices itself
     * * Device status must be in VALID_STATUSES
     * * Device registeredDay >= 0
     * * Device type must be in VALID_TYPES
     *
     * - If atomic == true:
     * * If ANY device fails validation, NONE are added. State is unchanged. Return
     * false.
     * * If ALL pass, all are added. Return true.
     *
     * - If atomic == false:
     * * Add all devices that pass validation; skip invalid ones.
     * * Return true if AT LEAST ONE device was added; false if 0 were added.
     *
     * @param newDevices list of devices to add
     * @param atomic     whether the operation is all-or-nothing
     * @return true if successful according to the atomic mode, false otherwise
     */
    public boolean batchAddDevices(List<Device> newDevices, boolean atomic) {

        if (newDevices == null || newDevices.isEmpty()) {
            return false;
        }

        if (atomic) {
            Map<String, Device> staged = new HashMap<>();
            for (Device d : newDevices) {
                if (isValidDevice(d) && !staged.keySet().contains(d.getId())) {
                    staged.put(d.getId(), d);
                } else {
                    return false;
                }
            }
            devices.putAll(staged);
            return true;
        } else {
            int addCount = 0;
            for (Device d : newDevices) {
                if (isValidDevice(d)) {
                    devices.put(d.getId(), d);
                    addCount++;
                }
            }
            return addCount > 0;
        }

    }

    // ========================================================================
    // PHASE 7 — IMPLEMENT THIS METHOD
    // ========================================================================

    /**
     * Returns the top N device IDs of a given type with the lowest battery levels.
     *
     * Filtering:
     * 1. type must be in VALID_TYPES, and n > 0 (otherwise return empty list)
     * 2. Device type must match requested type
     * 3. Device must be active (status is a KEY in VALID_STATUS_TRANSITIONS)
     * 4. Device registeredDay >= 0
     *
     * Sorting & Limiting:
     * 1. Sort by batteryLevel ASCENDING (lowest battery first)
     * 2. Tie-breaker: if battery levels are equal, sort by registeredDay ASCENDING
     * 3. Return at most n device IDs
     *
     * @param type device type to inspect
     * @param n    maximum number of device IDs to return
     * @return list of up to n device IDs meeting criteria
     */
    public List<String> getTopNDevicesByLowestBattery(String type, int n) {

        if (n <= 0 || !VALID_TYPES.contains(type))
            return Collections.emptyList();

        List<String> deviceList = devices.values().stream()
                .filter(d -> d.getRegisteredDay() >= 0 && type.equals(d.getType())
                        && VALID_STATUS_TRANSITIONS.containsKey(d.getStatus()))
                .sorted(Comparator.comparingInt(Device::getBatteryLevel)
                        .thenComparingInt(Device::getRegisteredDay))
                .limit(n)
                .map(Device::getId)
                .collect(Collectors.toList());

        return deviceList;
    }

    // ---- Accessor ----

    public Map<String, Device> getDevices() {
        return devices;
    }
}
