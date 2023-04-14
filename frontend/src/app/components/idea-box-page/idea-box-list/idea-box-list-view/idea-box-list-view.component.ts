import { Component, Input, OnInit } from '@angular/core';
import { IdeaBoxListView } from 'src/app/models/ideaBoxListView';

@Component({
  selector: 'app-idea-box-list-view',
  templateUrl: './idea-box-list-view.component.html',
  styleUrls: ['./idea-box-list-view.component.scss'],
})
export class IdeaBoxListViewComponent implements OnInit {
  constructor() {}

  @Input() ideaBox: IdeaBoxListView;

  ngOnInit(): void {}
}
