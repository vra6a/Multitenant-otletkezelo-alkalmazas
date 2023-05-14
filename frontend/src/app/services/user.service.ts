import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Role } from 'src/app/models/Role';
import { environment } from 'src/environments/environment';
import { User } from '../models/dto/userDto';
import { RegisterModel } from 'src/app/models/registerModel';
import { LoginModel } from 'src/app/models/loginModel';
import { WebResponse } from 'src/app/models/webResponse';
import { WebData } from '../models/webData';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient) {}

  apiUrl = `${environment.apiUrl}`;

  getUsers$(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/user`);
  }

  getUser$(id: number): Observable<WebResponse<User>> {
    return this.http.get<WebResponse<User>>(`${this.apiUrl}/user/` + id);
  }

  getUserByEmail$(email: string): Observable<WebResponse<User>> {
    return this.http.get<WebResponse<User>>(
      `${this.apiUrl}/user/email/` + email
    );
  }

  registerUser$(user: RegisterModel): Observable<WebResponse<WebData>> {
    return this.http.post<WebResponse<WebData>>(
      `${this.apiUrl}/auth/register`,
      user
    );
  }

  loginUser$(user: LoginModel): Observable<WebResponse<WebData>> {
    return this.http.post<WebResponse<WebData>>(
      `${this.apiUrl}/auth/login`,
      user
    );
  }

  getRoles$(): Observable<Role[]> {
    return this.http.get<Role[]>(`${this.apiUrl}/roles`);
  }
}
