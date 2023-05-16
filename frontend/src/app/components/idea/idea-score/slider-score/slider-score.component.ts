import { Component, Input, OnInit } from '@angular/core';
import { ScoreSlimDto } from 'src/app/models/slimDto/scoreSlimDto';

@Component({
  selector: 'app-slider-score',
  templateUrl: './slider-score.component.html',
  styleUrls: ['./slider-score.component.scss'],
})
export class SliderScoreComponent implements OnInit {
  constructor() {}

  @Input() score: ScoreSlimDto = null;

  ngOnInit(): void {}
}
