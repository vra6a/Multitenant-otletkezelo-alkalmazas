import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { UserSlimDto } from 'src/app/models/slimDto/userSlimDto';
import { WebResponse } from 'src/app/models/webResponse';
import { SnackBarService } from 'src/app/services/snackBar.service';
import { UserService } from 'src/app/services/user.service';

@UntilDestroy()
@Component({
  selector: 'app-user-permission',
  templateUrl: './user-permission.component.html',
  styleUrls: ['./user-permission.component.scss'],
})
export class UserPermissionComponent implements OnInit {
  constructor(
    private userService: UserService,
    private snackBar: SnackBarService,
    private router: Router
  ) {}

  userDataSource: UserSlimDto[] = [];
  userDisplayedColumns: string[] = [
    'id',
    'firstName',
    'lastName',
    'role',
    'actions',
  ];

  ngOnInit(): void {
    this.getUsers();
  }

  editUser(email: number) {
    this.router.navigateByUrl('/user/' + email);
  }

  deleteUser(id: number) {
    this.userService
      .deleteUser$(id)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<string>) => {
        if (res.code == 200) {
          this.snackBar.ok(res.message);
          this.getUsers();
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  private getUsers() {
    this.userService
      .getUsers$()
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<UserSlimDto[]>) => {
        if (res.code == 200) {
          this.userDataSource = res.data;
        } else {
          this.snackBar.error(res.message);
        }
      });
  }
}
