import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/authentication/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  hide = true;
  username: string = "";
  password: string = "";

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
  }

  signIn() {
    this.authService.login(this.username, this.password);
    return false;
  }

}
