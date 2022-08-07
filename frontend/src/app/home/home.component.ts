import { Component, OnInit } from '@angular/core';
import { App } from 'src/app/apps/app';
import { Service } from 'src/app/grid/service';
import { AppsService } from 'src/app/apps/apps.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  services: Service[] = [new Service('Reset Password', 'Click here to reset your password.', '/home')];

  constructor(private appsService: AppsService) { }

  ngOnInit(): void {
    this.appsService.getApps().subscribe((apps: App[]) => {
      apps.forEach((app: App) => this.services.push(new Service(app.name, 'Click here to access this app.', app.proxyUrl, false)));
    });
  }

}
