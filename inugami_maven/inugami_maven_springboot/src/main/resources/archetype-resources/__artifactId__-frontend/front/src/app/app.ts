import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {InuToast} from 'inugami-ng/components/inu-toast';
import {InuFooter} from 'inugami-ng/components/inu-footer';
import {InuMainHeader} from 'inugami-ng/components/inu-main-header';
import {InuSiteLink} from 'inugami-ng/models';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    InuToast,
    InuFooter,
    InuMainHeader
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('demo');
  protected links               = signal<InuSiteLink[]>([
                                                          {
                                                            title   : 'Users',
                                                            path    : '/user'
                                                          }
                                                        ]);
}
