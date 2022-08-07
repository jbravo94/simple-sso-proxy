import { Component, OnInit } from '@angular/core';
import { Service } from 'src/app/grid/service';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  services: Service[] = [new Service('Manage Apps', 'Click here to add or remove apps', '/settings/apps')];

  constructor() { }

  ngOnInit(): void {
  }

}
