import { Component, OnInit } from '@angular/core';
import { DataSource } from '@angular/cdk/collections';
import { Observable, ReplaySubject } from 'rxjs';
import { App } from 'src/app/models/app';
import { AppsService } from 'src/app/services/apps/apps.service';

@Component({
  selector: 'app-apps',
  templateUrl: './apps.component.html',
  styleUrls: ['./apps.component.css']
})
export class AppsComponent implements OnInit {

  displayedColumns: string[] = ['position', 'name', 'weight', 'symbol', 'options'];

  dataSource = new ExampleDataSource([]);

  constructor(private appsService: AppsService) {
    this.dataSource.setData(this.appsService.getApps());
  }

  ngOnInit(): void {
  }

  addApp() {

  }
}


class ExampleDataSource extends DataSource<App> {
  private _dataStream = new ReplaySubject<App[]>();

  constructor(initialData: App[]) {
    super();
    this.setData(initialData);
  }

  connect(): Observable<App[]> {
    return this._dataStream;
  }

  disconnect() { }

  setData(data: App[]) {
    this._dataStream.next(data);
  }
}