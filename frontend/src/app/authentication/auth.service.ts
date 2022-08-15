/*
The MIT License
Copyright © 2022 Johannes HEINZL

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { convertFromBase64, convertToBase64 } from 'src/app/globals/globalFunctions';
import { BACKEND_URL } from 'src/app/globals/globalConstants';
import { JwtToken } from 'src/app/token/jwtToken';
import { LocalTokenService } from 'src/app/token/local-token.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  redirectUrl: string = '';

  constructor(private localTokenService: LocalTokenService, private router: Router, private http: HttpClient) { }

  getLoggedIn() {
    return this.localTokenService.getProxyCookie() != null && this.localTokenService.getProxyCookie() != '';
  }

  private toggleDemoLogin() {
    if (!this.localTokenService.getProxyData()) {
      this.localTokenService.setProxyCookie(convertToBase64(JSON.stringify(new JwtToken('', '', 0, 0, ['ROLE_ADMIN']))));
    } else {
      this.localTokenService.deleteProxyCookie();
    }
  }

  login(username: string, password: string) {

    this.http.post<any>(BACKEND_URL + '/api/v1/auth/login', { username: username, password: password }, { observe: 'response' }).subscribe((response) => {
      const bearerToken = response.body.access_token;

      if (bearerToken) {
        this.localTokenService.setProxyCookie(bearerToken);
        if (this.redirectUrl) {
          this.router.navigate([this.redirectUrl]);
          this.redirectUrl = '';
        } else {
          this.router.navigate(['/home']);
        }
      }
    })
  }

  logout() {
    this.localTokenService.deleteProxyCookie();
    console.log("here");
    this.router.navigate(['/login']);
  }

  isAdmin() {
    const encodedData: string = this.localTokenService.getProxyCookie();

    if (!encodedData) {
      return false;
    }

    const data: string = convertFromBase64(encodedData.split('.')[1]);

    if (!data) {
      return false;
    }

    const token: JwtToken = JSON.parse(data);

    return token.roles && token.roles.indexOf('ROLE_ADMIN') !== -1;
  }
}
