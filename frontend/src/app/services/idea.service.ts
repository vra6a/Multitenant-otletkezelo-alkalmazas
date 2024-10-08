import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from './auth/auth.service';
import { environment } from 'src/environments/environment';
import { IdeaDto } from '../models/dto/ideaDto';
import { Observable } from 'rxjs';
import { WebResponse } from '../models/webResponse';
import { IdeaSlimDto } from '../models/slimDto/ideaSlimDto';
import { ScoreDto } from '../models/dto/scoreDto';
import { UserSlimDto } from '../models/slimDto/userSlimDto';
import { BulkIdeaDto } from '../models/dto/utility/BulkIdeaDto';

@Injectable({
  providedIn: 'root',
})
export class IdeaService {
  constructor(private http: HttpClient, private auth: AuthService) {}

  apiUrl = `${environment.apiUrl}`;

  getIdea$(id: string): Observable<WebResponse<IdeaDto>> {
    return this.http.get<WebResponse<IdeaDto>>(`${this.apiUrl}/idea/${id}`);
  }

  getIdeaSlim$(id: string): Observable<WebResponse<IdeaSlimDto>> {
    return this.http.get<WebResponse<IdeaSlimDto>>(
      `${this.apiUrl}/idea/slim/${id}`
    );
  }

  getReviewedIdeas(): Observable<WebResponse<IdeaSlimDto[]>> {
    return this.http.get<WebResponse<IdeaSlimDto[]>>(
      `${this.apiUrl}/ideas/reviewed`
    );
  }

  createIdea$(idea: IdeaDto): Observable<WebResponse<IdeaDto>> {
    return this.http.post<WebResponse<IdeaDto>>(`${this.apiUrl}/idea`, idea);
  }

  editIdea$(id: string, idea: IdeaDto) {
    return this.http.put<WebResponse<IdeaDto>>(
      `${this.apiUrl}/idea/${id}`,
      idea
    );
  }

  deleteIdea$(id: number): Observable<WebResponse<string>> {
    return this.http.delete<WebResponse<string>>(`${this.apiUrl}/idea/${id}`);
  }

  likeComment$(id: string): Observable<WebResponse<string>> {
    return this.http.post<WebResponse<string>>(
      `${this.apiUrl}/idea/${id}/like`,
      {}
    );
  }

  getDefaultJuries$(id: string): Observable<WebResponse<UserSlimDto[]>> {
    return this.http.get<WebResponse<UserSlimDto[]>>(
      `${this.apiUrl}/idea/${id}/juries`
    );
  }

  dislikeComment$(id: string): Observable<WebResponse<string>> {
    return this.http.post<WebResponse<string>>(
      `${this.apiUrl}/idea/${id}/dislike`,
      {}
    );
  }

  addScore$(id: number, score: ScoreDto): Observable<WebResponse<ScoreDto>> {
    return this.http.post<WebResponse<ScoreDto>>(
      `${this.apiUrl}/idea/${id}/score`,
      score
    );
  }

  removeScore$(id: number, score: ScoreDto): Observable<WebResponse<string>> {
    return this.http.delete<WebResponse<string>>(
      `${this.apiUrl}/idea/${id}/score`
    );
  }

  getIdeasToScore$(): Observable<WebResponse<IdeaDto[]>> {
    return this.http.get<WebResponse<IdeaDto[]>>(
      `${this.apiUrl}/idea/ideasToScore`
    );
  }

  getScoredIdeas$(): Observable<WebResponse<IdeaDto[]>> {
    return this.http.get<WebResponse<IdeaDto[]>>(
      `${this.apiUrl}/idea/scoredIdeas`
    );
  }

  getBulkIdeas$(): Observable<WebResponse<BulkIdeaDto[]>> {
    return this.http.get<WebResponse<BulkIdeaDto[]>>(
      `${this.apiUrl}/score/getIdeas`
    )
  }

  approveIdea$(id: number): Observable<WebResponse<IdeaDto>> {
    return this.http.post<WebResponse<IdeaDto>>(
      `${this.apiUrl}/idea/${id}/approve`, {}
    )
  }

  denyIdea$(id: number): Observable<WebResponse<IdeaDto>> {
    return this.http.post<WebResponse<IdeaDto>>(
      `${this.apiUrl}/idea/${id}/deny`, {}
    )
  }
}
