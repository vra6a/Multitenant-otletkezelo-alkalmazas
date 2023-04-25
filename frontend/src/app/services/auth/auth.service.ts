import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../../models/user';
import { Subject } from 'rxjs';
import { WebData } from 'src/app/models/webData';
import { Role } from 'src/app/models/Role';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient) {}

  currentUser: User = null;
  login = new Subject<User>();
  accessToken: string = '';

  setCurrentUser(data: WebData) {
    if (this.currentUser == null) {
      this.currentUser = {
        id: data.id,
        firstName: data.firstName,
        lastName: data.lastName,
        email: data.email,
        role: data.role as unknown as Role,
      };
      this.setAccessToken(data.token);
      this.login.next(this.currentUser);
    }
  }

  setAccessToken(token: string) {
    this.accessToken = token;
  }

  getCurrentUser(): User {
    return this.currentUser;
  }

  logOut() {
    if (this.currentUser) {
      this.currentUser = null;
      this.accessToken = '';
    }
  }
}
