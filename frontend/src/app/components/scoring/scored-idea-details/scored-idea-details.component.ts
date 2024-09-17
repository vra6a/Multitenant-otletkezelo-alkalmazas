import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { EChartsOption } from 'echarts';
import { Subscription } from 'rxjs';
import { IdeaBoxDto } from 'src/app/models/dto/ideaBoxDto';
import { WebResponse } from 'src/app/models/webResponse';
import { IdeaService } from 'src/app/services/idea.service';
import { IdeaBoxService } from 'src/app/services/ideaBox.service';
import { ScoreSheetService } from 'src/app/services/scoreSheet.service';
import { SnackBarService } from 'src/app/services/snackBar.service';

@UntilDestroy()
@Component({
  selector: 'app-scored-idea-details',
  templateUrl: './scored-idea-details.component.html',
  styleUrls: ['./scored-idea-details.component.scss']
})
export class ScoredIdeaDetailsComponent implements OnInit, OnDestroy {

  constructor(
    private ideaBoxService: IdeaBoxService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: SnackBarService
  ) { }

  routeSub: Subscription
  id = null
  selectedIdeaBox: IdeaBoxDto = null;
  ScoredIdeaCountByIdeaBox: number = null
  option: EChartsOption = null;
  isReadyToClose = false

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      this.id = params['id']
      this.ideaBoxService
        .isIdeaBoxReadyToClose$(this.id)
        .pipe(untilDestroyed(this))
        .subscribe((res: WebResponse<boolean>) => {
          this.isReadyToClose = res.data
        })
      this.ideaBoxService
      .getIdeaBox$(this.id.toString())
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
    });
  }

  closeIdeaBox() {
    this.ideaBoxService
      .closeIdeaBox$(this.id)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<string>) => {
        if (res.code == 200) {
          this.snackBar.ok(res.message);
        } else {
          this.snackBar.error(res.message);
        }
      })
  }

  moreInfo() {
    this.router.navigate(["scoring","info", this.id])
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
            { value: this.ScoredIdeaCountByIdeaBox, name: 'Scored ideas' },
            { value: this.selectedIdeaBox.ideas.length - this.ScoredIdeaCountByIdeaBox, name: 'Not scored ideas' },
          ]
        }
      ]
    };
  }

  ngOnDestroy() {
  this.routeSub.unsubscribe();
}

}
