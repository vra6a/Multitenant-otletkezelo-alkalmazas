import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IdeaBoxDto } from 'src/app/models/dto/ideaBoxDto';
import { environment } from 'src/environments/environment';
import { AuthService } from './auth/auth.service';
import { WebResponse } from '../models/webResponse';
import { IdeaBoxSlimDto } from '../models/slimDto/ideaBoxSlimDto';

@Injectable({
  providedIn: 'root',
})
export class IdeaBoxService {
  constructor(private http: HttpClient, private auth: AuthService) {}

  apiUrl = `${environment.apiUrl}`;

  getIdeaBoxes$(
    s: string,
    sort: string,
    page: number,
    items: number
  ): Observable<WebResponse<IdeaBoxSlimDto[]>> {
    return this.http.get<WebResponse<IdeaBoxSlimDto[]>>(
      `${this.apiUrl}/idea-box`,
      {
        params: {
          s: s,
          sort: sort,
          page: page,
          items: items,
        },
      }
    );
  }

  getIdeaBox$(id: string): Observable<WebResponse<IdeaBoxDto>> {
    return this.http.get<WebResponse<IdeaBoxDto>>(
      `${this.apiUrl}/idea-box/` + id
    );
  }

  getIdeaBoxSlim$(id: string): Observable<WebResponse<IdeaBoxSlimDto>> {
    return this.http.get<WebResponse<IdeaBoxDto>>(
      `${this.apiUrl}/idea-box/slim/` + id
    );
  }

  getIdeaBoxListCount$(): Observable<WebResponse<number>> {
    return this.http.get<WebResponse<number>>(`${this.apiUrl}/idea-box-count`);
  }

  createIdeaBox$(ideaBox: IdeaBoxDto): Observable<WebResponse<IdeaBoxDto>> {
    return this.http.post<WebResponse<IdeaBoxDto>>(
      `${this.apiUrl}/idea-box`,
      ideaBox
    );
  }

  editIdeaBox$(
    id: string,
    ideaBox: IdeaBoxDto
  ): Observable<WebResponse<IdeaBoxDto>> {
    return this.http.put<WebResponse<IdeaBoxDto>>(
      `${this.apiUrl}/idea-box/${id}`,
      ideaBox
    );
  }

  deleteIdeaBox$(id: number): Observable<WebResponse<string>> {
    return this.http.delete<WebResponse<string>>(
      `${this.apiUrl}/idea-box/${id.toString()}`
    );
  }
}
