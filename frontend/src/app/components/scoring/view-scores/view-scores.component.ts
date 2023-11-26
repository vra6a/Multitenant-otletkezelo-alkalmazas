import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { IdeaDto } from 'src/app/models/dto/ideaDto';
import { ScoreSheetDto } from 'src/app/models/dto/scoreScheetDto';
import { WebResponse } from 'src/app/models/webResponse';
import { IdeaService } from 'src/app/services/idea.service';
import { ScoreSheetService } from 'src/app/services/scoreSheet.service';

@UntilDestroy()
@Component({
  selector: 'app-view-scores',
  templateUrl: './view-scores.component.html',
  styleUrls: ['./view-scores.component.scss'],
})
export class ViewScoresComponent implements OnInit {
  constructor(
    private ideaService: IdeaService,
    private route: ActivatedRoute,
    private scoreSheetService: ScoreSheetService
  ) {}

  idea: IdeaDto = null;
  scores: ScoreSheetDto[] = [];
  ideaId: string = '';
  template: ScoreSheetDto = null;
  scoreSheets: ScoreSheetDto[] = [];

  ngOnInit(): void {
    this.ideaId = this.route.snapshot.paramMap.get('id');
    this.ideaService
      .getIdea$(this.ideaId)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaDto>) => {
        this.idea = res.data;
        this.scoreSheetService
          .getScoreSheetsByIdeaId$(this.idea.id)
          .pipe(untilDestroyed(this))
          .subscribe((res: WebResponse<ScoreSheetDto[]>) => {
            res.data.forEach((ss, index) => {
              if (index != 0) {
                this.scoreSheets.push(ss);
              }
            });

            this.template = res.data[0];
            this.calculateAverageScore();
          });
      });
  }

  private calculateAverageScore() {
    console.log(this.template);
    console.log(this.scoreSheets);

    let values = new Map<string, number>();
    this.template.scores.forEach((score) => {
      values.set(score.title, 0);
    });
    console.log(values);
    this.scoreSheets.forEach((sh) => {
      sh.scores.forEach((score) => {
        let value = values.get(score.title);
        value += score.score;
        values.set(score.title, value);
      });
    });
    console.log(values);
    this.template.scores.forEach((score) => {
      score.score = values.get(score.title) / this.scoreSheets.length;
    });
    console.log(this.template);
  }
}
