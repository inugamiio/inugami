import { Component, OnInit, forwardRef, Input, Output, EventEmitter } from '@angular/core';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';

import {TimeSlot} from '../../models/time.slot';

import {TimeSlotAdd} from '../../models/time.slot.add';
import {TimeSlotRemove} from '../../models/time.slot.remove';
import {TimeSlotChanged} from '../../models/time.slot.changed';

export const INPUT_TIME_SLOTS_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => InputTimeSlots),
    multi: true
}; 

@Component({
    selector: 'input-time-slots',
    providers: [INPUT_TIME_SLOTS_VALUE_ACCESSOR],
    templateUrl : 'js/app/components/forms/input.time.slots.html'
})
export class InputTimeSlots implements OnInit, ControlValueAccessor {

    /*****************************************************************************
    * ATTRIBUTES
    *****************************************************************************/
    @Input() style                  : any;

    @Input() styleClass             : string;

    @Input() tabindex               : number;

    @Input() inputId                : string;

    @Input() name                   : string;

    @Input() disabled               : boolean = false; 

    @Input() readonly               : boolean = false;

    @Input() labelOn                : string;

    @Input() labelOff               : string;

    @Input() validator              : any;

    @Output() onFocus               : EventEmitter<any> = new EventEmitter();

    @Output() onChange              : EventEmitter<any> = new EventEmitter();

    @Output() onAdd                 : EventEmitter<any> = new EventEmitter();

    @Output() onDelete              : EventEmitter<any> = new EventEmitter();

    onModelChange                   : Function = () => { };

    onModelTouched                  : Function = () => { };

    styleClasses                    : string[];

    focused                         : boolean;

    valid                           : boolean = true;

    enableValidator                 : boolean = false;

    timeSlotsModel                  : TimeSlot[] = [];

    separator                       : string;

    

    /*****************************************************************************
    * INIT
    *****************************************************************************/

    ngOnInit() {
      this.separator= org.inugami.formatters.message("time.slots.to");
      org.inugami.asserts.isFalse("??time.slots.to??"===this.separator, "property time.slots.to not found");
       if(this.timeSlotsModel.length == 0){
         let initialTimeSlot = new TimeSlot("00:00","23:59"); 
         this.timeSlotsModel.push(initialTimeSlot);
         
       }
    }
    constructor() { }

    /****************** ***********************************************************
     * IMPLEMENTS ControlValueAccessor
     *****************************************************************************/
    writeValue(timeSlots: any): void {
      if(isNull(timeSlots) || !isArray(timeSlots)){
        timeSlots = [];
      }
        this.timeSlotsModel = timeSlots;
        if(isNotNull(this.onModelChange)){
          this.onModelChange(this.timeSlotsModel);
        }
    }

    registerOnChange(fn: Function): void {
        this.onModelChange = fn;
    }

    registerOnTouched(fn: Function): void {
        this.onModelTouched = fn;
    }
    setDisabledState(val: boolean): void {
        this.disabled = val;
    }

    /*****************************************************************************
    * METHODS
    *****************************************************************************/

    addSlot(from = "00:00", to = "23:59"){
      if(!this.readonly && !this.disabled){
        let newSlot = new TimeSlot(from,to); 
        this.timeSlotsModel.push(newSlot);
        let addEventResponse = new TimeSlotAdd(newSlot,this.timeSlotsModel); 
        this.onAdd.emit(addEventResponse);
        this.onModelChange(this.timeSlotsModel);
      }
      this.onModelTouched();
    }
    removeSlot(index : number){
      if(!this.readonly && !this.disabled){
        let removedSlot = this.timeSlotsModel[index];
        this.timeSlotsModel.splice(index,1);

        let removeEventResponse = new TimeSlotRemove(removedSlot,this.timeSlotsModel);
        this.onDelete.emit(removeEventResponse);
        this.onModelChange(this.timeSlotsModel);
      }
      this.onModelTouched();
    }
    

    updateModel(event: Event, value: boolean) {
    }
    /*****************************************************************************
     * HANDLERS
     *****************************************************************************/
    onCompoFocused($event) {
        if (this.focused === false) {
            this.onFocus.emit({
                originalEvent: event
            })
        }
        this.focused = true;

    }
    onClick(event: Event, cb: HTMLInputElement) {
        if (!this.disabled && !this.readonly) {
            
        }
    }

    onBlur($event) {
      this.processValidator();
        this.focused = false;
    }

    onSlotChange($event,i){
      this.processValidator();
      let changedElement = new TimeSlotChanged(i,this.timeSlotsModel);
      this.onChange.emit(changedElement);
      this.onModelTouched();
      this.onModelChange(this.timeSlotsModel);
    }

    displayLabel() {
        return this.isNotNull(this.displayLabel) && ("" + this.displayLabel).length > 0;
    }

    /*****************************************************************************
      * VALIDATOR
      *****************************************************************************/
    private processValidator() {
        if (this.enableValidator && this.isNotNull(this.validator)) {
          let error = this.validator(this.timeSlotsModel);
        }
    }
    /*****************************************************************************
      TOOLS
      *****************************************************************************/
    isNotNull(value) {
        return (value != undefined && value != null)
    }
}
