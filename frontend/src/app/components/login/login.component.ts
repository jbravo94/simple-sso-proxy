import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';

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
    alert('Sign in triggered!');
    this.authService.login(this.username, this.password);
    return false;
  }

}
