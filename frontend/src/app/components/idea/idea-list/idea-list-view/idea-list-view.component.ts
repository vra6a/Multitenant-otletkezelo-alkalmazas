import { Component, Input, OnInit } from '@angular/core';
import { IdeaSlimDto } from 'src/app/models/slimDto/ideaSlimDto';

@Component({
  selector: 'app-idea-list-view',
  templateUrl: './idea-list-view.component.html',
  styleUrls: ['./idea-list-view.component.scss'],
})
export class IdeaListViewComponent implements OnInit {
  constructor() {}

  @Input() idea: IdeaSlimDto;

  ngOnInit(): void {}
}
