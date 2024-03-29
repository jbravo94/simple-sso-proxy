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
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/authentication/auth.service';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {

  isSidebarOpen = false;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
  }

  getIsSidebarOpen() {
    return this.isSidebarOpen;
  }

  toggleSidebarState() {
    this.isSidebarOpen = !this.isSidebarOpen;
  }

  onSidebarClose() {
    this.isSidebarOpen = false;
  }

  isLoggedIn() {
    return this.authService.getLoggedIn();
  }

  isAdmin() {
    return this.authService.isAdmin();
  }

  logout() {
    this.isSidebarOpen = false;
    this.authService.logout();
  }

  onClickSettings() {
    this.router.navigate(['/settings']);
    this.isSidebarOpen = false;
  }
}
