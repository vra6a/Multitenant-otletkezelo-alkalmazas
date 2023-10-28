import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, UntypedFormBuilder, Validators } from '@angular/forms';
import { ScoreItemDto } from 'src/app/models/dto/scoreItemDto';

@Component({
  selector: 'app-score-item-form',
  templateUrl: './score-item-form.component.html',
  styleUrls: ['./score-item-form.component.scss'],
})
export class ScoreItemFormComponent implements OnInit {
  constructor(private fb: UntypedFormBuilder) {}

  @Output() newScoreItem = new EventEmitter<ScoreItemDto>();

  ItemTypeForm = new FormControl('', Validators.required);

  scoreItemForm = this.fb.group({
    type: ['', Validators.required],
    title: ['', Validators.required],
  });

  ngOnInit(): void {}

  addScoreItem() {
    if (this.scoreItemForm.valid) {
      let scoreItem: ScoreItemDto;
      scoreItem = this.scoreItemForm.value;
      this.scoreItemForm.reset();
      console.log(scoreItem);
      this.newScoreItem.emit(scoreItem);
    }
  }
}
