import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IdeaBox } from 'src/app/models/ideaBox';
import { IdeaBoxListView } from 'src/app/models/ideaBoxListView';
import { environment } from 'src/environments/environment';
import { AuthService } from './auth/auth.service';

@Injectable({
  providedIn: 'root',
})
export class IdeaBoxService {
  constructor(private http: HttpClient, private auth: AuthService) {}

  apiUrl = `${environment.apiUrl}`;

  getIdeaBoxListView$(
    s: string,
    sort: string,
    page: number,
    items: number
  ): Observable<IdeaBoxListView[]> {
    return this.http.get<IdeaBoxListView[]>(`${this.apiUrl}/idea-box`, {
      params: {
        s: s,
        sort: sort,
        page: page,
        items: items,
      },
    });
  }

  getIdeaBoxListCount$(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/idea-box-count`);
  }

  createIdeaBox(ideaBox: IdeaBox) {
    let ib = ideaBox;
    let currentUser = this.auth.currentUser;
    ib = { ...ib, creator: currentUser };
    return this.http.post(`${this.apiUrl}/idea-box`, ib);
  }
}
