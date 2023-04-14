import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Role } from 'src/app/models/Role';
import { environment } from 'src/environments/environment';
import { User } from '../../models/user';
import { UserListView } from 'src/app/models/userListView';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient) {}

  apiUrl = `${environment.apiUrl}`;

  getUsers$(): Observable<User[]> {
    return this.http.get<UserListView[]>(`${this.apiUrl}/users`);
  }

  getUser$(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/user/` + id);
  }

  createUser$(user: User) {
    console.log(user);
    return this.http.post<User>(`${this.apiUrl}/user`, user);
  }

  getRoles$(): Observable<Role[]> {
    return this.http.get<Role[]>(`${this.apiUrl}/roles`);
  }

  loginUser(user: User) {}
}
