import {Component,forwardRef}                       from '@angular/core';
import {NG_VALUE_ACCESSOR,ControlValueAccessor}     from '@angular/forms';

import {Plugin}                                     from './../../models/plugin'
import {PluginInfosBehaviors}                       from './../plugin_infos_behaviors/plugin.infos.behaviors';
import {AdminBloc}                                  from './../admin_bloc/admin.bloc';
import {EventInfos}                                 from './../event_infos/event.infos';

export const PLUGIN_INFOS_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => PluginInfos),
    multi: true
};


@Component({
  selector      : 'plugin-infos',
  templateUrl   : 'js/app/components/plugin_infos/plugin.infos.html',
  directives    : [PluginInfosBehaviors,AdminBloc,EventInfos],
  providers     : [PLUGIN_INFOS_VALUE_ACCESSOR]
})
export class PluginInfos implements ControlValueAccessor{

  /*****************************************************************************
  * ATTRIBUTES
  *****************************************************************************/
  private innerValue                  : Plugin   = null;
  private enableStyleClass            : string   = "enable";
  private hasEvents                   : boolean  = false;



  /*****************************************************************************
  * IMPLEMENTS ControlValueAccessor
  *****************************************************************************/
  writeValue(value: any) {
    if (value !== this.innerValue) {
      this.innerValue = value;

      this.hasEvents=false;
      if(isNotNull(this.innerValue)){
        this.hasEvents=isNotNull(this.innerValue.events) && this.innerValue.events.present;
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
