import { Component, OnInit } from '@angular/core';
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
    private ideaService: IdeaService,
    private scoreSheetService: ScoreSheetService,
    private ideaBoxService: IdeaBoxService,
  ) {}

  scoredIdeaBoxes: IdeaBoxSlimDto[] = [];
  selectedIdeaBox: IdeaBoxDto = null;
  ScoredIdeaCountByIdeaBox: number = null
  option: EChartsOption = null;

  ngOnInit(): void {
    this.scoreSheetService
      .getScoredIdeaBoxes$()
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaBoxSlimDto[]>) => {
        this.scoredIdeaBoxes = res.data;
        console.log(res)
      });
  }

  ideaBoxClicked(ideaBox: IdeaBoxSlimDto): void {
    this.ideaBoxService
      .getIdeaBox$(ideaBox.id.toString())
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaBoxDto>) => {
        this.selectedIdeaBox = res.data

        this.ideaBoxService
          .getScoredIdeaCountByIdeaBox$(this.selectedIdeaBox.id.toString())
          .pipe(untilDestroyed(this))
          .subscribe((res: WebResponse<number>) => {
            this.ScoredIdeaCountByIdeaBox = res.data
            this.createDiagram()
          })
      })
  }

  createDiagram() {
    this.option = {
      tooltip: {
        trigger: 'item'
      },
      legend: {
        top: '5%',
        left: 'center'
      },
      series: [
        {
          name: 'Access From',
          type: 'pie',
          radius: ['20%', '70%'],
          avoidLabelOverlap: true,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 3
          },
          label: {
            show: false,
            position: 'center'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: 40,
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          data: [
            { value: this.selectedIdeaBox.ideas.length, name: 'Scored ideas' },
            { value: this.ScoredIdeaCountByIdeaBox, name: 'Not scored ideas' },
          ]
        }
      ]
    };
  }

}
