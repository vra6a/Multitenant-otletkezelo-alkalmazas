import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { IdeaBoxDto } from 'src/app/models/dto/ideaBoxDto';
import { UserSlimDto } from 'src/app/models/slimDto/userSlimDto';
import { WebResponse } from 'src/app/models/webResponse';
import { AuthService } from 'src/app/services/auth/auth.service';
import { IdeaBoxService } from 'src/app/services/ideaBox.service';
import { SnackBarService } from 'src/app/services/snackBar.service';

@UntilDestroy()
@Component({
  selector: 'app-idea-box-create',
  templateUrl: './idea-box-create-edit.component.html',
  styleUrls: ['./idea-box-create-edit.component.scss'],
})
export class IdeaBoxCreateEditComponent implements OnInit {
  constructor(
    private fb: UntypedFormBuilder,
    private snackBar: SnackBarService,
    private auth: AuthService,
    private router: Router,
    private ideaBoxService: IdeaBoxService,
    private route: ActivatedRoute
  ) {}

  id: string = '';
  user: UserSlimDto = null;
  ideaBox: IdeaBoxDto = null;
  isEdit: boolean = false;

  IdeaBoxForm = this.fb.group({
    name: ['', Validators.required],
    description: ['', Validators.required],
    startDate: ['', Validators.required],
    endDate: ['', Validators.required],
    creator: this.user,
  });

  ngOnInit(): void {
    this.user = this.auth.getCurrentUser();
    this.id = this.route.snapshot.paramMap.get('id');
    this.IdeaBoxForm.controls['creator'].setValue(this.user);
    if (this.id) {
      this.isEdit = true;
      this.getIdeaBox();
    } else {
      this.isEdit = false;
    }
    console.log(this.isEdit);
  }

  create() {
    this.ideaBoxService
      .createIdeaBox$(this.IdeaBoxForm.value)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaBoxDto>) => {
        console.log(res);
        if (res.code == 200) {
          this.snackBar.ok(res.message);
          this.router.navigateByUrl('/idea-boxes');
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  edit() {
    this.ideaBoxService
      .editIdeaBox$(this.ideaBox.id.toString(), this.IdeaBoxForm.value)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaBoxDto>) => {
        console.log(res);
        if (res.code == 200) {
          this.snackBar.ok(res.message);
          this.router.navigateByUrl('/idea-boxes');
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  private getIdeaBox() {
    this.ideaBoxService
      .getIdeaBox$(this.id)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaBoxDto>) => {
        if (res.code == 200) {
          console.log(res.data);
          this.ideaBox = res.data;
          this.fillForm(res.data);
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  private fillForm(ib: IdeaBoxDto) {
    this.IdeaBoxForm.controls['name'].setValue(ib.name);
    this.IdeaBoxForm.controls['description'].setValue(ib.description);
    this.IdeaBoxForm.controls['startDate'].setValue(ib.startDate);
    this.IdeaBoxForm.controls['endDate'].setValue(ib.endDate);
  }
}
