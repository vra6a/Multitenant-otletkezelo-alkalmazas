import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { IdeaBoxDto } from 'src/app/models/dto/ideaBoxDto';
import { IdeaDto } from 'src/app/models/dto/ideaDto';
import { ScoreSheetDto } from 'src/app/models/dto/scoreScheetDto';
import { WebResponse } from 'src/app/models/webResponse';
import { IdeaService } from 'src/app/services/idea.service';
import { IdeaBoxService } from 'src/app/services/ideaBox.service';
import { ScoreSheetService } from 'src/app/services/scoreSheet.service';

@UntilDestroy()
@Component({
  selector: 'app-score-idea-component',
  templateUrl: './score-idea-component.component.html',
  styleUrls: ['./score-idea-component.component.scss'],
})
export class ScoreIdeaComponentComponent implements OnInit {
  constructor(
    private ideaService: IdeaService,
    private route: ActivatedRoute,
    private scoreSheetService: ScoreSheetService,
    private ideaBoxService: IdeaBoxService
  ) {}

  scoreSheet: ScoreSheetDto = null;
  idea: IdeaDto = null;
  ideaId: string = '';

  ngOnInit(): void {
    this.ideaId = this.route.snapshot.paramMap.get('id');
    this.ideaService
      .getIdea$(this.ideaId)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaDto>) => {
        this.idea = res.data;
        this.ideaBoxService
          .getIdeaBox$(res.data.ideaBox.id.toString())
          .pipe(untilDestroyed(this))
          .subscribe((res: WebResponse<IdeaBoxDto>) => {
            this.scoreSheetService
              .getScoreSheetById$(res.data.scoreSheetTemplates[0].id.toString())
              .pipe(untilDestroyed(this))
              .subscribe((res: WebResponse<ScoreSheetDto>) => {
                console.log(res);
                this.scoreSheet = res.data;
              });
          });
      });
  }

  saveItem(event: number, id: number) {
    console.log(event, id);
    this.scoreSheet.scores.find((item) => item.id == id).score = event;
    console.log(this.scoreSheet.scores.find((item) => item.id == id));
  }

  save() {
    let invalid = this.scoreSheet.scores.find((item) => item.score == null);
    if (!invalid) {
      console.log('asd');
      this.scoreSheetService
        .saveScoreSheet(this.scoreSheet)
        .pipe(untilDestroyed(this))
        .subscribe((res: WebResponse<ScoreSheetDto>) => {
          console.log(res);
        });
    }
  }
}
