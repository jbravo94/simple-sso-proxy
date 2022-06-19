import { Injectable } from '@angular/core';
import { LocalTokenService } from '../local-token/local-token.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private localTokenService: LocalTokenService) { }

  getLoggedIn() {
    return this.localTokenService.getProxyData() != null;
  }

  toggleLoggedIn() {
    if (!this.localTokenService.getProxyData()) {
      this.localTokenService.setProxyData({ token: "token" });
    } else {
      this.localTokenService.removeProxyData();
    }
  }
}
