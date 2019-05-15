export class AlertEntity {
    constructor(
        public alerteName       : string,
        public level            : string,
        public levelType        : string,
        public levelNumber      : number,
        public label            : string,
        public subLabel         : string,
        public channel          : string,
        public duration         : number,
        public created          : number,
        public data             : string,
        public enable           : boolean,
        public ttl              : number,
        public providers        : string[]
    ){ }
}


