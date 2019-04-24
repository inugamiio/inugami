import {TimeSlot} from './time.slot';
export class TimeSlotRemove {
    constructor(
       public deleted       :TimeSlot,
       public data          : TimeSlot []     
    ){}
}