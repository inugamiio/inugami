import {Auditable, SearchRequest} from 'inugami-ng/models';

export interface UserAPI extends Auditable{
  uid?: string;
  firstName?: string;
  lastName?: string;
  email?: string;
}

export interface UserDTOSearchRequestAPI extends SearchRequest{
  uid?:string[];
  firstName?:string[];
  lastName?:string[];
  email?:string[];
}
