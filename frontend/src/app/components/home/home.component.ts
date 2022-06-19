import { Component, OnInit } from '@angular/core';
import { Service } from 'src/app/models/service';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  services: Service[] = [new Service('Reset Password', 'Click here to reset your password', '/home')];

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
  }

  isLoggedIn() {
    return this.authService.getLoggedIn();
  }

}
