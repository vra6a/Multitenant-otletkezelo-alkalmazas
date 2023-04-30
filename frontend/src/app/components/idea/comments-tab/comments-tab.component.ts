import { Component, Input, OnInit } from '@angular/core';
import { UntypedFormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { CommentDto } from 'src/app/models/dto/commentDto';
import { IdeaDto } from 'src/app/models/dto/ideaDto';
import { CommentSlimDto } from 'src/app/models/slimDto/commentSlimDto';
import { IdeaSlimDto } from 'src/app/models/slimDto/ideaSlimDto';
import { UserSlimDto } from 'src/app/models/slimDto/userSlimDto';
import { WebResponse } from 'src/app/models/webResponse';
import { AuthService } from 'src/app/services/auth/auth.service';
import { CommentService } from 'src/app/services/comment.service';
import { IdeaService } from 'src/app/services/idea.service';
import { SnackBarService } from 'src/app/services/snackBar.service';

@UntilDestroy()
@Component({
  selector: 'app-comments-tab',
  templateUrl: './comments-tab.component.html',
  styleUrls: ['./comments-tab.component.scss'],
})
export class CommentsTabComponent implements OnInit {
  constructor(
    private fb: UntypedFormBuilder,
    private commentService: CommentService,
    private ideaService: IdeaService,
    private auth: AuthService,
    private route: ActivatedRoute,
    private snackBar: SnackBarService
  ) {}

  user: UserSlimDto = null;
  idea: IdeaSlimDto = null;
  id: string = '';

  @Input() comments: CommentSlimDto[] = [];

  commentForm = this.fb.group({
    text: ['', Validators.required],
  });

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id');
    this.user = this.auth.getCurrentUser();
    this.ideaService
      .getIdeaSlim$(this.id)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaSlimDto>) => {
        if (res.code == 200) {
          this.idea = res.data;
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  comment() {
    let comment = this.commentForm.value;
    comment.creationDate = '';
    comment.owner = this.user;
    comment.idea = this.idea;
    comment.likes = [];

    this.commentService
      .createComment$(comment)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<CommentDto>) => {
        if (res.code == 200) {
          this.snackBar.ok(res.message);
          this.refreshComments();
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  refreshComments() {
    this.commentService
      .getCommentsByIdea$(this.id)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<CommentSlimDto[]>) => {
        if (res.code == 200) {
          this.comments = res.data;
        } else {
          this.snackBar.error(res.message);
        }
      });
  }
}
