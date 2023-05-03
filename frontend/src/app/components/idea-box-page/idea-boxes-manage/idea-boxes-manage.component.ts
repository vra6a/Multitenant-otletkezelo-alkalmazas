import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { IdeaBoxSlimDto } from 'src/app/models/slimDto/ideaBoxSlimDto';
import { WebResponse } from 'src/app/models/webResponse';
import { IdeaBoxService } from 'src/app/services/ideaBox.service';
import { SnackBarService } from 'src/app/services/snackBar.service';
import { DeleteWarningComponent } from '../../popup/delete-warning/delete-warning.component';

@UntilDestroy()
@Component({
  selector: 'app-idea-boxes-manage',
  templateUrl: './idea-boxes-manage.component.html',
  styleUrls: ['./idea-boxes-manage.component.scss'],
})
export class IdeaBoxesManageComponent implements OnInit {
  constructor(
    private ideaBoxService: IdeaBoxService,
    private router: Router,
    private snackBar: SnackBarService,
    private dialog: MatDialog
  ) {}

  displayedColumns: string[] = [
    'id',
    'name',
    'startDate',
    'endDate',
    'actions',
  ];
  dataSource: IdeaBoxSlimDto[] = [];
  ideaBoxCount: number = 0;
  pageSize: number = 10;
  sort: string = '';
  search: string = '';

  ngOnInit(): void {
    this.getIdeaBoxList(this.search, this.sort, 1, this.pageSize);

    this.ideaBoxService
      .getIdeaBoxListCount$()
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<number>) => {
        if (res.code == 200) {
          this.ideaBoxCount = res.data;
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  private getIdeaBoxList(
    search: string,
    sort: string,
    page: number,
    items: number
  ) {
    this.ideaBoxService
      .getIdeaBoxes$(search, sort, page, items)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaBoxSlimDto[]>): void => {
        this.dataSource = res.data;
      });
  }

  private filter() {
    this.getIdeaBoxList(this.search, this.sort, 1, this.pageSize);
  }

  getServerData(event?: PageEvent) {
    this.getIdeaBoxList(
      this.search,
      this.sort,
      event?.pageIndex ? event.pageIndex + 1 : 1,
      event?.pageSize ? event.pageSize : 4
    );
  }

  editIdea(id: number) {
    console.log('edit ' + id);
    this.router.navigateByUrl('/idea-boxes/' + id + '/edit');
  }

  deleteIdea(id: number) {
    console.log('delete ' + id);
    let dialogRef = this.dialog.open(DeleteWarningComponent);
    dialogRef.afterClosed().subscribe((result) => {
      if (result === 'Delete') {
        this.ideaBoxService
          .deleteIdeaBox$(id)
          .pipe(untilDestroyed(this))
          .subscribe((res: WebResponse<string>) => {
            if (res.code == 200) {
              this.snackBar.ok(res.message);
              this.getIdeaBoxList(this.search, this.sort, 1, this.pageSize);
            } else {
              this.snackBar.error(res.message);
            }
          });
      }
    });
  }
}
