import { Component, OnInit } from '@angular/core';
import { GridElement } from '../../grid/grid-element';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  services: GridElement[] = [new GridElement('Manage Apps', 'Click here to add or remove apps', '/settings/apps')];

  constructor() { }

  ngOnInit(): void {
  }

}
