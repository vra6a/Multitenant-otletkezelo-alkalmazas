import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { User } from 'src/app/models/dto/userDto';
import { WebResponse } from 'src/app/models/webResponse';
import { AuthService } from 'src/app/services/auth/auth.service';
import { SnackBarService } from 'src/app/services/snackBar.service';
import { UserService } from 'src/app/services/user.service';

@UntilDestroy()
@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrls: ['./user-page.component.scss'],
})
export class UserPageComponent implements OnInit {
  constructor(
    private authService: AuthService,
    private router: Router,
    private snackBar: SnackBarService,
    private route: ActivatedRoute,
    private userService: UserService
  ) {}

  currentUser: User = null;

  ngOnInit(): void {
    let email = this.route.snapshot.paramMap.get('email');
    this.userService
      .getUserByEmail$(email)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<User>) => {
        if (res.code == 200) {
          this.currentUser = res.data;
        } else {
          this.snackBar.error(res.message);
        }
      });
  }
}
