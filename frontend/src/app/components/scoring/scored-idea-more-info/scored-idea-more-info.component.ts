import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { ECharts, EChartsOption } from 'echarts';
import { Subscription } from 'rxjs';
import { IdeaBoxDto } from 'src/app/models/dto/ideaBoxDto';
import { IdeaDto } from 'src/app/models/dto/ideaDto';
import { ScoreSheetDto } from 'src/app/models/dto/scoreScheetDto';
import { IdeaScoreSheets } from 'src/app/models/dto/utility/ideaScoreSheets';
import { WebResponse } from 'src/app/models/webResponse';
import { AuthService } from 'src/app/services/auth/auth.service';
import { IdeaService } from 'src/app/services/idea.service';
import { IdeaBoxService } from 'src/app/services/ideaBox.service';
import { ScoreSheetService } from 'src/app/services/scoreSheet.service';
import { SnackBarService } from 'src/app/services/snackBar.service';

@UntilDestroy()
@Component({
  selector: 'app-scored-idea-more-info',
  templateUrl: './scored-idea-more-info.component.html',
  styleUrls: ['./scored-idea-more-info.component.scss']
})
export class ScoredIdeaMoreInfoComponent implements OnInit {

  constructor(
    private ideaBoxService: IdeaBoxService,
    private scoreSheetService: ScoreSheetService,
    private ideaService: IdeaService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: SnackBarService,
    private authService: AuthService
  ) { }

  routeSub: Subscription
  id = null
  selectedIdeaBox: IdeaBoxDto = null
  selectedIdea: string = ""
  isReadyToClose = false
  scoreSheet: ScoreSheetDto = null
  ssp_option = null
  scoreOption: EChartsOption = null
  ideaOption: EChartsOption = null
  selectedScoreId = null
  chartInstance: ECharts
  viewNumbers = true
  selectedFullIdea: IdeaDto = null
  juries = [4, 6, 7]
  currentUserRole = null


  ngOnInit(): void {
    this.currentUserRole = this.authService.getRole()
    this.routeSub = this.route.params.subscribe( params => {
      this.id = params['id']
      this.ideaBoxService
      .getIdeaBox$(this.id.toString())
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaBoxDto>) => {
        this.selectedIdeaBox = res.data
        console.log(this.selectedIdeaBox)
        this.ideaBoxService
          .checkIfIdeaBoxHasAllRequiredScoreSheets$(this.selectedIdeaBox?.id)
          .pipe(untilDestroyed(this))
          .subscribe((res: WebResponse<Boolean>) => {
            this.isReadyToClose = res.data && new Date(this.selectedIdeaBox.endDate) < new Date()
            //TODO isClosed
          })
        
        this.scoreSheetService
          .getScoreSheetById$(this.selectedIdeaBox.scoreSheetTemplates[0].id.toString())
          .pipe(untilDestroyed(this))
          .subscribe((res: WebResponse<ScoreSheetDto>) => {
            this.scoreSheet = res.data
          })
      })
    })
  }

  changeScore(event) {
    this.selectedScoreId = event.value
    this.ideaBoxService
      .getScoreSheetsForIdeaBox$(this.id)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaScoreSheets[]>) => {
          this.scoreOption =  this.generateScoreOptions(res.data, event.value.title)
      })
  }

  changeIdea(event) {
    this.selectedIdea = event.value
    this.ideaBoxService
      .getScoreSheetsForIdeaBox$(this.id)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaScoreSheets[]>) => {
          this.ideaOption =  this.generateIdeaOptions(res.data, event.value.id)
      })
    this.ideaService
      .getIdea$(event.value.id)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaDto>) => {
        this.selectedFullIdea = res.data
        console.log(res.data)
      })
  }

  generateScoreOptions(data: IdeaScoreSheets[], filter: string): EChartsOption {
    console.log(filter, data)
    let xAxisData: string[] = this.selectedIdeaBox.ideas.map(idea => idea.title).filter(title => title !== undefined) as string[]
    let seriesData: number[] = []
    data.forEach( ideaScoreSheet => {
      let avgScore = 0
      let count = 0
      ideaScoreSheet.scoreSheet.forEach( sheet => {
        sheet.scores.forEach( score => {
          if(score.title == filter) {
            avgScore += score.score
            count++
          }
        })
      })
      avgScore /= count
      seriesData.push(avgScore)
    })
    let option: EChartsOption = {
      xAxis: {
        type: 'category',
        data: xAxisData,
        axisLabel: {
          rotate: 90  // Rotate the x-axis labels 90 degrees
        }
        
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: seriesData,
          type: 'bar',
          label: {
            show: this.viewNumbers,  // Display the label
            position: 'top',  // Position the label at the top of each bar
            formatter: '{c}'   // Use the data value as the label
          }
        }
      ]
    };
    return option
  }

  generateIdeaOptions(data: IdeaScoreSheets[], filter: string): EChartsOption {
    let filteredData = data.find(iss => iss.idea.id.toString() == filter)
    let indicators = this.scoreSheet.scores.map(score => {
      return {
        name: score.title,
        max: score.type === 'STAR' ? 5 : score.type === 'SLIDER' ? 10 : 0
      }
    })

    let values =[]
    let juries = []

    filteredData.scoreSheet.forEach( sheet => {
      let jury = sheet.owner.firstName + " " + sheet.owner.lastName
      juries.push(jury)
      let scores = []
      sheet.scores.forEach( score => {
        scores.push(score.score)
      })
      values.push({value: scores, name: jury})
    })

    let option: EChartsOption = {
      legend: {
        data: juries
      },
      radar: {
        radius: '60%',
        indicator: indicators
      },
      series: [
        {
          type: 'radar',
          data: values,
          label: {
            show: this.viewNumbers, // Enable showing the values
            
          }
        }
      ]
    };
    return option
  }

  toggleViewNumbers(event) {
    this.viewNumbers = event.checked
  }

  onChartInit(chartInstance: ECharts) {
    this.chartInstance = chartInstance
    this.chartInstance.on('click', (params) => {
      const clickedCategory = params.name
      const clickedValue = params.value
      this.handleBarClick(clickedCategory, clickedValue.toString())
    });
  }

  handleBarClick(category: string, value: string) {
    console.log(`Category: ${category}, Value: ${value}`);
  }

  backClicked() {
    this.router.navigate(["scoring","details", this.id])
  }

  allRequiredJuriesHaveScored(): boolean {
    let idea = this.selectedFullIdea
    if (!idea.requiredJuries || !idea.scoreSheets) {
        return false;
    }
    let asd = idea.requiredJuries.every((jury) => 
      idea.scoreSheets!.some((scoreSheet) => scoreSheet.owner?.id === jury.id)
    );
    return asd
  }
 
  approveIdea() {
    this.ideaService
    .approveIdea$(this.selectedFullIdea.id)
    .pipe(untilDestroyed(this))
    .subscribe((res: WebResponse<IdeaDto>) => {
      if (res.code == 200) {
        this.snackBar.ok(res.message);
        this.selectedFullIdea = res.data
      } else {
        this.snackBar.error(res.message);
      }
    })
  }

  denyIdea() {
    this.ideaService
    .denyIdea$(this.selectedFullIdea.id)
    .pipe(untilDestroyed(this))
    .subscribe((res: WebResponse<IdeaDto>) => {
      if (res.code == 200) {
        this.snackBar.ok(res.message);
        this.selectedFullIdea = res.data
      } else {
        this.snackBar.error(res.message);
      }
    })
  }
}
