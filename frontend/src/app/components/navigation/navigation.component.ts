import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth/auth.service';

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
