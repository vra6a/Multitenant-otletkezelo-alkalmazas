import { Component, OnInit } from '@angular/core';
import { FormControl, UntypedFormBuilder, Validators } from '@angular/forms';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { IdeaBoxDto } from 'src/app/models/dto/ideaBoxDto';
import { ScoreItemDto } from 'src/app/models/dto/scoreItemDto';
import { ScoreSheetDto } from 'src/app/models/dto/scoreScheetDto';
import { IdeaBoxSlimDto } from 'src/app/models/slimDto/ideaBoxSlimDto';
import { WebResponse } from 'src/app/models/webResponse';
import { AuthService } from 'src/app/services/auth/auth.service';
import { IdeaBoxService } from 'src/app/services/ideaBox.service';
import { ScoreSheetService } from 'src/app/services/scoreSheet.service';
import { SnackBarService } from 'src/app/services/snackBar.service';
import { UserService } from 'src/app/services/user.service';

@UntilDestroy()
@Component({
  selector: 'app-create-score-sheet',
  templateUrl: './create-score-sheet.component.html',
  styleUrls: ['./create-score-sheet.component.scss'],
})
export class CreateScoreSheetComponent implements OnInit {
  constructor(
    private fb: UntypedFormBuilder,
    private ideaBoxService: IdeaBoxService,
    private authService: AuthService,
    private scoreSheetService: ScoreSheetService,
    private snackBar: SnackBarService
  ) {}

  ngOnInit(): void {
    this.getDraftIdeaBoxes();
  }

  scoreItems: ScoreItemDto[] = [];
  draftIdeaBoxes: IdeaBoxDto[] = [];

  selectedDraftIdeaBoxForm = this.fb.group({
    selectedDraftIdeaBox: ['', Validators.required],
  });

  addNewScoreItem(event: ScoreItemDto) {
    this.scoreItems.push(event);
  }

  assignScoreSheet() {
    let scoreSheet: ScoreSheetDto = {};
    scoreSheet.templateFor = this.draftIdeaBoxes.find(
      (i) => i.id == this.selectedDraftIdeaBoxForm.value.selectedDraftIdeaBox
    );
    scoreSheet.owner = this.authService.getCurrentUser();
    this.ideaBoxService
      .assignScoreSheetTemplate$(scoreSheet)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<ScoreSheetDto>) => {
        console.log(res.data);
        let ss = res.data;
        this.scoreItems.forEach((item) => {
          item.scoreSheet = ss;
        });
        this.scoreSheetService
          .createScoreItems$(this.scoreItems, res.data.id)
          .pipe(untilDestroyed(this))
          .subscribe((res: WebResponse<String>) => {
            console.log(res.data);
            if (res.code == 200) {
              this.snackBar.ok(res.message);
            } else {
              this.snackBar.error(res.message);
            }
          });
      });
  }

  private getDraftIdeaBoxes() {
    this.ideaBoxService
      .getAllIdeaBoxes$()
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaBoxSlimDto[]>): void => {
        res.data.forEach((ideabox) => {
          if (ideabox.draft) {
            this.draftIdeaBoxes.push(ideabox);
          }
        });
      });
  }
}
