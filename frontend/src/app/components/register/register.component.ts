import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { WebResponse } from 'src/app/models/webResponse';
import { AuthService } from 'src/app/services/auth/auth.service';
import { SnackBarService } from 'src/app/services/snackBar.service';
import { UserService } from 'src/app/services/user.service';

@UntilDestroy()
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit {
  constructor(
    private fb: UntypedFormBuilder,
    private userService: UserService,
    private snackBar: SnackBarService,
    private auth: AuthService,
    private router: Router
  ) {}

  userForm = this.fb.group({
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    email: ['', Validators.required],
    password: ['', Validators.required],
  });

  ngOnInit(): void {}

  create() {
    this.userService
      .registerUser$(this.userForm.value)
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
