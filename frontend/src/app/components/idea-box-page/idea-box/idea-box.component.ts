import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { Idea } from 'src/app/models/idea';
import { IdeaBox } from 'src/app/models/ideaBox';
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

  ideaBox: IdeaBox = null;
  id: string = '';
  submitted: Idea[] = [];
  reviewed: Idea[] = [];
  approved: Idea[] = [];
  denied: Idea[] = [];

  ngOnInit(): void {
    let idea: Idea;
    this.submitted.push(idea);
    this.submitted.push(idea);
    this.submitted.push(idea);
    this.submitted.push(idea);
    this.submitted.push(idea);

    this.submitted.push(idea);
    this.submitted.push(idea);
    this.reviewed.push(idea);
    this.reviewed.push(idea);
    this.id = this.route.snapshot.paramMap.get('id');
    this.ideaBoxService
      .getIdeaBox$(this.id)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaBox>) => {
        if (res.code == 200) {
          this.ideaBox = res.data;
        } else {
          this.snackBar.error(res.message);
        }
      });
  }
}
