import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from './auth/auth.service';
import { environment } from 'src/environments/environment';
import { IdeaDto } from '../models/dto/ideaDto';
import { Observable } from 'rxjs';
import { WebResponse } from '../models/webResponse';
import { IdeaSlimDto } from '../models/slimDto/ideaSlimDto';

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

  createIdea$(idea: IdeaDto): Observable<WebResponse<IdeaDto>> {
    return this.http.post<WebResponse<IdeaDto>>(`${this.apiUrl}/idea`, idea);
  }

  likeComment$(id: string): Observable<WebResponse<string>> {
    return this.http.post<WebResponse<string>>(
      `${this.apiUrl}/idea/${id}/like`,
      {}
    );
  }

  dislikeComment$(id: string): Observable<WebResponse<string>> {
    return this.http.post<WebResponse<string>>(
      `${this.apiUrl}/idea/${id}/dislike`,
      {}
    );
  }
}
