import {
  HttpEvent,
  HttpHandler,
  HttpHeaders,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from '../services/auth/auth.service';
import { Observable } from 'rxjs';
import { TenantService } from '../services/multitenancy/tenantService';

@Injectable()
export class HeaderInterceptor implements HttpInterceptor {
  constructor(private auth: AuthService, private tenantService: TenantService) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    let tenant = this.tenantService.getTenantId()
    let token = window.sessionStorage.getItem('token');
    let headers = new HttpHeaders({
      Authorization: 'Bearer ' + token,
      'Content-Type': 'application/json',
      'X-Tenant-Id': tenant
    });
    if (
      request.headers.get('Content-Type')?.toString() !==
      'application/x-www-form-urlencoded'
    ) {
      if (token) {
        request = request.clone({
          headers: headers,
        });
      }
    }
    return next.handle(request);
  }
}
