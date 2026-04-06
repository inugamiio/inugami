import {ApplicationConfig, provideBrowserGlobalErrorListeners, signal} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {INUGAMI_DEFAULT_ICONS, InugamiIconsUtils} from 'inugami-icons';
import {provideCacheTracking, SVG_ASSETS} from 'inugami-ng/services';
import {INUGAMI_SVG_ASSETS_DEFAULT} from 'inugami-svg-assets';
import {UuidUtils} from 'inugami-ng/utils';
import {APP_BASE_HREF} from '@angular/common'


InugamiIconsUtils.register(INUGAMI_DEFAULT_ICONS);
SVG_ASSETS.register(INUGAMI_SVG_ASSETS_DEFAULT)

const APPLICATION = signal<string>('demo');
const ENV         = signal<string>('DEV');
const SESSION_UID = signal<string>(UuidUtils.buildUid());
const VERSION     = signal<string>('0.0.1');

export const appConfig: ApplicationConfig = {
  providers: [
    provideCacheTracking({
                           env        : ENV,
                           sessionUid : SESSION_UID,
                           application: APPLICATION,
                           version    : VERSION
                         }),
    {
      provide : APP_BASE_HREF,
      useValue: '/'
    },
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes)
  ]
};
