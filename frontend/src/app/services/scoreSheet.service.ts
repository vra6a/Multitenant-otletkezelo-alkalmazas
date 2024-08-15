import { Injectable } from '@angular/core';
import { AuthService } from './auth/auth.service';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { ScoreItemDto } from '../models/dto/scoreItemDto';
import { WebResponse } from '../models/webResponse';
import { Observable } from 'rxjs';
import { IdeaBoxSlimDto } from '../models/slimDto/ideaBoxSlimDto';
import { ScoreSheetDto } from '../models/dto/scoreScheetDto';

@Injectable({
  providedIn: 'root',
})
export class ScoreSheetService {
  constructor(private http: HttpClient, private auth: AuthService) {}

  apiUrl = `${environment.apiUrl}`;

  addScoreTemplateItem$(
    id: number,
    item: ScoreItemDto
  ): Observable<WebResponse<String>> {
    return this.http.post<WebResponse<String>>(
      `${this.apiUrl}/scoreSheet/` + id,
      item
    );
  }

  createScoreItems$(
    items: ScoreItemDto[],
    id: number
  ): Observable<WebResponse<String>> {
    return this.http.post<WebResponse<String>>(
      `${this.apiUrl}/scoreSheet/create/` + id,
      items
    );
  }

  getScoreSheetById$(id: string): Observable<WebResponse<ScoreSheetDto>> {
    return this.http.get<WebResponse<ScoreSheetDto>>(
      `${this.apiUrl}/scoreSheet/` + id
    );
  }

  getScoreSheetsByIdeaId$(
    id: number
  ): Observable<WebResponse<ScoreSheetDto[]>> {
    return this.http.get<WebResponse<ScoreSheetDto[]>>(
      `${this.apiUrl}/idea/` + id + `/scoreSheets`
    );
  }

  saveScoreSheet$(ss: ScoreSheetDto): Observable<WebResponse<ScoreSheetDto>> {
    console.log(ss);
    return this.http.post<WebResponse<ScoreSheetDto>>(
      `${this.apiUrl}/scoreSheet/` + ss.id + `/save`,
      ss
    );
  }

  getScoredIdeaBoxes$(): Observable<WebResponse<IdeaBoxSlimDto[]>> {
    return this.http.get<WebResponse<IdeaBoxSlimDto[]>>(
      `${this.apiUrl}/score/getScoredIdeaBoxes`
    );
  }

}
