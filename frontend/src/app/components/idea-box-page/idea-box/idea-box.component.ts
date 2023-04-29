import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { IdeaBoxDto } from 'src/app/models/dto/ideaBoxDto';
import { WebResponse } from 'src/app/models/webResponse';
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
    private snackBar: SnackBarService
  ) {}

  ideaBox: IdeaBoxDto = null;
  id: string = '';
  submitted: IdeaBoxDto[] = [];
  reviewed: IdeaBoxDto[] = [];
  approved: IdeaBoxDto[] = [];
  denied: IdeaBoxDto[] = [];

  ngOnInit(): void {
    let idea: IdeaBoxDto;
    this.id = this.route.snapshot.paramMap.get('id');
    this.ideaBoxService
      .getIdeaBox$(this.id)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaBoxDto>) => {
        if (res.code == 200) {
          this.ideaBox = res.data;
          this.sortIdeas();
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
}
