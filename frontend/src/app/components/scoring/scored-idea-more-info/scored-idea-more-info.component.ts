import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { Subscription } from 'rxjs';
import { IdeaBoxDto } from 'src/app/models/dto/ideaBoxDto';
import { WebResponse } from 'src/app/models/webResponse';
import { IdeaBoxService } from 'src/app/services/ideaBox.service';

@UntilDestroy()
@Component({
  selector: 'app-scored-idea-more-info',
  templateUrl: './scored-idea-more-info.component.html',
  styleUrls: ['./scored-idea-more-info.component.scss']
})
export class ScoredIdeaMoreInfoComponent implements OnInit {

  constructor(
    private ideaBoxService: IdeaBoxService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  routeSub: Subscription
  id = null
  selectedIdeaBox: IdeaBoxDto = null;
  isReadyToClose = false

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe( params => {
      this.id = params['id']
      this.ideaBoxService
      .getIdeaBox$(this.id.toString())
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaBoxDto>) => {
        this.selectedIdeaBox = res.data
      })

      this.ideaBoxService
      .checkIfIdeaBoxHasAllRequiredScoreSheets$(this.selectedIdeaBox.id)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<Boolean>) => {
        this.isReadyToClose = res.data && new Date(this.selectedIdeaBox.endDate) < new Date()
        //TODO isClosed
      })
    })
  }

}
