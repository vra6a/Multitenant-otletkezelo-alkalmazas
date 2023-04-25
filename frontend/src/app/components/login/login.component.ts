import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth/auth.service';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { UserService } from 'src/app/services/user/user.service';
import { Router } from '@angular/router';
import { WebResponse } from 'src/app/models/webResponse';
import { MatSnackBar } from '@angular/material/snack-bar';

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
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  loginForm = this.fb.group({
    email: ['', Validators.required],
    password: ['', Validators.required],
  });

  ngOnInit(): void {}

  login() {
    this.userService
      .loginUser$(this.loginForm.value)
      .pipe(untilDestroyed(this))
      .subscribe(
        (res: WebResponse) => {
          this.snackBar.open(res.message);
          if (res.code == 200) {
            this.auth.setCurrentUser(res.data);
            this.router.navigateByUrl('/idea-boxes');
            console.log(res);
          }
        },
        (res: any) => {
          this.snackBar.open(res.error.message);
        }
      );
  }
}
