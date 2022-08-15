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
import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { App } from 'src/app/apps/app';
import { AppsService } from 'src/app/apps/apps.service';

export enum DialogType {
  CREATE = "Create",
  UPDATE = "Update"
}

@Component({
  selector: 'app-dialog',
  templateUrl: 'app.dialog.html',
  styleUrls: ['./app.dialog.css']
})
export class AppDialog {

  app: App;
  type: DialogType;

  constructor(
    public dialogRef: MatDialogRef<AppDialog>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private appsService: AppsService
  ) {
    this.app = data.app || {};
    this.type = data.type || DialogType.CREATE;
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onClick(): void {
    if (this.type === DialogType.CREATE) {
      this.appsService.postApp(this.app).subscribe(() => this.dialogRef.close())
    } else if (this.type === DialogType.UPDATE) {
      this.appsService.editApp(this.app).subscribe(() => this.dialogRef.close())
    }
  }

}