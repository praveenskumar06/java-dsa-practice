export type DeviceStatus = 'ACTIVE' | 'PENDING' | 'MAINTENANCE' | 'RETIRED';
export type DeviceType = 'SENSOR' | 'THERMOSTAT' | 'CAMERA' | 'GATEWAY';

export interface Device {
  id: string;
  name: string;
  type: DeviceType;
  status: DeviceStatus;
  batteryLevel: number; // 0 - 100
  registeredDay: number;
}

