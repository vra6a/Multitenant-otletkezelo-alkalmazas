import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IdeaBox } from 'src/app/models/ideaBox';
import { IdeaBoxListView } from 'src/app/models/ideaBoxListView';
import { environment } from 'src/environments/environment';
import { AuthService } from './auth/auth.service';
import { WebResponse } from '../models/webResponse';
import { Idea } from '../models/idea';

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
  ): Observable<WebResponse<IdeaBoxListView[]>> {
    return this.http.get<WebResponse<IdeaBoxListView[]>>(
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

  getIdeaBox$(id: string): Observable<WebResponse<IdeaBox>> {
    return this.http.get<WebResponse<IdeaBox>>(`${this.apiUrl}/idea-box/` + id);
  }

  getIdeaBoxListCount$(): Observable<WebResponse<number>> {
    return this.http.get<WebResponse<number>>(`${this.apiUrl}/idea-box-count`);
  }

  createIdeaBox$(ideaBox: IdeaBox): Observable<WebResponse<IdeaBox>> {
    let ib = ideaBox;
    let currentUser = this.auth.currentUser;
    ib = { ...ib, creator: currentUser };
    return this.http.post<WebResponse<IdeaBox>>(`${this.apiUrl}/idea-box`, ib);
  }
}
