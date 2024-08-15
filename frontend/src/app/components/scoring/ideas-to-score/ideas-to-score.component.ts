import { Component, OnInit } from '@angular/core';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { IdeaDto } from 'src/app/models/dto/ideaDto';
import { ScoreSheetDto } from 'src/app/models/dto/scoreScheetDto';
import { BulkIdeaDto } from 'src/app/models/dto/utility/BulkIdeaDto';
import { WebResponse } from 'src/app/models/webResponse';
import { IdeaService } from 'src/app/services/idea.service';
import { ScoreSheetService } from 'src/app/services/scoreSheet.service';

@UntilDestroy()
@Component({
  selector: 'app-ideas-to-score',
  templateUrl: './ideas-to-score.component.html',
  styleUrls: ['./ideas-to-score.component.scss'],
})
export class IdeasToScoreComponent implements OnInit {
  constructor(private ideaService: IdeaService) {}

  ideasToScore: BulkIdeaDto[] = [];

  ngOnInit(): void {
    this.ideaService
      .getBulkIdeas$()
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<BulkIdeaDto[]>) => {
        this.ideasToScore = res.data;
        console.log(res)
      });
  }
}
