import {PluginFrontConfig}            from './plugin.front.config';
export class OptionnalEventConfig {
    constructor(
        public present           : boolean,
        public data              : PluginFrontConfig
    ){ }
}
