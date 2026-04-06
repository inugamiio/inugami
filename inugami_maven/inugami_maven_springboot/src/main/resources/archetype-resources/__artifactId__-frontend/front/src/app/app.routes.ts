import { Routes } from '@angular/router';
import {UserSearchView} from './view/user/user-search.view'
import {UserFileView} from './view/user/file/user-file.view'

export const routes: Routes = [
  {
    path: "user", children: [
      {path: '', redirectTo:'search',pathMatch:'full'},
      {path: 'search', component: UserSearchView},
      {path: 'file/:uid', component: UserFileView}
    ]
  }
];
