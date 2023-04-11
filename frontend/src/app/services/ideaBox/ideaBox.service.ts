import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IdeaBoxListView } from 'src/app/models/ideaBoxListView';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class IdeaBoxService {
  constructor(private http: HttpClient) {}

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
}
