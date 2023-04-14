import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../../models/user';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient) {}

  currentUser: User = null;
  login = new Subject<User>();

  setCurrentUser(user: User) {
    if (this.currentUser == null) {
      this.currentUser = user;
      this.login.next(this.currentUser);
    }
  }

  getCurrentUser(): User {
    console.log(this.currentUser);
    return this.currentUser;
  }

  logOut() {
    if (this.currentUser) {
      this.currentUser = null;
    }
  }
}
