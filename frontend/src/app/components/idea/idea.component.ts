import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { IdeaDto } from 'src/app/models/dto/ideaDto';
import { WebResponse } from 'src/app/models/webResponse';
import { IdeaService } from 'src/app/services/idea.service';
import { SnackBarService } from 'src/app/services/snackBar.service';

@UntilDestroy()
@Component({
  selector: 'app-box',
  templateUrl: './idea.component.html',
  styleUrls: ['./idea.component.scss'],
})
export class IdeaComponent implements OnInit {
  constructor(
    private route: ActivatedRoute,
    private snackBar: SnackBarService,
    private ideaService: IdeaService
  ) {}

  idea: IdeaDto = null;
  id: string = '';

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id');
    this.ideaService
      .getIdea$(this.id)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaDto>) => {
        if (res.code == 200) {
          this.idea = res.data;
        } else {
          this.snackBar.error(res.message);
        }
      });
  }
}
