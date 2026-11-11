/**
 * IoT Device Entity
 * Represents a device registered in the IoT platform.
 *
 * Karat / Citi Bank Interview Exercise
 */
public class Device {

    private String id;             // Unique ID, e.g., "D-1", "D-2"
    private String type;           // From VALID_TYPES: SENSOR, THERMOSTAT, CAMERA, GATEWAY, ACTUATOR
    private int registeredDay;     // Day the device was registered (must be >= 0 for valid devices)
    private int maintenanceDay;    // Day of last maintenance
    private int batteryLevel;      // Battery percentage (0-100)
    private String status;         // From VALID_STATUSES: REGISTERED, PENDING, ACTIVE, MAINTENANCE, RETIRED

    public Device(String id, String type, int registeredDay, int maintenanceDay, int batteryLevel, String status) {
        this.id = id;
        this.type = type;
        this.registeredDay = registeredDay;
        this.maintenanceDay = maintenanceDay;
        this.batteryLevel = batteryLevel;
        this.status = status;
    }

    // ---- Getters ----
    public String getId() { return id; }
    public String getType() { return type; }
    public int getRegisteredDay() { return registeredDay; }
    public int getMaintenanceDay() { return maintenanceDay; }
    public int getBatteryLevel() { return batteryLevel; }
    public String getStatus() { return status; }

    // ---- Setters ----
    public void setId(String id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setRegisteredDay(int registeredDay) { this.registeredDay = registeredDay; }
    public void setMaintenanceDay(int maintenanceDay) { this.maintenanceDay = maintenanceDay; }
    public void setBatteryLevel(int batteryLevel) { this.batteryLevel = batteryLevel; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Device{id='" + id + "', type='" + type + "', registeredDay=" + registeredDay +
               ", maintenanceDay=" + maintenanceDay + ", batteryLevel=" + batteryLevel +
               ", status='" + status + "'}";
    }
}

