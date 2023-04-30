import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from './auth/auth.service';
import { environment } from 'src/environments/environment';
import { WebResponse } from '../models/webResponse';
import { Observable } from 'rxjs';
import { CommentDto } from '../models/dto/commentDto';

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  constructor(private http: HttpClient, private auth: AuthService) {}

  apiUrl = `${environment.apiUrl}`;

  getComment$(id: string): Observable<WebResponse<CommentDto>> {
    return this.http.get<WebResponse<CommentDto>>(
      `${this.apiUrl}/comment/${id}`
    );
  }

  getCommentsByIdea$(id: string): Observable<WebResponse<CommentDto[]>> {
    return this.http.get<WebResponse<CommentDto[]>>(
      `${this.apiUrl}/comments/${id}`
    );
  }

  createComment$(comment: CommentDto): Observable<WebResponse<CommentDto>> {
    return this.http.post<WebResponse<CommentDto>>(
      `${this.apiUrl}/comment`,
      comment
    );
  }

  editComent$(comment: CommentDto): Observable<WebResponse<CommentDto>> {
    return this.http.post<WebResponse<CommentDto>>(
      `${this.apiUrl}/comment/${comment.id}`,
      comment
    );
  }

  likeComment$(id: string): Observable<WebResponse<string>> {
    return this.http.post<WebResponse<string>>(
      `${this.apiUrl}/comment/${id}/like`,
      {}
    );
  }

  dislikeComment$(id: string): Observable<WebResponse<string>> {
    return this.http.post<WebResponse<string>>(
      `${this.apiUrl}/comment/${id}/dislike`,
      {}
    );
  }
}
