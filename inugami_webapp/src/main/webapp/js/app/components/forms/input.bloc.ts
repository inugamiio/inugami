import {Component,
  Input,
  Output,
  forwardRef,
  EventEmitter,
  AfterViewInit}                                      from '@angular/core';

import {NG_VALUE_ACCESSOR,ControlValueAccessor}       from '@angular/forms';


export const INPUT_BLOC_VALUE_ACCESSOR: any = {
provide: NG_VALUE_ACCESSOR,
useExisting: forwardRef(() => InputBloc),
multi: true
};


@Component({
selector      : 'input-bloc',
template      : `
<div [ngClass]="getBaseStyleClass()" [class]="styleClass" [ngStyle]="style">
  <div [ngClass]="' input-bloc-label '" [class.show]="displayLabel()">
    &nbsp;
    <span class="input-bloc-label-txt">{{labelTxt}}</span>
    <span class="input-bloc-label-end"></span>
  </div>

  <div class="input-bloc-data">
    <input  [type]="type" 
            [placeholder]="labelTxt"
            [attr.disabled]="isDisabled?'':null"
            [(ngModel)]="innerValue"
            (focus)="onFocus($event)"
            (focusout)="onFocusOut($event)"
            (ngModelChange)="onChange($event)"
            (keypress)="onKeyPress($event)"
            (enter)="onEnterPress($event)"/>
  </div>
  <div class="error-message" [class.show]="errorTxt">
    &nbsp;  
    <span  class="error-message-label">{{errorTxt}}</span>
  <div>
  <div class="input-bloc-end"></div>
</div>  
`,
directives    : [],
providers     : [INPUT_BLOC_VALUE_ACCESSOR]
})
export class InputBloc implements ControlValueAccessor,AfterViewInit {

  /*****************************************************************************
  * ATTRIBUTES
  *****************************************************************************/
  @Input() styles                     : string;
  @Input() styleClass                 : string;
  @Input() label                      : string    = "";
  @Input() type                       : string    = "text";
  @Input() validator                  : any ;
  @Input() disabled                   : boolean ;
  

  @Output() focus                     : EventEmitter<any> = new EventEmitter();
  @Output() focusout                  : EventEmitter<any> = new EventEmitter();
  @Output() ngModelChange             : EventEmitter<any> = new EventEmitter();
  @Output() keypress                  : EventEmitter<any> = new EventEmitter();
  @Output() enter                     : EventEmitter<any> = new EventEmitter();


  private onChangeCallback            : any;
  private onTouchedCallback           : any;       
  private innerValue                  : any       = null;
  private labelTxt                    : string    = "";
  private errorTxt                    : string    = null;
  private enableValidator             : boolean   = false;
  private isDisabled                  : string    = "";

  /*****************************************************************************
  * INIT
  *****************************************************************************/
  ngAfterContentInit() {
    this.labelTxt = this.applyProperty(this.label);
    if(this.disabled){
      this.isDisabled="disabled";
    }
  }

  private applyProperty(key:string){
    let result = key;
    let bundleMsg =  org.inugami.formatters.messageValue(key);
    if(isNotNull(bundleMsg)){
      result = bundleMsg;
    }
    return result;
  }
  /*****************************************************************************
  * METHODS
  *****************************************************************************/
  private displayLabel(){
    return isNotNull(this.innerValue) && (""+this.innerValue).length>0;
  }
 
  private getBaseStyleClass(){
    let result = ["input-bloc"];
    result.push(this.isDisabled);

    if(isNotNull(this.errorTxt)){
      result.push("error");
    }
    return result.join(" ");
  }
  
  /*****************************************************************************
  * VALIDATOR
  *****************************************************************************/
  private processValidator(){
    this.errorTxt = null;
    if(this.enableValidator && isNotNull(this.validator)){
      let error = this.validator(this.innerValue);
      if(isNotNull(error)){
        this.errorTxt = this.applyProperty(error);
      }
    }
  }
  /*****************************************************************************
  * HANDLERS
  *****************************************************************************/
  private onFocus(event){
    this.enableValidator = true;
    this.focus.emit(event);
  }
  private onFocusOut(event){
    this.processValidator();
    this.focusout.emit(event);
  }
  private onChange(event){
    this.processValidator();
    this.onChangeCallback(event);
    this.ngModelChange.emit(event);
  }
  private onKeyPress(event){
    this.keypress.emit(event);
    this.onTouchedCallback();
  }
  private onEnterPress(event){
    this.processValidator();
    this.enter.emit(event);
    this.onTouchedCallback();
  }
  /*****************************************************************************
  * IMPLEMENTS ControlValueAccessor
  *****************************************************************************/
  writeValue(value: any) {
    this.innerValue = value;
  }

  registerOnChange(fn: any) {
    this.onChangeCallback = fn;
  }
  registerOnTouched(fn: any) {
    this.onTouchedCallback = fn;
  }
}
