import {SimpleEvent}      from './simple.event';

export class Event {
    constructor(
        public name           : string,
        public from           : string,
        public until          : string,
        public provider       : string,
        public targets        : SimpleEvent[]
    ){ }
}
