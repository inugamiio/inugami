import { ProviderSource } from "./provider.source";
import { Tag } from "./tag";
import { DynamicLevel } from "./dynamic.level";
import { ActivationTime } from "./activation.time"; 

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
        public script           : string,
        public providers        : string[],
        public source           : ProviderSource,
        public tags             : Tag[],
        public levels           : DynamicLevel[],
        public activations      : ActivationTime[],
        public inverse          : boolean,
        public dynamicAlerting? : boolean
    ){ }
}


