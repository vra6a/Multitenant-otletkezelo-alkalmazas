import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { IdeaBoxDto } from 'src/app/models/dto/ideaBoxDto';
import { UserSlimDto } from 'src/app/models/slimDto/userSlimDto';
import { WebResponse } from 'src/app/models/webResponse';
import { AuthService } from 'src/app/services/auth/auth.service';
import { IdeaBoxService } from 'src/app/services/ideaBox.service';
import { SnackBarService } from 'src/app/services/snackBar.service';

@UntilDestroy()
@Component({
  selector: 'app-idea-box',
  templateUrl: './idea-box.component.html',
  styleUrls: ['./idea-box.component.scss'],
})
export class IdeaBoxComponent implements OnInit {
  constructor(
    private ideaBoxService: IdeaBoxService,
    private route: ActivatedRoute,
    private snackBar: SnackBarService,
    private auth: AuthService
  ) {}

  ideaBox: IdeaBoxDto = null;
  id: string = '';
  canEdit: boolean = false;

  submitted: IdeaBoxDto[] = [];
  reviewed: IdeaBoxDto[] = [];
  approved: IdeaBoxDto[] = [];
  denied: IdeaBoxDto[] = [];

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id');
    this.ideaBoxService
      .getIdeaBox$(this.id)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaBoxDto>) => {
        if (res.code == 200) {
          this.ideaBox = res.data;
          this.sortIdeas();
          this.checkCanEdit();
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  private sortIdeas() {
    this.ideaBox.ideas.forEach((idea) => {
      switch (idea.status) {
        case 'SUBMITTED':
          this.submitted.push(idea);
          break;
        case 'REVIEWED':
          this.reviewed.push(idea);
          break;
        case 'APPROVED':
          this.approved.push(idea);
          break;
        case 'DENIED':
          this.denied.push(idea);
          break;
      }
    });
  }

  private checkCanEdit() {
    let user = this.auth.getCurrentUser();
    if (user.role == 'ADMIN') {
      this.canEdit = true;
    } else if (user.id == this.ideaBox.creator.id) {
      this.canEdit = true;
    } else {
      this.canEdit = false;
    }
  }
}
