import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { IdeaBox } from 'src/app/models/ideaBox';
import { AuthService } from 'src/app/services/auth/auth.service';
import { IdeaBoxService } from 'src/app/services/ideaBox.service';

@UntilDestroy()
@Component({
  selector: 'app-idea-box-create',
  templateUrl: './idea-box-create.component.html',
  styleUrls: ['./idea-box-create.component.scss'],
})
export class IdeaBoxCreateComponent implements OnInit {
  constructor(
    private fb: UntypedFormBuilder,
    private auth: AuthService,
    private snackBar: MatSnackBar,
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
    console.log(this.IdeaBoxForm.value);
    this.ideaBoxService
      .createIdeaBox(this.IdeaBoxForm.value)
      .subscribe((res: IdeaBox) => {
        if (res.id) {
          this.router.navigate(['/idea-boxes']);
        }
      });
  }
}
