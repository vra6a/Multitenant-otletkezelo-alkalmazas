import { Component, Input, OnInit } from '@angular/core';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { CommentDto } from 'src/app/models/dto/commentDto';
import { UserSlimDto } from 'src/app/models/slimDto/userSlimDto';
import { WebResponse } from 'src/app/models/webResponse';
import { AuthService } from 'src/app/services/auth/auth.service';
import { CommentService } from 'src/app/services/comment.service';
import { SnackBarService } from 'src/app/services/snackBar.service';
import Utils from 'src/app/utility/utils';

@UntilDestroy()
@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.scss'],
})
export class CommentComponent implements OnInit {
  constructor(
    private commentService: CommentService,
    private snackBar: SnackBarService,
    private auth: AuthService
  ) {}

  @Input() id: number;
  user: UserSlimDto = null;
  likeText: string = '';
  comment: CommentDto = null;
  isLiked: boolean = false;
  canEdit: boolean = false;
  isEditing: boolean = false;
  EditedText: string = '';

  ngOnInit(): void {
    this.user = this.auth.getCurrentUser();
    this.getComment();
  }

  likeAction() {
    if (this.isLiked) {
      this.commentService
        .dislikeComment$(this.id.toString())
        .pipe(untilDestroyed(this))
        .subscribe((res: WebResponse<string>) => {
          if (res.code == 200) {
            this.snackBar.ok(res.message);
            this.getComment();
          } else {
            this.snackBar.error(res.message);
          }
        });
    } else {
      this.commentService
        .likeComment$(this.id.toString())
        .pipe(untilDestroyed(this))
        .subscribe((res: WebResponse<string>) => {
          if (res.code == 200) {
            this.snackBar.ok(res.message);
            this.getComment();
          } else {
            this.snackBar.error(res.message);
          }
        });
    }
  }

  edit() {
    this.isEditing = !this.isEditing;
  }

  save() {
    this.comment.text = this.EditedText;
    this.commentService
      .editComent$(this.comment)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<CommentDto>) => {
        if (res.code == 200) {
          this.snackBar.ok(res.message);
          this.getComment();
          this.isEditing = false;
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  private createLikeText() {
    this.likeText = Utils.createLikeText(this.comment.likes, this.user.id);
  }

  private getComment() {
    this.commentService
      .getComment$(this.id.toString())
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<CommentDto>) => {
        if (res.code == 200) {
          this.comment = res.data;
          this.createLikeText();
          this.checkIfLiked();
          this.checkCanEdit();
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  private checkIfLiked() {
    this.isLiked = false;
    this.comment.likes.forEach((user) => {
      if (user.id == this.user.id) {
        this.isLiked = true;
      }
    });
  }

  private checkCanEdit() {
    if (this.user.role == 'ADMIN') {
      this.canEdit = true;
    } else if (this.user.id == this.comment.owner.id) {
      this.canEdit = true;
    } else {
      this.canEdit = false;
    }
  }
}
