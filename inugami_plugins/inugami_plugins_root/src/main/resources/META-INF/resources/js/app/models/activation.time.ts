import { TimeSlot } from "./time.slot";

export class ActivationTime{
    constructor(
        public days         : string[],
        public hours        : TimeSlot[]
    ){}
}