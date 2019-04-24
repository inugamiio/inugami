import {TimeSlot} from './time.slot';
export class TimeSlotChanged {
    constructor(
       public index         : any,
       public data          : TimeSlot []     
    ){}
}