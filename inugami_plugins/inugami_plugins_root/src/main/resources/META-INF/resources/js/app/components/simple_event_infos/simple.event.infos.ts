import {Component,forwardRef}                       from '@angular/core';
import {NG_VALUE_ACCESSOR,ControlValueAccessor}     from '@angular/forms';
import {SimpleEvent}                                from './../../models/simple.event';


export const SIMPLE_EVENT_INFOS_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => SimpleEventInfos),
    multi: true
};


@Component({
  selector      : 'simple-event-infos',
  templateUrl   : 'js/app/components/simple_event_infos/simple.event.infos.html',
  directives    : [],
  providers     : [SIMPLE_EVENT_INFOS_VALUE_ACCESSOR]
})
export class SimpleEventInfos implements ControlValueAccessor{

  /*****************************************************************************
  * ATTRIBUTES
  *****************************************************************************/
  private innerValue                  : SimpleEvent  = null;
  private hasTimeShift                : boolean;



  /*****************************************************************************
  * IMPLEMENTS ControlValueAccessor
  *****************************************************************************/
  writeValue(value: any) {
    if (value !== this.innerValue) {
      this.innerValue = value;
      if(isNotNull(value)){
        this.hasTimeShift = isNotNull(value.from) ||  isNotNull(value.until);
      }
    }
  }

  registerOnChange(fn: any) {
    this.onChangeCallback = fn;
  }
  registerOnTouched(fn: any) {
    this.onTouchedCallback = fn;
  }
}
