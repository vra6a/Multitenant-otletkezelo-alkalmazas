import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { WebResponse } from '../models/webResponse';
import { TagDto } from '../models/dto/tagDto';
import { Observable } from 'rxjs';
import { TagSlimDto } from '../models/slimDto/tagSlimDto';

@Injectable({
  providedIn: 'root',
})
export class TagService {
  constructor(private http: HttpClient) {}

  apiUrl = `${environment.apiUrl}`;

  getTag$(id: string): Observable<WebResponse<TagDto>> {
    return this.http.get<WebResponse<TagDto>>(`${this.apiUrl}/tag/${id}`);
  }

  getTagSlim$(id: string): Observable<WebResponse<TagSlimDto>> {
    return this.http.get<WebResponse<TagSlimDto>>(
      `${this.apiUrl}/tag/slim/${id}`
    );
  }

  getTags$(): Observable<WebResponse<TagSlimDto[]>> {
    return this.http.get<WebResponse<TagSlimDto[]>>(`${this.apiUrl}/tags`);
  }

  createTag$(tag: TagDto): Observable<WebResponse<TagDto>> {
    return this.http.post<WebResponse<TagDto>>(`${this.apiUrl}/tag`, tag);
  }
}
