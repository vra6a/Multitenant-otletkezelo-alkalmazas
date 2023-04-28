import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',
})
export class SnackBarService {
  constructor(private snackBar: MatSnackBar) {}

  ok(msg: string) {
    if (msg != '') {
      this.snackBar.open(msg, '', {
        duration: 2000,
        panelClass: ['ok-snackbar'],
      });
    }
  }

  error(msg: string) {
    this.snackBar.open(msg, '', {
      duration: 2000,
      panelClass: ['error-snackbar'],
    });
  }
}
