import { Component, Input, OnInit } from '@angular/core';
import { EChartsOption } from 'echarts';
import { IdeaSlimDto } from 'src/app/models/slimDto/ideaSlimDto';

@Component({
  selector: 'app-user-idea-list',
  templateUrl: './user-idea-list.component.html',
  styleUrls: ['./user-idea-list.component.scss'],
})
export class UserIdeaListComponent implements OnInit {
  constructor() {}

  @Input() ideas: IdeaSlimDto[] = [];

  ngOnInit(): void {}
}
