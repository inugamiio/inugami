import { Component, OnInit, forwardRef, Input, Output, EventEmitter, ChangeDetectorRef, NgModule } from '@angular/core';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';

import {DaysSelectorChange} from '../../models/days.selector.change';

export const INPUT_DAYS_SELECTOR_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => InputDaysSelector),
    multi: true
}; 

@Component({
    selector: 'input-days-selector',
    providers: [INPUT_DAYS_SELECTOR_VALUE_ACCESSOR],
    template: `
    <div [ngClass]="{'inugami-input-days-selector':true,readonly : readonly}" >
      <span [ngClass]="{'days':true}">
        <span *ngFor="let day of daysList; index as i" [ngClass]="getDayClass(day)" 
        (click)="onClick(day) ">
        <span [class]="'label'">{{labelList[i]}}</span>
        </span>
      </span>
    </div>
  `
})
export class InputDaysSelector implements OnInit, ControlValueAccessor {

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

    onModelChange                   : Function = () => { };

    onModelTouched                  : Function = () => { };

    styleClasses                    : string[];

    focused                         : boolean;

    valid                           :  boolean = true;

    enableValidator                 : boolean = false;

    daysModel                       : string[] = [];

    daysList                        : string[] = [];

    labelList                       : string[] = [];


    /*****************************************************************************
    * INIT
    *****************************************************************************/

    ngOnInit() {
       
       this.daysList = ["Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"];
       let labelProperty = org.inugami.formatters.message("days.selector.first.letters")
       
       org.inugami.asserts.isFalse(labelProperty === "??days.selector.first.letters??");
       this.labelList = labelProperty.split(" ");
       this.daysModel = []
       
    }
    constructor(private cd: ChangeDetectorRef) { }

    /****************** ***********************************************************
     * IMPLEMENTS ControlValueAccessor
     *****************************************************************************/
    writeValue(daysModel: any): void {
      if(isNull(daysModel) || !isArray(daysModel)){
        daysModel = [];
      }
      this.daysModel = daysModel;
      if(isNotNull(this.onModelChange)){
        this.onModelChange(this.daysModel);
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

    updateModel(event: Event, value: boolean) {
    }
    /*****************************************************************************
     * HANDLERS
     *****************************************************************************/
    onCompoFocused($event) {
        if (this.focused === false) {
            this.onFocus.emit();
        }
        this.focused = true;

    }
    onClick(day : string) {
        if (!this.disabled && !this.readonly) {
          this.processValidator();
            if(this.daysModel.indexOf(day) > -1){
              this.daysModel.splice(this.daysModel.indexOf(day),1);
            }else{
              this.daysModel.push(day);
            }
            this.onChange.emit(new DaysSelectorChange(this.daysModel));
            this.onModelChange(this.daysModel);
            this.onModelTouched();
        }
    }

    onBlur($event) {
      this.processValidator();
        this.focused = false;
    }

    displayLabel() {
        return this.isNotNull(this.displayLabel) && ("" + this.displayLabel).length > 0;
    }

    /*****************************************************************************
      * VALIDATOR
      *****************************************************************************/
    private processValidator() {
        if (this.enableValidator && this.isNotNull(this.validator)) {
          let error = this.validator(this.daysModel);
        }
    }
    /*****************************************************************************
      TOOLS
      *****************************************************************************/
    isNotNull(value) {
        return (value != undefined && value != null)
    }

    getDayClass(day : string){
      let selected = this.daysModel.indexOf(day) > -1 ? "selected" : "";
      return "day day-"+day+" "+selected ;
    }

}


