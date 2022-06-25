import { Component, OnInit } from '@angular/core';
import { App } from 'src/app/models/app';
import { Service } from 'src/app/models/service';
import { AppsService } from 'src/app/services/apps/apps.service';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  services: Service[] = [new Service('Reset Password', 'Click here to reset your password.', '/home')];

  constructor(private authService: AuthService, private appsService: AppsService) { }

  ngOnInit(): void {
    this.appsService.getApps().subscribe((apps: App[]) => {
      apps.forEach((app: App) => this.services.unshift(new Service(app.name, 'Click here to access this app.', app.baseUrl)));
    });
  }

  isLoggedIn() {
    return this.authService.getLoggedIn();
  }

}
