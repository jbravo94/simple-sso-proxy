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
import { Injectable } from '@angular/core';
import { COOKIE_NAME } from 'src/app/globals/globalConstants';

@Injectable({
  providedIn: 'root'
})
export class LocalTokenService {

  constructor() { }

  private setCookie(name: string, value: string, expireDays: number, domain: string = '', path: string = '') {
    let d: Date = new Date();
    d.setTime(d.getTime() + expireDays * 24 * 60 * 60 * 1000);
    let expires: string = `expires=${d.toUTCString()}`;
    let cpath: string = path ? `; path=${path}` : '';
    let cdomain: string = domain ? `; domain=${domain}` : '';
    document.cookie = `${name}=${value}; ${expires}${cpath}${cdomain}`;
  }

  setProxyCookie(value: string) {
    this.setCookie(COOKIE_NAME, value, 365, window.location.hostname, '/');
  }

  private getCookie(name: string) {
    let ca: Array<string> = document.cookie.split(';');
    let caLen: number = ca.length;
    let cookieName = `${name}=`;
    let c: string;

    for (let i: number = 0; i < caLen; i += 1) {
      c = ca[i].replace(/^\s+/g, '');
      if (c.indexOf(cookieName) == 0) {
        return c.substring(cookieName.length, c.length);
      }
    }
    return '';
  }

  getProxyCookie() {
    return this.getCookie(COOKIE_NAME);
  }

  private deleteCookie(name: string, domain: string, path: string) {
    this.setCookie(name, '', -1, domain, path);
  }

  deleteProxyCookie() {
    this.deleteCookie(COOKIE_NAME, window.location.hostname, '/');
  }

  private setData(key: string, data: object) {
    const jsonData = JSON.stringify(data);
    localStorage.setItem(key, jsonData);
  }

  setProxyData(data: object) {
    this.setData(COOKIE_NAME, data);
  }

  private getData(key: string) {
    return localStorage.getItem(key);
  }

  getProxyData() {
    const data = this.getData(COOKIE_NAME);

    if (data) {
      return JSON.parse(data);
    } else {
      return null;
    }
  }

  private removeData(key: string) {
    localStorage.removeItem(key);
  }

  removeProxyData() {
    this.removeData(COOKIE_NAME);
  }

}
