import { Component, OnInit } from '@angular/core';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { IdeaDto } from 'src/app/models/dto/ideaDto';
import { WebResponse } from 'src/app/models/webResponse';
import { IdeaService } from 'src/app/services/idea.service';

@UntilDestroy()
@Component({
  selector: 'app-view-scored-ideas',
  templateUrl: './view-scored-ideas.component.html',
  styleUrls: ['./view-scored-ideas.component.scss'],
})
export class ViewScoredIdeasComponent implements OnInit {
  constructor(private ideaService: IdeaService) {}

  scoredIdeas: IdeaDto[] = [];

  ngOnInit(): void {
    this.ideaService
      .getScoredIdeas$()
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaDto[]>) => {
        this.scoredIdeas = res.data;
      });
  }
}
