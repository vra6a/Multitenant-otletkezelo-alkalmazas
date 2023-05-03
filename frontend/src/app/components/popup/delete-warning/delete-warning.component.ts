import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-delete-warning',
  templateUrl: './delete-warning.component.html',
  styleUrls: ['./delete-warning.component.scss'],
})
export class DeleteWarningComponent implements OnInit {
  constructor(public dialogRef: MatDialogRef<DeleteWarningComponent>) {}

  ngOnInit(): void {}
}
