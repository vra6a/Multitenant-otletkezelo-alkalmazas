import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { ScoreSheetDto } from 'src/app/models/dto/scoreScheetDto';
import { WebResponse } from 'src/app/models/webResponse';
import { ScoreSheetService } from 'src/app/services/scoreSheet.service';

@UntilDestroy()
@Component({
  selector: 'app-view-scoring-template',
  templateUrl: './view-scoring-template.component.html',
  styleUrls: ['./view-scoring-template.component.scss'],
})
export class ViewScoringTemplateComponent implements OnInit {
  constructor(
    private scoreSheetService: ScoreSheetService,
    private route: ActivatedRoute
  ) {
    this.arr = [1, 2, 3, 4, 5];
  }

  scoreSheet: ScoreSheetDto = null;
  id: string = '';

  arr: any[] = [];
  index: number = -1;

  sliderValue = 10;

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id');
    this.scoreSheetService
      .getScoreSheetById$(this.id)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<ScoreSheetDto>) => {
        console.log(res);
        this.scoreSheet = res.data;
      });
  }

  onClickItem(index) {
    this.index = index;
  }

  sliderChange(e: number) {
    this.sliderValue = e;
  }
}
