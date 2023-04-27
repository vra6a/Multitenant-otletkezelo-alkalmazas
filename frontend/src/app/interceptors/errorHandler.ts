import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable, catchError, throwError } from 'rxjs';
import { SnackBarService } from '../services/snackBar.service';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {
  constructor(private snackBar: SnackBarService) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        let errMsg = '';
        if (error.error instanceof ErrorEvent) {
          errMsg = error.error.message;
        } else {
          errMsg = error.error.message;
        }
        if (errMsg && errMsg !== '') {
          this.snackBar.error(errMsg);
        }
        return throwError(errMsg);
      })
    );
  }
}
