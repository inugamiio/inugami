import { DynamicLevelValues } from "./dynamic.level.values";

export class DynamicLevel{
    constructor(public name                     : string,
                public data                     : DynamicLevelValues[],
                public activationDelais         : number,
    ){}
}