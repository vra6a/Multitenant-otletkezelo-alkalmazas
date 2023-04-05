import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { Role } from 'src/app/models/Role';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user/user.service';

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
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  roles: Role[] = [];
  userForm = this.fb.group({
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    email: ['', Validators.required],
    role: ['', Validators.required],
  });

  ngOnInit(): void {
    this.userService
      .getRoles$()
      .pipe(untilDestroyed(this))
      .subscribe((roles: Role[]): void => {
        this.roles = roles;
        console.log(this.roles);
      });
  }

  create() {
    this.userService
      .createUser$(this.userForm.value)
      .pipe(untilDestroyed(this))
      .subscribe(
        () => {
          this.snackBar.open('User created!', 'OK!');
          this.router.navigate(['/login']);
        },
        () => {
          this.snackBar.open('User creation failed!', 'OK!');
        }
      );
  }
}
