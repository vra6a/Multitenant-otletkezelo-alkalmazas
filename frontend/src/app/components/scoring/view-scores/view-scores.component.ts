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

  ngOnInit(): void {
    this.ideaId = this.route.snapshot.paramMap.get('id');
    this.ideaService
      .getIdea$(this.ideaId)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaDto>) => {
        this.idea = res.data;
        let isFirst = true;
        res.data.scoreSheets.forEach((sheet) => {
          this.scoreSheetService
            .getScoreSheetById$(sheet.id.toString())
            .pipe(untilDestroyed(this))
            .subscribe((res: WebResponse<ScoreSheetDto>) => {
              this.scores.push(res.data);
              console.log(res.data);
              if (isFirst) {
                this.template = res.data;
                console.log(this.template);
              }
              isFirst = false;
            });
        });
      });
  }

  private calculateAverageScore() {
    this.scores.forEach((score) => {
      let sliderCount = 0;
      let sliderValue = 0;
      let starCount = 0;
      let starValue = 0;
      let value = 0;
      let count = 0;

      score.scores.forEach((s) => {
        switch (s.type) {
          case 'SLIDER':
            sliderValue++;
            sliderCount += s.score;
            break;
          case 'STAR':
            starCount++;
            starValue += s.score;
            break;
        }
      });
    });
  }
}
