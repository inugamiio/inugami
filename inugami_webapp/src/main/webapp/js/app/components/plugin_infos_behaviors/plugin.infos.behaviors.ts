import {Component,Input,forwardRef}                 from '@angular/core';
import {NG_VALUE_ACCESSOR,ControlValueAccessor}     from '@angular/forms';

import {ClassBehavior}                              from './../../models/class.behavior';
import {AdminBloc}                                  from './../admin_bloc/admin.bloc';

export const PLUGIN_INFOS_BEHAVIORS_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => PluginInfosBehaviors),
    multi: true
};


@Component({
  selector      : 'plugin-infos-behaviors',
  templateUrl   : 'js/app/components/plugin_infos_behaviors/plugin.infos.behaviors.html',
  directives    : [AdminBloc],
  providers     : [PLUGIN_INFOS_BEHAVIORS_VALUE_ACCESSOR]
})
export class PluginInfosBehaviors implements ControlValueAccessor{

  /*****************************************************************************
  * ATTRIBUTES
  *****************************************************************************/
  @Input() title                      : string;
  @Input() styleClass                 : string;
  private innerValue                  : ClassBehavior[] = null;
  private showComponent               : boolean         = false;


  /*****************************************************************************
  * IMPLEMENTS ControlValueAccessor
  *****************************************************************************/
  writeValue(value: any) {
    if (value !== this.innerValue) {
      this.innerValue = value;
      if(value!=undefined && value.length>0){
        this.showComponent=true;
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
