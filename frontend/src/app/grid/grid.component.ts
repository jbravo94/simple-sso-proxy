import { Component, Input, OnInit } from '@angular/core';
import { GridElement } from './grid-element';

@Component({
  selector: 'app-grid',
  templateUrl: './grid.component.html',
  styleUrls: ['./grid.component.css']
})
export class GridComponent implements OnInit {

  @Input()
  services: GridElement[] = [];

  constructor() { }

  ngOnInit(): void {
  }

}
