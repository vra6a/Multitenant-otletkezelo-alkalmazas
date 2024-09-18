import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { IdeaDto } from 'src/app/models/dto/ideaDto';
import { ScoreSheetDto } from 'src/app/models/dto/scoreScheetDto';
import { UserSlimDto } from 'src/app/models/slimDto/userSlimDto';
import { WebResponse } from 'src/app/models/webResponse';
import { AuthService } from 'src/app/services/auth/auth.service';
import { IdeaService } from 'src/app/services/idea.service';
import { ScoreSheetService } from 'src/app/services/scoreSheet.service';
import { SnackBarService } from 'src/app/services/snackBar.service';
import Utils from 'src/app/utility/utils';

@UntilDestroy()
@Component({
  selector: 'app-box',
  templateUrl: './idea.component.html',
  styleUrls: ['./idea.component.scss'],
})
export class IdeaComponent implements OnInit {
  constructor(
    private route: ActivatedRoute,
    private snackBar: SnackBarService,
    private auth: AuthService,
    private ideaService: IdeaService,
    private scoreSheetService: ScoreSheetService,
  ) {}

  idea: IdeaDto = null;
  scoreSheets: ScoreSheetDto[] = []
  id: string = '';
  likeText: string = '';
  isLiked: boolean = false;
  user: UserSlimDto = null;
  canEdit: boolean = true;

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id');
    this.user = this.auth.getCurrentUser();
    this.getIdea();
  }

  likeAction() {
    if (this.isLiked) {
      this.ideaService
        .dislikeComment$(this.id.toString())
        .pipe(untilDestroyed(this))
        .subscribe((res: WebResponse<string>) => {
          if (res.code == 200) {
            this.snackBar.ok(res.message);
            this.getIdea();
          } else {
            this.snackBar.error(res.message);
          }
        });
    } else {
      this.ideaService
        .likeComment$(this.id.toString())
        .pipe(untilDestroyed(this))
        .subscribe((res: WebResponse<string>) => {
          if (res.code == 200) {
            this.snackBar.ok(res.message);
            this.getIdea();
          } else {
            this.snackBar.error(res.message);
          }
        });
    }
  }

  private getIdea() {
    this.ideaService
      .getIdea$(this.id)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaDto>) => {
        if (res.code == 200) {
          this.idea = res.data;
          this.getScoreSheets()
          this.createLikeText();
          this.checkIfLiked();
          this.checkCanEdit();
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  private createLikeText() {
    this.likeText = Utils.createLikeText(this.idea.likes, this.user.id);
  }

  private getScoreSheets() {
    this.scoreSheetService
      .getScoreSheetsByIdeaId$(this.idea.id)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<ScoreSheetDto[]>) =>{
        this.scoreSheets = res.data
        this.scoreSheets.shift()
      })
  }

  private checkIfLiked() {
    this.isLiked = false;
    this.idea.likes.forEach((user) => {
      if (user.id == this.user.id) {
        this.isLiked = true;
      }
    });
  }

  private checkCanEdit() {
    if (this.user.role == 'ADMIN') {
      this.canEdit = true;
    } else if (this.user.id == this.idea.owner.id) {
      this.canEdit = true;
    } else {
      this.canEdit = false;
    }
  }
}
