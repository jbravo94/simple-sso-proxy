import { Component, OnInit } from '@angular/core';
import { App } from 'src/app/apps/app';
import { AppsService } from 'src/app/apps/apps.service';
import { GridElement } from '../../grid/grid-element';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  services: GridElement[] = [new GridElement('Reset Password', 'Click here to reset your password.', '/home')];

  constructor(private appsService: AppsService) { }

  ngOnInit(): void {
    this.appsService.getApps().subscribe((apps: App[]) => {
      apps.forEach((app: App) => this.services.push(new GridElement(app.name, 'Click here to access this app.', app.proxyUrl, false)));
    });
  }

}
