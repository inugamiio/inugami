import {PluginComponent}      from './plugin.component';
import {RouterModel}          from './router.model';
import {MenuLink}             from './menu.link';

export class PluginFrontConfig {
  constructor(
    public pluginBaseName     : string,
    public routerModuleName   : string,
    public commonsCss         : string,
    public module             : PluginComponent,
    public router             : RouterModel[],
    public menuLinks          : MenuLink[],
  ){ }
}
