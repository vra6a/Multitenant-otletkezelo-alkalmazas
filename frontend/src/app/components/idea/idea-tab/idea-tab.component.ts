import { Component, Input, OnInit } from '@angular/core';
import { ScoreSlimDto } from 'src/app/models/slimDto/scoreSlimDto';

@Component({
  selector: 'app-idea-tab',
  templateUrl: './idea-tab.component.html',
  styleUrls: ['./idea-tab.component.scss'],
})
export class IdeaTabComponent implements OnInit {
  constructor() {}

  @Input() description: string;
  @Input() scores: ScoreSlimDto[];

  ngOnInit(): void {}
}
