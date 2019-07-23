import {Gav}              from './gav';
import {Event}            from './event';
import {SimpleEvent}      from './simple.event';

export class EventConfig {
    constructor(
        public gav                 : Gav,
        public enable              : boolean,
        public name                : string,
        public configFile          : string,
        public events              : Event[],
        public simpleEvents        : SimpleEvent[]
    ){ }
}
