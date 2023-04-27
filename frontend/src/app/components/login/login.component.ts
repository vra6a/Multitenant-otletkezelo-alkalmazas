import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth/auth.service';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { WebResponse } from 'src/app/models/webResponse';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SnackBarService } from 'src/app/services/snackBar.service';

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
    private snackBar: SnackBarService,
    private router: Router
  ) {}

  loginForm = this.fb.group({
    email: ['', Validators.required],
    password: ['', Validators.required],
  });

  ngOnInit(): void {
    this.auth.tryLogin();
  }

  login() {
    this.userService
      .loginUser$(this.loginForm.value)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse) => {
        this.snackBar.ok(res.message);
        if (res.code == 200) {
          this.auth.setCurrentUser(res.data);
          this.router.navigateByUrl('/idea-boxes');
        }
      });
  }
}
