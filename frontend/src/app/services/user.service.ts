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
import { UserSlimDto } from '../models/slimDto/userSlimDto';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient) {}

  apiUrl = `${environment.apiUrl}`;

  getUsers$(): Observable<WebResponse<UserSlimDto[]>> {
    return this.http.get<WebResponse<UserSlimDto[]>>(`${this.apiUrl}/user`);
  }

  getJuries$(): Observable<WebResponse<UserSlimDto[]>> {
    return this.http.get<WebResponse<UserSlimDto[]>>(
      `${this.apiUrl}/user/juries`
    );
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

  editUserPermission$(
    id: number,
    role: string
  ): Observable<WebResponse<UserSlimDto>> {
    return this.http.post<WebResponse<UserSlimDto>>(
      `${this.apiUrl}/user/${id}/permission`,
      role
    );
  }

  deleteUser$(id: number): Observable<WebResponse<string>> {
    return this.http.delete<WebResponse<string>>(`${this.apiUrl}/user/${id}`);
  }
}
