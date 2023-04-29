import { Component, Input, OnInit } from '@angular/core';
import { UntypedFormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { IdeaBoxSlimDto } from 'src/app/models/slimDto/ideaBoxSlimDto';
import { IdeaSlimDto } from 'src/app/models/slimDto/ideaSlimDto';
import { UserSlimDto } from 'src/app/models/slimDto/userSlimDto';
import { WebResponse } from 'src/app/models/webResponse';
import { AuthService } from 'src/app/services/auth/auth.service';
import { IdeaService } from 'src/app/services/idea.service';
import { IdeaBoxService } from 'src/app/services/ideaBox.service';
import { SnackBarService } from 'src/app/services/snackBar.service';

@UntilDestroy()
@Component({
  selector: 'app-idea-create',
  templateUrl: './idea-create.component.html',
  styleUrls: ['./idea-create.component.scss'],
})
export class IdeaCreateComponent implements OnInit {
  constructor(
    private fb: UntypedFormBuilder,
    private auth: AuthService,
    private route: ActivatedRoute,
    private router: Router,
    private ideaBoxService: IdeaBoxService,
    private ideaService: IdeaService,
    private snackBar: SnackBarService
  ) {}

  ideaBox: IdeaBoxSlimDto;
  user: UserSlimDto = null;
  id: string = '';

  IdeaForm = this.fb.group({
    title: ['', Validators.required],
    description: ['', Validators.required],
    owner: this.user,
    tags: [[]],
  });

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id');
    this.user = this.auth.getCurrentUser();
    this.ideaBoxService
      .getIdeaBoxSlim$(this.id)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaBoxSlimDto>) => {
        if (res.code == 200) {
          this.ideaBox = res.data;
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  create() {
    let idea = this.IdeaForm.value;
    idea.owner = this.user;
    idea.status = 'SUBMITTED';
    idea.creationDate = '';
    idea.ideaBox = this.ideaBox;
    this.ideaService
      .createIdea$(idea)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaSlimDto>) => {
        if (res.code == 200) {
          this.snackBar.ok(res.message);
          this.router.navigateByUrl('/idea-boxes/' + this.id);
        } else {
          this.snackBar.error(res.message);
          this.router.navigateByUrl('/idea-boxes/' + this.id);
        }
      });
  }
}
