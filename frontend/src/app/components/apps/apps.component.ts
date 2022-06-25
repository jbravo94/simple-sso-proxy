import { Component, OnInit } from '@angular/core';
import { DataSource } from '@angular/cdk/collections';
import { Observable, ReplaySubject } from 'rxjs';
import { App } from 'src/app/models/app';
import { AppsService } from 'src/app/services/apps/apps.service';
import { MatDialog } from '@angular/material/dialog';
import { AppDialog } from 'src/app/dialogs/app/app.dialog';

@Component({
  selector: 'app-apps',
  templateUrl: './apps.component.html',
  styleUrls: ['./apps.component.css']
})
export class AppsComponent implements OnInit {

  displayedColumns: string[] = ['name', 'baseUrl', 'loginScript', 'logoutScript', 'resetScript', 'options'];

  dataSource = new ExampleDataSource([]);

  constructor(private appsService: AppsService, public dialog: MatDialog) {
    this.dataSource.setData(this.appsService.getApps());
  }

  ngOnInit(): void {
  }

  addApp(): void {
    const dialogRef = this.dialog.open(AppDialog, {
      width: '250px',
      data: {},
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
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