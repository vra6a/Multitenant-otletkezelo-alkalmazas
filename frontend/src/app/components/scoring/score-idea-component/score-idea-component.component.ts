import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { IdeaDto } from 'src/app/models/dto/ideaDto';
import { WebResponse } from 'src/app/models/webResponse';
import { IdeaService } from 'src/app/services/idea.service';

@UntilDestroy()
@Component({
  selector: 'app-score-idea-component',
  templateUrl: './score-idea-component.component.html',
  styleUrls: ['./score-idea-component.component.scss'],
})
export class ScoreIdeaComponentComponent implements OnInit {
  constructor(
    private ideaService: IdeaService,
    private route: ActivatedRoute
  ) {}

  idea: IdeaDto = null;
  ideaId: string = '';

  ngOnInit(): void {
    this.ideaId = this.route.snapshot.paramMap.get('id');
    this.ideaService
      .getIdea$(this.ideaId)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaDto>) => {
        this.idea = res.data;
      });
  }
}
