import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Role } from 'src/app/models/Role';
import { environment } from 'src/environments/environment';
import { User } from '../models/user';
import { RegisterModel } from 'src/app/models/registerModel';
import { LoginModel } from 'src/app/models/loginModel';
import { WebResponse } from 'src/app/models/webResponse';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient) {}

  apiUrl = `${environment.apiUrl}`;

  getUsers$(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/user`);
  }

  getUser$(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/user/` + id);
  }

  registerUser$(user: RegisterModel) {
    return this.http.post<WebResponse>(`${this.apiUrl}/auth/register`, user);
  }

  loginUser$(user: LoginModel): Observable<WebResponse> {
    return this.http.post<WebResponse>(`${this.apiUrl}/auth/login`, user);
  }

  getRoles$(): Observable<Role[]> {
    return this.http.get<Role[]>(`${this.apiUrl}/roles`);
  }
}
