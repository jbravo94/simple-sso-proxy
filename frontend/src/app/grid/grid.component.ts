import { Component, Input, OnInit } from '@angular/core';
import { Service } from 'src/app/grid/service';

@Component({
  selector: 'app-grid',
  templateUrl: './grid.component.html',
  styleUrls: ['./grid.component.css']
})
export class GridComponent implements OnInit {

  @Input()
  services: Service[] = [];

  constructor() { }

  ngOnInit(): void {
  }

}
