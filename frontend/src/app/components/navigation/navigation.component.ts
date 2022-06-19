import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {

  isSidebarOpen = false;

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
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

  logout() {
    this.isSidebarOpen = false;
    this.authService.logout();
  }
}
