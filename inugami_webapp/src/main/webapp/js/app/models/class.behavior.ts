import {Config}                  from './config';
export class ClassBehavior {
    constructor(
        public name             : string,
        public className        : string,
        public configs          : Config[]
    ){ }
}
