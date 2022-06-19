import { Injectable } from '@angular/core';
import { COOKIE_NAME } from 'src/app/globals';

@Injectable({
  providedIn: 'root'
})
export class LocalTokenService {

  constructor() { }

  private setCookie(name: string, value: string, expireDays: number, path: string = '') {
    let d: Date = new Date();
    d.setTime(d.getTime() + expireDays * 24 * 60 * 60 * 1000);
    let expires: string = `expires=${d.toUTCString()}`;
    let cpath: string = path ? `; path=${path}` : '';
    document.cookie = `${name}=${value}; ${expires}${cpath}`;
  }

  setProxyCookie(value: string, expireDays: number, path: string = '') {
    this.setCookie(COOKIE_NAME, value, expireDays, path);
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

  private deleteCookie(name: string) {
    this.setCookie(name, '', -1);
  }

  deleteProxyCookie() {
    this.deleteCookie(COOKIE_NAME);
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
    return this.getData(COOKIE_NAME);
  }

  private removeData(key: string) {
    localStorage.removeItem(key);
  }

  removeProxyData() {
    this.removeData(COOKIE_NAME);
  }

}
