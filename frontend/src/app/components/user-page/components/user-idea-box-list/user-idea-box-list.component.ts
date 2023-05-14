import { Component, Input, OnInit } from '@angular/core';
import { IdeaBoxSlimDto } from 'src/app/models/slimDto/ideaBoxSlimDto';

@Component({
  selector: 'app-user-idea-box-list',
  templateUrl: './user-idea-box-list.component.html',
  styleUrls: ['./user-idea-box-list.component.scss'],
})
export class UserIdeaBoxListComponent implements OnInit {
  constructor() {}

  @Input() ideaBoxes: IdeaBoxSlimDto[] = [];

  ngOnInit(): void {}
}
