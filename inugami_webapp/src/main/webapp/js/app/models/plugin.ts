import {PluginConfiguration}    from './plugin.configuration';
import {OptionnalEventConfig}   from './optionnal.event.config';

export class Plugin {
    constructor(
        public enable              : boolean,
        public config              : PluginConfiguration ,
        public events              : OptionnalEventConfig
    ){ }
}
