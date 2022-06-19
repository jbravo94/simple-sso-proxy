import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { JwtToken } from 'src/app/models/jwtToken';
import { LocalTokenService } from '../local-token/local-token.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  redirectUrl: string = '';

  constructor(private localTokenService: LocalTokenService, private router: Router) { }

  getLoggedIn() {
    return this.localTokenService.getProxyData() != null;
  }

  private toggleDemoLogin() {
    if (!this.localTokenService.getProxyData()) {
      this.localTokenService.setProxyData(new JwtToken('', '', 0, 0, ['admin']));
    } else {
      this.localTokenService.removeProxyData();
    }
  }

  login(username: string, password: string) {
    this.toggleDemoLogin();
    if (this.redirectUrl) {
      this.router.navigate([this.redirectUrl]);
      this.redirectUrl = '';
    } else {
      this.router.navigate(['/home']);
    }
  }

  logout() {
    this.toggleDemoLogin();
    this.router.navigate(['/login']);
  }

  isAdmin() {
    const token: JwtToken = this.localTokenService.getProxyData();

    return token.roles && token.roles.indexOf('admin') !== -1;
  }
}
