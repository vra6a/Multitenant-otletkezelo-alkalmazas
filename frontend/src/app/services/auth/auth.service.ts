import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../../models/user';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient) {}

  currentUser?: User;
  login = new Subject<User>();

  setCurrentUser(user: User) {
    if (this.currentUser == undefined) {
      this.currentUser = user;
      this.login.next(this.currentUser);
    }
  }

  getCurrentUser(): User | undefined {
    return this.currentUser;
  }

  logOut() {
    if (this.currentUser != undefined) {
      this.currentUser = undefined;
    }
  }
}
