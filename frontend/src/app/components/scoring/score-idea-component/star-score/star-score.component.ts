import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ScoreItemDto } from 'src/app/models/dto/scoreItemDto';

@Component({
  selector: 'app-star-score',
  templateUrl: './star-score.component.html',
  styleUrls: ['./star-score.component.scss'],
})
export class StarScoreComponent implements OnInit {
  constructor() {
    this.arr = [1, 2, 3, 4, 5];
  }

  @Input() item: ScoreItemDto = null;
  @Input() scored: boolean = false;
  @Output() save: EventEmitter<number> = new EventEmitter<number>();

  arr: any[] = [];
  index: number = -1;

  ngOnInit(): void {}

  onClickItem(index) {
    this.index = index;
  }

  saveItem() {
    this.save.emit(this.index + 1);
  }
}
