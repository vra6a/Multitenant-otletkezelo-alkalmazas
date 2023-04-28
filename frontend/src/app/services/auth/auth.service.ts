import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../../models/user';
import { Subject } from 'rxjs';
import { WebData } from 'src/app/models/webData';
import { Role } from 'src/app/models/Role';
import jwt_decode from 'jwt-decode';
import { UserService } from '../user.service';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { WebResponse } from 'src/app/models/webResponse';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SnackBarService } from '../snackBar.service';

@UntilDestroy()
@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(
    private userService: UserService,
    private router: Router,
    private snackBar: SnackBarService
  ) {}

  currentUser: User = null;
  login = new Subject<User>();
  accessToken: string = '';

  setCurrentUser(data: WebData) {
    console.log(data);
    if (this.currentUser == null) {
      this.currentUser = {
        id: data.id,
        firstName: data.firstName,
        lastName: data.lastName,
        email: data.email,
        role: data.role as unknown as Role,
      };
      if (data.token != '') {
        this.setAccessToken(data.token);
      }
      this.login.next(this.currentUser);
    }
  }

  tryLogin() {
    let jwt = window.sessionStorage.getItem('token');
    if (jwt != undefined) {
      let tokenInfo = this.getDecodedAccessToken(jwt);
      let user: WebData = {
        id: tokenInfo.id,
        firstName: tokenInfo.firstName,
        lastName: tokenInfo.lastName,
        email: tokenInfo.email,
        role: tokenInfo.role,
      };
      this.setCurrentUser(user);
      this.snackBar.ok(
        'User was logged is as ' + user.firstName + ' ' + user.lastName
      );
      this.router.navigateByUrl('/idea-boxes');
    }
  }

  private getDecodedAccessToken(token: string): any {
    try {
      return jwt_decode(token);
    } catch (Error) {
      return null;
    }
  }

  setAccessToken(token: string) {
    this.accessToken = token;
    if (token != undefined) window.sessionStorage.setItem('token', token);
  }

  getCurrentUser(): User {
    return this.currentUser;
  }

  getAccessToken(): string {
    if (this.accessToken != '') {
      return this.accessToken;
    } else {
      let token = window.sessionStorage.getItem('token');
      if (token) {
        return token;
      } else {
        return '';
      }
    }
  }

  logOut() {
    if (this.currentUser) {
      this.currentUser = null;
      window.sessionStorage.removeItem('token');
      this.accessToken = '';
    }
  }
}
