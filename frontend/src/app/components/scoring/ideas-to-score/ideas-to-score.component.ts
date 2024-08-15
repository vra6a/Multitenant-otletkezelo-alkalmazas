import { STEPPER_GLOBAL_OPTIONS } from '@angular/cdk/stepper';
import { FlatTreeControl, NestedTreeControl } from '@angular/cdk/tree';
import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatTreeFlatDataSource, MatTreeFlattener, MatTreeNestedDataSource } from '@angular/material/tree';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { IdeaBoxDto } from 'src/app/models/dto/ideaBoxDto';
import { IdeaDto } from 'src/app/models/dto/ideaDto';
import { ScoreSheetDto } from 'src/app/models/dto/scoreScheetDto';
import { BulkIdeaDto } from 'src/app/models/dto/utility/BulkIdeaDto';
import { IdeaBoxSlimDto } from 'src/app/models/slimDto/ideaBoxSlimDto';
import { IdeaSlimDto } from 'src/app/models/slimDto/ideaSlimDto';
import { UserSlimDto } from 'src/app/models/slimDto/userSlimDto';
import { WebResponse } from 'src/app/models/webResponse';
import { AuthService } from 'src/app/services/auth/auth.service';
import { IdeaService } from 'src/app/services/idea.service';
import { IdeaBoxService } from 'src/app/services/ideaBox.service';
import { ScoreSheetService } from 'src/app/services/scoreSheet.service';
import { SnackBarService } from 'src/app/services/snackBar.service';

@UntilDestroy()
@Component({
  selector: 'app-ideas-to-score',
  templateUrl: './ideas-to-score.component.html',
  styleUrls: ['./ideas-to-score.component.scss'],
  providers: [
    {
      provide: STEPPER_GLOBAL_OPTIONS,
      useValue: {displayDefaultIndicatorType: false},
    },
  ],
})
export class IdeasToScoreComponent implements OnInit {
  constructor(
    private ideaService: IdeaService,
    private authService: AuthService,
    private ideaBoxService: IdeaBoxService,
    private scoreSheetService: ScoreSheetService,
    private snackBar: SnackBarService,
  ) {}

  ideasToScore: BulkIdeaDto[] = [];
  scoreSheet: ScoreSheetDto = null;
  selectedIdea: IdeaDto = null;
  currentUser: UserSlimDto = null;
  invalid = this.scoreSheet?.scores.find((item) => item.score == null);


  ngOnInit(): void {
    this.ideaService
      .getBulkIdeas$()
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<BulkIdeaDto[]>) => {
        this.ideasToScore = res.data;
      });
    this.currentUser = this.authService.getCurrentUser();

  }

  ideaClicked(idea: IdeaSlimDto): void {
    this.ideaService.getIdea$(idea.id.toString())
    .pipe(untilDestroyed(this))
    .subscribe((res: WebResponse<IdeaDto>) => {
      this.selectedIdea = res.data
      console.log(this.selectedIdea)

      this.ideaBoxService
          .getIdeaBox$(this.selectedIdea.id.toString())
          .pipe(untilDestroyed(this))
          .subscribe((res: WebResponse<IdeaBoxDto>) => {
            this.scoreSheetService
              .getScoreSheetById$(res.data.scoreSheetTemplates[0].id.toString())
              .pipe(untilDestroyed(this))
              .subscribe((res: WebResponse<ScoreSheetDto>) => {
                console.log(res);
                this.scoreSheet = res.data;
                this.scoreSheet.owner = this.currentUser;
              });
          });
    })
  }

  saveItem(event: number, id: number) {
    this.scoreSheet.scores.find((item) => item.id == id).score = event;
    this.invalid = this.scoreSheet.scores.find((item) => item.score == null);
  }

  save() {
    if (!this.invalid) {
      this.scoreSheet.idea = this.selectedIdea;
      console.log(this.scoreSheet);
      this.scoreSheetService
        .saveScoreSheet(this.scoreSheet)
        .pipe(untilDestroyed(this))
        .subscribe((res: WebResponse<ScoreSheetDto>) => {
          console.log(res);
          if (res.code == 200) {
            this.snackBar.ok(res.message);
          } else {
            this.snackBar.error(res.message);
          }
        });
    }
  }

}
