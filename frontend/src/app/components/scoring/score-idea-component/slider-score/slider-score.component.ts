import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ScoreItemDto } from 'src/app/models/dto/scoreItemDto';

@Component({
  selector: 'app-slider-score',
  templateUrl: './slider-score.component.html',
  styleUrls: ['./slider-score.component.scss'],
})
export class SliderScoreComponent implements OnInit {
  constructor() {}

  @Input() item: ScoreItemDto = null;
  @Input() scored: boolean = false;
  @Output() save: EventEmitter<number> = new EventEmitter<number>();

  sliderValue = 1;

  ngOnInit(): void {}

  sliderChange(e: number) {
    this.sliderValue = e;
  }

  saveItem() {
    this.save.emit(this.sliderValue);
  }
}
