import { Component, computed, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Device, DeviceStatus } from '../../models/device.model';

@Component({
  selector: 'app-device-monitor',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './device-monitor.component.html',
  styleUrl: './device-monitor.component.css'
})
export class DeviceMonitorComponent {

  // --- Initial Mock Data ---
  private initialDevices: Device[] = [
    { id: 'D-101', name: 'Server Room Temp Sensor', type: 'SENSOR', status: 'ACTIVE', batteryLevel: 85, registeredDay: 12 },
    { id: 'D-102', name: 'Main Gate Security Camera', type: 'CAMERA', status: 'ACTIVE', batteryLevel: 15, registeredDay: 45 },
    { id: 'D-103', name: 'HVAC Floor 2 Thermostat', type: 'THERMOSTAT', status: 'MAINTENANCE', batteryLevel: 60, registeredDay: 5 },
    { id: 'D-104', name: 'Backup Gateway Hub', type: 'GATEWAY', status: 'PENDING', batteryLevel: 95, registeredDay: 1 },
    { id: 'D-105', name: 'Basement Water Leak Sensor', type: 'SENSOR', status: 'ACTIVE', batteryLevel: 10, registeredDay: 80 },
    { id: 'D-106', name: 'Old Storage Cam', type: 'CAMERA', status: 'RETIRED', batteryLevel: 0, registeredDay: 200 }
  ];

  // =========================================================================
  // 1. SIGNALS (Angular 17 Core Reactive State)
  // =========================================================================

  // Writable signal holding the list of all devices
  devices = signal<Device[]>(this.initialDevices);

  // Writable signal for current filter: 'ALL' | 'ACTIVE' | 'MAINTENANCE' | 'PENDING' | 'RETIRED'
  selectedFilter = signal<string>('ALL');

  // Writable signal for search term
  searchTerm = signal<string>('');

  // =========================================================================
  // 2. COMPUTED SIGNALS (Derived State — Auto updates when dependencies change)
  // =========================================================================

  /**
   * TODO #1: Implement filteredDevices
   *
   * Return a filtered list of devices based on:
   * 1. selectedFilter(): if not 'ALL', device.status must match selectedFilter()
   * 2. searchTerm(): if not empty, device.name OR device.id must contain the search term (case-insensitive)
   */
  filteredDevices = computed(() => {
    const filter = this.selectedFilter();
    const query = this.searchTerm().trim().toLowerCase();
    const all = this.devices();

    return all.filter(device => {
      const matchesFilter = filter === 'ALL' || device.status === filter;
      const matchesQuery = !query ||
        device.name.toLowerCase().includes(query) ||
        device.id.toLowerCase().includes(query);

      return matchesFilter && matchesQuery;
    });
  });

  /**
   * TODO #2: Implement activeCount
   * Returns total count of devices where status === 'ACTIVE'
   */
  activeCount = computed(() => {
    return this.devices().filter(d => d.status === 'ACTIVE').length;
  });

  /**
   * TODO #3: Implement lowBatteryCount
   * Returns count of devices that are 'ACTIVE' AND have batteryLevel < 20
   */
  lowBatteryCount = computed(() => {
    return this.devices().filter(d => d.status === 'ACTIVE' && d.batteryLevel < 20).length;
  });

  // =========================================================================
  // 3. MUTATING SIGNALS (.update() & .set())
  // =========================================================================

  /**
   * Updates filter state when user clicks a filter tab
   */
  setFilter(status: string): void {
    this.selectedFilter.set(status);
  }

  /**
   * Updates search term from input field
   */
  onSearchChange(term: string): void {
    this.searchTerm.set(term);
  }

  /**
   * TODO #4: Toggle device status between 'ACTIVE' and 'MAINTENANCE'
   * Use this.devices.update(...) to immutably update the device list.
   */
  toggleMaintenance(id: string): void {
    this.devices.update(list =>
      list.map(device => {
        if (device.id === id) {
          const nextStatus: DeviceStatus = device.status === 'ACTIVE' ? 'MAINTENANCE' : 'ACTIVE';
          return { ...device, status: nextStatus };
        }
        return device;
      })
    );
  }

  /**
   * TODO #5: Mark a device as 'RETIRED'
   * Use this.devices.update(...)
   */
  retireDevice(id: string): void {
    this.devices.update(list =>
      list.map(device =>
        device.id === id ? { ...device, status: 'RETIRED' } : device
      )
    );
  }

  /**
   * Resets devices back to initial state
   */
  resetDevices(): void {
    this.devices.set(this.initialDevices);
    this.selectedFilter.set('ALL');
    this.searchTerm.set('');
  }
}

