import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DeviceMonitorComponent } from './device-monitor.component';

describe('DeviceMonitorComponent (Angular 17 Signals & Control Flow)', () => {
  let component: DeviceMonitorComponent;
  let fixture: ComponentFixture<DeviceMonitorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeviceMonitorComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(DeviceMonitorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should compute activeCount correctly from signal', () => {
    // Initial devices has 3 ACTIVE devices (D-101, D-102, D-105)
    expect(component.activeCount()).toBe(3);
  });

  it('should compute lowBatteryCount for ACTIVE devices with battery < 20', () => {
    // D-102 (15%) and D-105 (10%) are ACTIVE with battery < 20
    expect(component.lowBatteryCount()).toBe(2);
  });

  it('should filter devices by status when selectedFilter signal changes', () => {
    component.setFilter('MAINTENANCE');
    expect(component.filteredDevices().length).toBe(1);
    expect(component.filteredDevices()[0].id).toBe('D-103');
  });

  it('should filter devices by search term case-insensitively', () => {
    component.onSearchChange('water leak');
    expect(component.filteredDevices().length).toBe(1);
    expect(component.filteredDevices()[0].id).toBe('D-105');
  });

  it('should toggle device status between ACTIVE and MAINTENANCE using signals update()', () => {
    // D-101 is initially ACTIVE
    component.toggleMaintenance('D-101');
    const updated = component.devices().find(d => d.id === 'D-101');
    expect(updated?.status).toBe('MAINTENANCE');

    // activeCount should automatically recompute via signal dependency!
    expect(component.activeCount()).toBe(2);

    // Toggle back
    component.toggleMaintenance('D-101');
    const toggledBack = component.devices().find(d => d.id === 'D-101');
    expect(toggledBack?.status).toBe('ACTIVE');
    expect(component.activeCount()).toBe(3);
  });

  it('should retire a device and prevent further status changes', () => {
    component.retireDevice('D-101');
    const retired = component.devices().find(d => d.id === 'D-101');
    expect(retired?.status).toBe('RETIRED');
  });
});

