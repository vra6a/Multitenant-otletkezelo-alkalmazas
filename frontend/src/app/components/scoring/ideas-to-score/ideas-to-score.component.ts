import { Component, OnInit } from '@angular/core';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { IdeaDto } from 'src/app/models/dto/ideaDto';
import { WebResponse } from 'src/app/models/webResponse';
import { IdeaService } from 'src/app/services/idea.service';

@UntilDestroy()
@Component({
  selector: 'app-ideas-to-score',
  templateUrl: './ideas-to-score.component.html',
  styleUrls: ['./ideas-to-score.component.scss'],
})
export class IdeasToScoreComponent implements OnInit {
  constructor(private ideaService: IdeaService) {}

  ideasToScore: IdeaDto[] = [];

  ngOnInit(): void {
    this.ideaService
      .getIdeasToScore$()
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaDto[]>) => {
        this.ideasToScore = res.data;
      });
  }
}
