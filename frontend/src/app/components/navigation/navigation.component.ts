import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {

  isSidebarOpen = false;

  constructor() { }

  ngOnInit(): void {
  }

  toggleSidebarState() {
    this.isSidebarOpen = !this.isSidebarOpen;
  }

  onSidebarClose() {
    this.isSidebarOpen = false;
  }
}
