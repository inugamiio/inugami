import {Gav}                  from './gav';
import {OptionnalEventConfig} from './optionnal.plugin.front.config';
import {Resource}             from './resource';
import {ClassBehavior}        from './class.behavior';
import {EventsFileModel}      from './events.file.model';

export class PluginConfiguration {
    constructor(
        public configFile       : string,
        public gav              : Gav,
        public frontConfig      : OptionnalEventConfig,
        public resources        : Resource[],
        public providers        : ClassBehavior[],
        public listeners        : ClassBehavior[],
        public processors       : ClassBehavior[],
        public eventsFiles      : EventsFileModel[],
        public dependencies     : Gav[]
    ){ }
}
