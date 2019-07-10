import {EventConfig}            from './event.config';
export class OptionnalEventConfig {
    constructor(
        public present           : boolean,
        public data              : EventConfig[]
    ){ }
}
