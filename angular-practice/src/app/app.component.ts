import { Component } from '@angular/core';
import { DeviceMonitorComponent } from './tasks/device-monitor/device-monitor.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [DeviceMonitorComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Angular 17 Modern Practice — Signals & Control Flow';
}
