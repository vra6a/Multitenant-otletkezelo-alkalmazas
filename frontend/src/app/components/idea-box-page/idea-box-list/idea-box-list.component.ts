import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { User } from 'src/app/models/dto/userDto';
import { IdeaBoxSlimDto } from 'src/app/models/slimDto/ideaBoxSlimDto';
import { WebResponse } from 'src/app/models/webResponse';
import { AuthService } from 'src/app/services/auth/auth.service';
import { IdeaBoxService } from 'src/app/services/ideaBox.service';
import { SnackBarService } from 'src/app/services/snackBar.service';

@UntilDestroy()
@Component({
  selector: 'app-idea-box-list',
  templateUrl: './idea-box-list.component.html',
  styleUrls: ['./idea-box-list.component.scss'],
})
export class IdeaBoxListComponent implements OnInit {
  @ViewChild(MatPaginator) paginator: MatPaginator | undefined;

  constructor(
    private ideaBoxService: IdeaBoxService,
    private authService: AuthService,
    private router: Router,
    private snackBar: SnackBarService
  ) {}

  ideaBoxes: IdeaBoxSlimDto[] = [];
  ideaBoxCount: number = 0;
  pageSize: number = 8;
  sort: string = '';
  search: string = '';
  currentUser: User = null;

  ngOnInit(): void {
    this.getIdeaBoxList(this.search, this.sort, 1, this.pageSize);
    this.currentUser = this.authService.getCurrentUser();

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

  getServerData(event?: PageEvent) {
    this.getIdeaBoxList(
      this.search,
      this.sort,
      event?.pageIndex ? event.pageIndex + 1 : 1,
      event?.pageSize ? event.pageSize : 4
    );
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
        this.ideaBoxes = res.data;
      });
  }

  openIdeaBox(id: number) {
    this.router.navigate(['idea-boxes/' + id]);
  }

  filter() {
    this.getIdeaBoxList(this.search, this.sort, 1, this.pageSize);
  }
}
