import {AfterViewInit, Component, inject, signal} from '@angular/core';
import {InugamiTemplateDirective} from 'inugami-ng/directives'
import {InuPageLayout} from 'inugami-ng/components/inu-page-layout'
import {UserAPI} from '../../model/user.model'
import {SearchResponse} from 'inugami-ng/models';
import {UserService} from '../../service/user/user.service'
import {InuErrorService} from 'inugami-ng/services'
import {InuButton} from 'inugami-ng/components/inu-button'
import {Router} from '@angular/router'
import {UserCard} from '../../component/user/user-card/user-card.component'
import {InuIcon} from 'inugami-icons'


@Component({
             templateUrl: './user-search.view.html',
             styleUrls  : ['./user-search.view.scss'],
             imports: [InugamiTemplateDirective, InuPageLayout, InuButton, UserCard, InuIcon]
           })
export class UserSearchView implements AfterViewInit{


  // ===================================================================================================================
  // ATTRIBUTES
  // ===================================================================================================================
  inuErrorService = inject(InuErrorService);
  userService     = inject(UserService);
  router          = inject(Router);

  usersSearchResponse = signal<SearchResponse<UserAPI>>({
                                                          page    : 0,
                                                          pageSize: 10,
                                                          previous: false,
                                                          next    : false
                                                        });


  ngAfterViewInit(): void {
    this.search();
  }
  // ===================================================================================================================
  // ACTIONS
  // ===================================================================================================================
  search(): void {
    this.userService.search()
      .subscribe({
                   next : res => this.usersSearchResponse.set(res),
                   error: err => this.inuErrorService.handlerError(err)
                 });
  }

  addUser() {
    this.router.navigate(['/user/file/new']);
  }
}
