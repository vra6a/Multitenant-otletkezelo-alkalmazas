import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IdeaBoxDto } from 'src/app/models/dto/ideaBoxDto';
import { environment } from 'src/environments/environment';
import { AuthService } from './auth/auth.service';
import { WebResponse } from '../models/webResponse';
import { IdeaBoxSlimDto } from '../models/slimDto/ideaBoxSlimDto';
import { ScoreSheetDto } from '../models/dto/scoreScheetDto';
import { IdeaScoreSheets } from '../models/dto/utility/ideaScoreSheets';

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

  getAllIdeaBoxes$(): Observable<WebResponse<IdeaBoxSlimDto[]>> {
    return this.http.get<WebResponse<IdeaBoxSlimDto[]>>(
      `${this.apiUrl}/idea-box`
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

  getScoredIdeaCountByIdeaBox$(id: string): Observable<WebResponse<number>> {
    return this.http.get<WebResponse<number>>(`${this.apiUrl}/idea-box/` + id + `/scored`)
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

  assignScoreSheetTemplate$(
    scoreSheet: ScoreSheetDto
  ): Observable<WebResponse<ScoreSheetDto>> {
    return this.http.post<WebResponse<ScoreSheetDto>>(
      `${this.apiUrl}/idea-box/${scoreSheet.templateFor.id}/createScoreSheetTemplate`,
      scoreSheet
    );
  }

  deleteIdeaBox$(id: number): Observable<WebResponse<string>> {
    return this.http.delete<WebResponse<string>>(
      `${this.apiUrl}/idea-box/${id.toString()}`
    );
  }

  checkIfIdeaBoxHasAllRequiredScoreSheets$(id: number): Observable<WebResponse<Boolean>> {
    return this.http.get<WebResponse<Boolean>>(
      `${this.apiUrl}/idea-box/${id}/checkScoreSheets`
    )
  }

  getScoreSheetsForIdeaBox$(id: number): Observable<WebResponse<IdeaScoreSheets[]>> {
    return this.http.get<WebResponse<IdeaScoreSheets[]>>(
      `${this.apiUrl}/idea-box/scoreSheets/${id}`
    )
  }

  isIdeaBoxReadyToClose$(id: number): Observable<WebResponse<boolean>> {
    return this.http.get<WebResponse<boolean>>(
      `${this.apiUrl}/idea-box/${id}/isReadyToClose`
    )
  }

  closeIdeaBox$(id: number): Observable<WebResponse<string>> {
    return this.http.post<WebResponse<string>>(
      `${this.apiUrl}/idea-box/${id}/close`, {}
    )
  }
}
