import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { IdeaBoxDto } from 'src/app/models/dto/ideaBoxDto';
import { WebResponse } from 'src/app/models/webResponse';
import { IdeaBoxService } from 'src/app/services/ideaBox.service';
import { SnackBarService } from 'src/app/services/snackBar.service';

@UntilDestroy()
@Component({
  selector: 'app-idea-box-create',
  templateUrl: './idea-box-create.component.html',
  styleUrls: ['./idea-box-create.component.scss'],
})
export class IdeaBoxCreateComponent implements OnInit {
  constructor(
    private fb: UntypedFormBuilder,
    private snackBar: SnackBarService,
    private router: Router,
    private ideaBoxService: IdeaBoxService
  ) {}

  IdeaBoxForm = this.fb.group({
    name: ['', Validators.required],
    description: ['', Validators.required],
    startDate: ['', Validators.required],
    endDate: ['', Validators.required],
  });

  ngOnInit(): void {}

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
}
