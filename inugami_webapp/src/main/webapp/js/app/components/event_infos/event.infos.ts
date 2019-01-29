import {Component,Inject,OnInit,Input,Output,forwardRef}    from '@angular/core';
import {NG_VALUE_ACCESSOR,ControlValueAccessor}             from '@angular/forms';

import {AdminBloc}               from './../admin_bloc/admin.bloc';
import {SimpleEventInfos}        from './../simple_event_infos/simple.event.infos';
import {EventConfig}             from './../../models/event.config';


export const EVENT_INFOS_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => EventInfos),
    multi: true
};


@Component({
  selector      : 'event-infos',
  templateUrl   : 'js/app/components/event_infos/event.infos.html',
  directives    : [AdminBloc,SimpleEventInfos],
  providers     : [EVENT_INFOS_VALUE_ACCESSOR]
})
export class EventInfos implements ControlValueAccessor{

  /*****************************************************************************
  * ATTRIBUTES
  *****************************************************************************/
  private innerValue                  : EventConfig  = null;
  private hasSimpleEvents             : boolean;
  private hasEvents                   : boolean;

  /*****************************************************************************
  * IMPLEMENTS ControlValueAccessor
  *****************************************************************************/
  writeValue(value: any) {
    if (value !== this.innerValue) {
      this.innerValue = value;
      if(isNotNull(value)){
        this.hasSimpleEvents = isNotNull(value.simpleEvents) && value.simpleEvents.length >0;
        this.hasEvents       = isNotNull(value.events)       && value.events.length >0;
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
