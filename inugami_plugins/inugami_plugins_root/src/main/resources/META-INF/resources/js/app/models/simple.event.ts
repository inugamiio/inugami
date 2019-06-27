export class SimpleEvent {
    constructor(
        public name           : string,
        public from           : string,
        public until          : string,
        public provider       : string,
        public parent         : string,
        public query          : string
    ){ }
}
