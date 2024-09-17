import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { EChartsOption } from 'echarts';
import { pipe } from 'rxjs';
import { IdeaBoxDto } from 'src/app/models/dto/ideaBoxDto';
import { IdeaDto } from 'src/app/models/dto/ideaDto';
import { IdeaBoxSlimDto } from 'src/app/models/slimDto/ideaBoxSlimDto';
import { WebResponse } from 'src/app/models/webResponse';
import { IdeaService } from 'src/app/services/idea.service';
import { IdeaBoxService } from 'src/app/services/ideaBox.service';
import { ScoreSheetService } from 'src/app/services/scoreSheet.service';

@UntilDestroy()
@Component({
  selector: 'app-view-scored-ideas',
  templateUrl: './view-scored-ideas.component.html',
  styleUrls: ['./view-scored-ideas.component.scss'],
})
export class ViewScoredIdeasComponent implements OnInit {
  constructor(
    private scoreSheetService: ScoreSheetService,
    private router: Router
  ) {}

  scoredIdeaBoxes: IdeaBoxSlimDto[] = [];
  selectedId = null
  ScoredIdeaCountByIdeaBox: number = null
  option: EChartsOption = null;

  ngOnInit(): void {
    this.scoreSheetService
      .getScoredIdeaBoxes$()
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaBoxSlimDto[]>) => {
        this.scoredIdeaBoxes = res.data.filter( box => box.isSclosed != true)
        console.log(res)
      });
  }

  ideaBoxClicked(ideaBox: IdeaBoxSlimDto): void {
    this.router.navigate(["scoring","details",ideaBox.id])
    this.selectedId = ideaBox.id
  }
}
