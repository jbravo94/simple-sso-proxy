import { Component, OnInit } from '@angular/core';
import { App } from 'src/app/apps/app';
import { Service } from 'src/app/models/service';
import { AppsService } from 'src/app/apps/apps.service';
import { AuthService } from 'src/app/authentication/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  apps: Service[] = [];
  services: Service[] = [new Service('Reset Password', 'Click here to reset your password.', '/home')];

  constructor(private authService: AuthService, private appsService: AppsService) { }

  ngOnInit(): void {
    this.appsService.getApps().subscribe((apps: App[]) => {
      apps.forEach((app: App) => this.apps.push(new Service(app.name, 'Click here to access this app.', app.proxyUrl)));
    });
  }

  isLoggedIn() {
    return this.authService.getLoggedIn();
  }

}
