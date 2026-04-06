import {AfterViewInit, Component, computed, effect, inject, input, signal} from '@angular/core';
import {InuIcon} from 'inugami-icons';
import {NgClass} from '@angular/common';
import {UserAPI} from '../../../model/user.model'
import {Router} from '@angular/router'

@Component({
             selector   : 'user-card',
             standalone : true,
             imports    : [],
             templateUrl: './user-card.component.html',
             styleUrl   : './user-card.component.scss',
           })
export class UserCard {

  user = input<UserAPI>();
  router = inject(Router);

  goToUser():void{
    this.router.navigate([`/user/file/${this.user()?.uid!}`])
  }
}
