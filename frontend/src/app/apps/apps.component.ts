import { Component, OnInit } from '@angular/core';
import { DataSource } from '@angular/cdk/collections';
import { Observable, ReplaySubject } from 'rxjs';
import { App } from 'src/app/apps/app';
import { AppsService } from 'src/app/apps/apps.service';
import { MatDialog } from '@angular/material/dialog';
import { AppDialog, DialogType } from 'src/app/apps/app.dialog';

@Component({
  selector: 'app-apps',
  templateUrl: './apps.component.html',
  styleUrls: ['./apps.component.css']
})
export class AppsComponent implements OnInit {

  displayedColumns: string[] = ['name', 'baseUrl', 'proxyUrl', 'options'];

  dataSource = new ExampleDataSource([]);

  constructor(private appsService: AppsService, public dialog: MatDialog) {
    this.refresh();
  }

  refresh(): void {
    this.appsService.getApps().subscribe((apps: App[]) => this.dataSource.setData(apps));
  }

  ngOnInit(): void {
  }

  addApp(): void {
    const dialogRef = this.dialog.open(AppDialog, {
      width: '80%',
      data: {},
    });

    dialogRef.afterClosed().subscribe(result => {
      this.refresh();
    });
  }

  editApp(app: App): void {
    const dialogRef = this.dialog.open(AppDialog, {
      width: '80%',
      data: {
        app: app,
        type: DialogType.UPDATE
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      this.refresh();
    });
  }

  deleteApp(app: App): void {
    this.appsService.deleteApp(app).subscribe(() => this.refresh());
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