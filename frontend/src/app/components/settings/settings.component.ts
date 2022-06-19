import { Component, OnInit } from '@angular/core';
import { Service } from 'src/app/models/service';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  services: Service[] = [new Service('Manage Apps', 'Click here to add or remove apps', '/settings/apps')];

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
  }

  isLoggedIn() {
    return this.authService.getLoggedIn();
  }

}
