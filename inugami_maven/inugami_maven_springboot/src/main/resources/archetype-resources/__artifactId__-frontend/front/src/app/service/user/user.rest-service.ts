import {inject, Injectable} from '@angular/core'
import {SearchResponse} from 'inugami-ng/models'
import {Observable} from 'rxjs'
import {UserAPI, UserDTOSearchRequestAPI} from '../../model/user.model'
import {HttpClient, HttpParams} from '@angular/common/http'

const BASE_API = 'ws/user';

@Injectable({providedIn: 'root'})
export class UserRestService {
  // ===================================================================================================================
  // ATTRIBUTES
  // ===================================================================================================================
  httpClient = inject(HttpClient);

  // ===================================================================================================================
  // CREATE
  // ===================================================================================================================
  create(users: UserAPI[]): Observable<UserAPI[]> {
    return this.httpClient.post<UserAPI[]>(BASE_API, users);
  }

  // ===================================================================================================================
  // READ
  // ===================================================================================================================
  search(request?: UserDTOSearchRequestAPI): Observable<SearchResponse<UserAPI>> {
    const currentRequest = {...(request ?? {})};
    if (!currentRequest.page || currentRequest.page < 0) {
      currentRequest.page = 0;
    }
    if (!currentRequest.pageSize || currentRequest.pageSize < 1) {
      currentRequest.pageSize = 15;
    }
    const params = new HttpParams({fromObject: currentRequest as any});
    return this.httpClient.get<SearchResponse<UserAPI>>(BASE_API, {params});
  }

  getByUid(uid: string): Observable<UserAPI> {
    return this.httpClient.get<UserAPI>(`${BASE_API}/${uid}`);
  }

  // ===================================================================================================================
  // UPDATE
  // ===================================================================================================================
  update(users: UserAPI[]):Observable<UserAPI[]> {
    return this.httpClient.put<UserAPI[]>(BASE_API, users);
  }

  // ===================================================================================================================
  // DELETE
  // ===================================================================================================================


}
