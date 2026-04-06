import {inject, Injectable} from '@angular/core'
import {Observable} from 'rxjs'
import {UserAPI, UserDTOSearchRequestAPI} from '../../model/user.model'
import { SearchResponse } from 'inugami-ng/models'
import {UserRestService} from './user.rest-service'

@Injectable({providedIn: 'root'})
export class UserService {
  // ===================================================================================================================
  // ATTRIBUTES
  // ===================================================================================================================
  userRestService = inject(UserRestService);

  // ===================================================================================================================
  // CREATE
  // ===================================================================================================================
  create(users: UserAPI[]):Observable<UserAPI[]> {
    return this.userRestService.create(users);
  }

  // ===================================================================================================================
  // READ
  // ===================================================================================================================
  search(request?:UserDTOSearchRequestAPI): Observable<SearchResponse<UserAPI>> {
    return this.userRestService.search(request);
  }

  getByUid(uid:string):Observable<UserAPI>{
    return this.userRestService.getByUid(uid);
  }

  // ===================================================================================================================
  // UPDATE
  // ===================================================================================================================
  update(users: UserAPI[]):Observable<UserAPI[]> {
    return this.userRestService.update(users);
  }
  // ===================================================================================================================
  // DELETE
  // ===================================================================================================================


}
