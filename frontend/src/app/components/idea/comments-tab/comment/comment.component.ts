import { Component, Input, OnInit } from '@angular/core';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { CommentDto } from 'src/app/models/dto/commentDto';
import { UserSlimDto } from 'src/app/models/slimDto/userSlimDto';
import { WebResponse } from 'src/app/models/webResponse';
import { AuthService } from 'src/app/services/auth/auth.service';
import { CommentService } from 'src/app/services/comment.service';
import { SnackBarService } from 'src/app/services/snackBar.service';

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

  private createLikeText() {
    let likes = this.comment?.likes?.length;
    let lastLike = null;

    if (likes == 0) {
      lastLike = null;
    } else {
      lastLike = this.comment.likes.at(likes - 1);
    }

    switch (likes) {
      case 0:
        this.likeText = 'No one liked this comment';
        break;
      case 1:
        if (lastLike?.id == this.user.id) {
          this.likeText = 'You liked this';
        } else {
          this.likeText =
            lastLike?.firstName + ' ' + lastLike?.lastName + ' liked this';
        }
        break;
      default:
        if (lastLike?.id == this.user.id) {
          this.likeText = 'You and ' + (likes - 1) + ' other liked this';
        } else {
          this.likeText =
            lastLike?.firstName +
            ' ' +
            lastLike?.lastName +
            ' and ' +
            (likes - 1) +
            ' other liked this';
        }
        this.likeText =
          lastLike?.firstName +
          ' ' +
          lastLike?.lastName +
          ' and ' +
          (likes - 1) +
          ' other liked this';
    }
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
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  private checkIfLiked() {
    this.comment.likes.forEach((user) => {
      if (user.id == this.user.id) {
        this.isLiked = true;
      }
    });
  }
}
