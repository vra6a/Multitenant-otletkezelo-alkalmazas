import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { IdeaSlimDto } from 'src/app/models/slimDto/ideaSlimDto';
import { WebResponse } from 'src/app/models/webResponse';
import { IdeaService } from 'src/app/services/idea.service';
import { SnackBarService } from 'src/app/services/snackBar.service';

@UntilDestroy()
@Component({
  selector: 'app-reviewed-ideas-list',
  templateUrl: './reviewed-ideas-list.component.html',
  styleUrls: ['./reviewed-ideas-list.component.scss'],
})
export class ReviewedIdeasListComponent implements OnInit {
  constructor(
    private router: Router,
    private ideaService: IdeaService,
    private snackBar: SnackBarService
  ) {}

  dataSource: IdeaSlimDto[] = [];
  displayedColumns: string[] = ['id', 'title', 'actions'];

  ngOnInit(): void {
    this.getIdeas();
  }

  open(id: number) {
    this.router.navigateByUrl('/idea/' + id + '/decide');
  }

  private getIdeas() {
    this.ideaService
      .getReviewedIdeas()
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaSlimDto[]>) => {
        if (res.code == 200) {
          this.dataSource = res.data;
        } else {
          this.snackBar.error(res.message);
        }
      });
  }
}
