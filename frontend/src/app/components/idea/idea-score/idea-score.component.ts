import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, Validators } from '@angular/forms';
import { MatSliderChange } from '@angular/material/slider';
import { ActivatedRoute } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { IdeaDto } from 'src/app/models/dto/ideaDto';
import { ScoreDto } from 'src/app/models/dto/scoreDto';
import { ScoreSlimDto } from 'src/app/models/slimDto/scoreSlimDto';
import { UserSlimDto } from 'src/app/models/slimDto/userSlimDto';
import { WebResponse } from 'src/app/models/webResponse';
import { AuthService } from 'src/app/services/auth/auth.service';
import { IdeaService } from 'src/app/services/idea.service';
import { SnackBarService } from 'src/app/services/snackBar.service';

@UntilDestroy()
@Component({
  selector: 'app-idea-score',
  templateUrl: './idea-score.component.html',
  styleUrls: ['./idea-score.component.scss'],
})
export class IdeaScoreComponent implements OnInit {
  constructor(
    private route: ActivatedRoute,
    private fb: UntypedFormBuilder,
    private ideaService: IdeaService,
    private snackBar: SnackBarService,
    private auth: AuthService
  ) {}

  currentUser: UserSlimDto = null;

  idea: IdeaDto = null;
  starScores: ScoreDto[] = [];
  sliderScores: ScoreDto[] = [];

  starCount: number = 5;
  starRatingArr = [];
  starRating: number = 3;

  sliderValue = 5;

  starForm = this.fb.group({
    title: ['', Validators.required],
  });
  sliderForm = this.fb.group({
    title: ['', Validators.required],
  });

  ngOnInit(): void {
    this.currentUser = this.auth.getCurrentUser();
    this.getIdea();
    for (let index = 0; index < this.starCount; index++) {
      this.starRatingArr.push(index);
    }
  }

  onClick(rating: number) {
    this.starRating = rating;
    return false;
  }

  showIcon(index: number) {
    if (this.starRating >= index + 1) {
      return 'star';
    } else {
      return 'star_border';
    }
  }

  sliderChange(e: number) {
    this.sliderValue = e;
  }

  addStarRating() {
    let score: ScoreDto = {
      id: null,
      score: this.starRating,
      type: 'STAR',
      idea: this.idea,
      title: this.starForm.controls['title'].value,
      owner: this.currentUser,
    };
    this.ideaService
      .addScore$(this.idea.id, score)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<ScoreDto>) => {
        if (res.code == 200) {
          this.snackBar.ok(res.message);
          this.getIdea();
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  addSliderRating() {
    let score: ScoreDto = {
      id: null,
      score: this.sliderValue,
      type: 'SLIDER',
      idea: this.idea,
      title: this.sliderForm.controls['title'].value,
      owner: this.currentUser,
    };
    this.ideaService
      .addScore$(this.idea.id, score)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<ScoreDto>) => {
        if (res.code == 200) {
          this.snackBar.ok(res.message);
          this.getIdea();
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  private getIdea() {
    let id = this.route.snapshot.paramMap.get('id');
    console.log(id);
    this.ideaService
      .getIdea$(id)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaDto>) => {
        if (res.code == 200) {
          this.idea = res.data;
          this.distributeScores();
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  private distributeScores() {
    this.idea.score.forEach((score: ScoreSlimDto) => {
      switch (score.type) {
        case 'STAR':
          this.starScores.push(score);
          break;
        case 'SLIDER':
          this.sliderScores.push(score);
          break;
      }
    });
    console.log(this.starScores);
    console.log(this.sliderScores);
  }
}
