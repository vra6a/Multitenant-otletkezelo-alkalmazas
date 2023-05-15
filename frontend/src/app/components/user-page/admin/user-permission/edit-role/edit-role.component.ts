import { Component, Input, OnInit } from '@angular/core';
import { MatSelectChange } from '@angular/material/select';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { UserSlimDto } from 'src/app/models/slimDto/userSlimDto';
import { WebResponse } from 'src/app/models/webResponse';
import { SnackBarService } from 'src/app/services/snackBar.service';
import { UserService } from 'src/app/services/user.service';

@UntilDestroy()
@Component({
  selector: 'app-edit-role',
  templateUrl: './edit-role.component.html',
  styleUrls: ['./edit-role.component.scss'],
})
export class EditRoleComponent implements OnInit {
  constructor(
    private userService: UserService,
    private snackBar: SnackBarService
  ) {}

  @Input() originalRole: string = '';
  @Input() id: number = null;
  role: string = this.originalRole;
  showEditButton: boolean = false;

  ngOnInit(): void {
    this.role = this.originalRole;
  }

  edit() {
    this.userService
      .editUserPermission$(this.id, this.role)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<UserSlimDto>) => {
        if (res.code == 200) {
          this.snackBar.ok(res.message);
          this.showEditButton = false;
          this.originalRole = this.role;
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  change(event: MatSelectChange) {
    this.showEditButton = false;
    if (this.originalRole != event.value) {
      this.showEditButton = true;
      this.role = event.value;
    }
  }
}
