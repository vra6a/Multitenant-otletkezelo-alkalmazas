import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { IdeaBoxListView } from 'src/app/models/ideaBoxListView';
import { IdeaBoxService } from 'src/app/services/ideaBox.service';

@UntilDestroy()
@Component({
  selector: 'app-idea-box-list',
  templateUrl: './idea-box-list.component.html',
  styleUrls: ['./idea-box-list.component.scss'],
})
export class IdeaBoxListComponent implements OnInit {
  @ViewChild(MatPaginator) paginator: MatPaginator | undefined;

  constructor(private ideaBoxService: IdeaBoxService, private router: Router) {}

  ideaBoxes: IdeaBoxListView[] = [];
  ideaBoxCount: number = 0;
  pageSize: number = 4;
  sort: string = '';
  search: string = '';

  ngOnInit(): void {
    this.getIdeaBoxList(this.search, this.sort, 1, this.pageSize);

    this.ideaBoxService
      .getIdeaBoxListCount$()
      .pipe(untilDestroyed(this))
      .subscribe((count: number): void => {
        this.ideaBoxCount = count;
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
      .getIdeaBoxListView$(search, sort, page, items)
      .pipe(untilDestroyed(this))
      .subscribe((boxes: IdeaBoxListView[]): void => {
        this.ideaBoxes = boxes;
      });
  }

  openIdeaBox(id: number) {
    this.router.navigate(['idea-boxes/' + id]);
    console.log(id);
  }
}
