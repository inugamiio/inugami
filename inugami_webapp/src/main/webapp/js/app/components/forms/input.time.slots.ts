import { Component, OnInit, forwardRef, Input, Output, EventEmitter } from '@angular/core';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';


export const INPUT_TIME_SLOTS_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => InputTimeSlots),
    multi: true
}; 

@Component({
    selector: 'input-time-slots',
    providers: [INPUT_TIME_SLOTS_VALUE_ACCESSOR],
    template: `
    <div [ngClass]="{'inugami-input-time-slots': true}">
      <span class="time-slots">  
        <span  *ngFor="let timeSlots of timeSlotsModel; index as i; " (focus)="onCompoFocused($event)" (blur)="onBlur($event)" [ngClass]="{'time-slot':true, 'first': i==0,'odd':i%2===1,'even':i%2===0}">
          <input name="input-time-slot-from-{{i}}" [ngClass]="{'from': true, 'input':true}" type="time" [(ngModel)]="timeSlots.from" [disabled]="disabled" [readonly]="readonly"
           (change)="onSlotChange($event,i)" 
           (focus)="onCompoFocused($event)"
           [tabindex]="tabindex + i*3"/>
          <span [class]="'separator'">{{separator}}</span>
          <input name="input-time-slot-to-{{i}}" [ngClass]="{'to': true, 'input':true}" type="time" [(ngModel)]="timeSlots.to" [disabled]="disabled" [readonly]="readonly"
           (change)="onSlotChange($event,i)"  
           (focus)="onCompoFocused($event)"
            [tabindex]="tabindex +i*3+1"/>
          <span [class]="'action delete'" 
          (click)="removeSlot(i)" 
          (keyup.enter)="removeSlot(i)" 
          (keyup.space)="removeSlot(i)" 
          (focus)="onCompoFocused($event)" 
          [tabindex]="tabindex +i*3+2">
            <i class="fas fa-minus-circle"></i>
          </span>
        </span>
      </span>  
      <span [class]="'action add'" 
      (click)="addSlot()" 
      (keyup.enter)="addSlot()" 
      (keyup.space)="addSlot()" 
      (focus)="onCompoFocused($event)" 
      [tabindex]="tabindex + timeSlotsModel.length*3" >
        <span class="icon">
        <i class="fas fa-plus"></i>
        </span>
      </span>
    </div>
  `
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

    timeSlotsModel                  : any[] = [];

    separator                       : string;

    

    /*****************************************************************************
    * INIT
    *****************************************************************************/

    ngOnInit() {
       if(this.timeSlotsModel.length == 0){
         let initialTimeSlot = new TimeSlotBuilder().withFrom("00:00")
                                                    .withTo("23:59")
                                                    .build();
         this.timeSlotsModel.push(initialTimeSlot);
         this.separator= org.inugami.formatters.message("time.slots.to");
       }
    }
    constructor() { }

    /****************** ***********************************************************
     * IMPLEMENTS ControlValueAccessor
     *****************************************************************************/
    writeValue(timeSlots: any): void {
        this.timeSlotsModel = timeSlots;
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
        let newSlot = new TimeSlotBuilder().withFrom(from)
                                          .withTo(to)
                                          .build();
        this.timeSlotsModel.push(newSlot);
        let addEventResponse = {"added":newSlot,
                                "data":this.timeSlotsModel} 
        this.onAdd.emit(addEventResponse);
      }
    }
    removeSlot(index : number){
      if(!this.readonly && !this.disabled){
        let removedSlot = this.timeSlotsModel[index];
        this.timeSlotsModel.splice(index,1);

        let removeEventResponse = {"deleted":removedSlot,
                                "data":this.timeSlotsModel} 
        this.onDelete.emit(removeEventResponse);
      }
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
      let changedElement = {"index":i,
                            "data":this.timeSlotsModel};
      this.onChange.emit(changedElement);
      
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

class TimeSlotBuilder{
   from : string;
   to   : string;

   withFrom(from : string = ""){
     this.from = from;
     return this;
   }
   withTo(to : string=""){
     this.to = to;
     return this;
   }

   build(){
     return {
       "from":this.from,
       "to":this.to
     }
   }
}
