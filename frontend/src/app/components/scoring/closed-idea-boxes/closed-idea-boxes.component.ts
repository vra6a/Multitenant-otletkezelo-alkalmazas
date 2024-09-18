import { Component, OnInit } from '@angular/core';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { EChartsOption } from 'echarts';
import { IdeaBoxDto } from 'src/app/models/dto/ideaBoxDto';
import { IdeaDto } from 'src/app/models/dto/ideaDto';
import { IdeaSlimDto } from 'src/app/models/slimDto/ideaSlimDto';
import { WebResponse } from 'src/app/models/webResponse';
import { IdeaService } from 'src/app/services/idea.service';
import { IdeaBoxService } from 'src/app/services/ideaBox.service';
import { SnackBarService } from 'src/app/services/snackBar.service';

@UntilDestroy()
@Component({
  selector: 'app-closed-idea-boxes',
  templateUrl: './closed-idea-boxes.component.html',
  styleUrls: ['./closed-idea-boxes.component.scss']
})
export class ClosedIdeaBoxesComponent implements OnInit {

  constructor(
    private ideaBoxService: IdeaBoxService,
    private snackBarService: SnackBarService,
    private ideaService: IdeaService
  ) { }

  ideaBoxes: IdeaBoxDto[] = []
  approvedIdeas: IdeaSlimDto[] = []
  selectedIdeaBox: IdeaBoxDto = null
  selectedIdea: IdeaDto = null
  option: EChartsOption = null

  ngOnInit(): void {
    this.ideaBoxService
      .getClosedIdeaBoxes$()
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaBoxDto[]>) => {
        if(res.code == 200) {
          this.ideaBoxes = res.data
          
        } else {
          this.snackBarService.error(res.message)
        }
      })
  }

  ideaBoxClicked(ideaBox: IdeaBoxDto): void {
    this.selectedIdeaBox = ideaBox
    this.option = this.createOption()
    this.approvedIdeas = ideaBox.ideas.filter(idea => idea.status == 'APPROVED')
  }

  private createOption(): EChartsOption{
    let option: EChartsOption = {
      series: {
        type: 'sankey',
        emphasis: {
          focus: 'adjacency'
        },
        data: [
          {
            name: 'Ideas'
          },
          {
            name: 'Approved'
          },
          {
            name: 'Denied'
          }
        ],
        links: [
          {
            source: 'Ideas',
            target: 'Approved',
            value: this.selectedIdeaBox.ideas.filter(idea => idea.status == 'APPROVED').length
          },
          {
            source: 'Ideas',
            target: 'Denied',
            value: this.selectedIdeaBox.ideas.filter(idea => idea.status == 'DENIED').length
          }
        ],
        label: {
          show: true, // Show labels on nodes
          position: 'right',
          rotate: 90,
          align: 'left',
          formatter: '{b}: {c}' // {b} is the name of the node, {c} is the value
        },
      
      }
    };
    return option
  }

  clicked(idea) {
    console.log(idea)
    this.ideaService
      .getIdea$(idea.id)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaDto>) => {
        this.selectedIdea = res.data
      })
  }

}
