import { Component, Input, OnInit } from '@angular/core';
import { ScoreSlimDto } from 'src/app/models/slimDto/scoreSlimDto';

@Component({
  selector: 'app-star-score',
  templateUrl: './star-score.component.html',
  styleUrls: ['./star-score.component.scss'],
})
export class StarScoreComponent implements OnInit {
  constructor() {}

  @Input() score: ScoreSlimDto = null;

  ngOnInit(): void {}
}
