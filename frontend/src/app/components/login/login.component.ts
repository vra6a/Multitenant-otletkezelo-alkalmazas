import { Component, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  UntypedFormBuilder,
  Validators,
} from '@angular/forms';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/services/auth/auth.service';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { UserService } from 'src/app/services/user/user.service';
import { Router } from '@angular/router';
import { UserListView } from 'src/app/models/userListView';

@UntilDestroy()
@Component({
  selector: 'app-login-main',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginMainComponent implements OnInit {
  constructor(
    private auth: AuthService,
    private userService: UserService,
    private fb: UntypedFormBuilder,
    private router: Router
  ) {}

  users: UserListView[] = [];

  loginForm = this.fb.group({
    user: ['', Validators.required],
  });

  ngOnInit(): void {
    this.userService
      .getUsers$()
      .pipe(untilDestroyed(this))
      .subscribe((data: UserListView[]): void => {
        this.users = data;
        console.log(this.users);
      });
  }

  login() {
    this.auth.setCurrentUser(this.loginForm.value);
    this.router.navigate(['/idea-boxes']);
  }
}
