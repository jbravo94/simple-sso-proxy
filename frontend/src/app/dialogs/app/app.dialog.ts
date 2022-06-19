import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { App } from 'src/app/models/app';

@Component({
  selector: 'app-dialog',
  templateUrl: 'app.dialog.html',
  styleUrls: ['./app.dialog.css']
})
export class AppDialog {
  constructor(
    public dialogRef: MatDialogRef<AppDialog>,
    @Inject(MAT_DIALOG_DATA) public data: App,
  ) { }

  onNoClick(): void {
    this.dialogRef.close();
  }
}