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

  dataSource = new TableDataSource([]);

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


class TableDataSource extends DataSource<App> {
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