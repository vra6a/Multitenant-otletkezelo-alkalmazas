import { Component, Input, OnInit } from '@angular/core';
import { IdeaBoxSlimDto } from 'src/app/models/slimDto/ideaBoxSlimDto';

@Component({
  selector: 'app-idea-box-list-view',
  templateUrl: './idea-box-list-view.component.html',
  styleUrls: ['./idea-box-list-view.component.scss'],
})
export class IdeaBoxListViewComponent implements OnInit {
  constructor() {}

  @Input() ideaBox: IdeaBoxSlimDto;

  ngOnInit(): void {}
}
