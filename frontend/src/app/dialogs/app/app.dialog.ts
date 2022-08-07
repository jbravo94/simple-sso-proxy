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