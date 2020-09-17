import {Component,
  Inject,
  OnInit,
  Input,
  Output,
  forwardRef,
  AfterViewInit,
  ViewChild,
  ElementRef}                                         from '@angular/core';

import * as d3 from 'd3';
import {NG_VALUE_ACCESSOR,ControlValueAccessor}       from '@angular/forms';


import {GraphiteDatapoint}                            from 'js/app/models/graphite.datapoint.ts';

export const SIMPLE_VALUE_ACCESSOR: any = {
provide: NG_VALUE_ACCESSOR,
useExisting: forwardRef(() => SimpleValue),
multi: true
};


@Component({
selector      : 'simple-value',
templateUrl   : 'js/app/components/charts/simple_value/simple.value.html',
directives    : [],
providers     : [SIMPLE_VALUE_ACCESSOR]
})
export class SimpleValue implements ControlValueAccessor,AfterViewInit {

/*****************************************************************************
* ATTRIBUTES
*****************************************************************************/
private innerValue                     : any  = null;
private datapoint                      : GraphiteDatapoint  = null;
private valueStr                       : string = null;
private time                           : string = null;
private styleStr                       : string;
private previousData                   : number =0;
private d3JsNode                       : any;

@ViewChild('container') chartContainer : ElementRef;
@Input() styleClass                    : string;
@Input() title                         : string;
@Input() unit                          : string;
@Input() pluginName                    : string;
@Input() event                         : string;
@Input() extractor                     : any;
@Input() formatter                     : any;
@Input() dateFormatter                 : any;
@Input() inverseOrderData              : boolean;
@Input() delay                         : number   = 0;
@Input() duration                      : number   = 1000;
@Input() alerts                        : any[];



/*****************************************************************************
* INIT
*****************************************************************************/
  ngAfterContentInit() {
    this.d3JsNode = d3.select(this.chartContainer.nativeElement);

    if(isNotNull(this.event)){
      let self= this;

      io.inugami.events.addEventListenerByPlugin(this.pluginName,this.event, function(event) {
          if (isNotNull(event.detail.data) && isNotNull(event.detail.data.values)) {
            let realData = null;
            self.alerts = event.detail.data.alerts;
            if(Array.isArray(event.detail.data.values)){
              realData = event.detail.data.values[0];
            }else{
              realData = event.detail.data.values;
            }
            self.writeValue(realData);
          }
      });
    }
  }

/*****************************************************************************
* ACTIONS
*****************************************************************************/
  updateValue(){
    if(isNotNull(this.innerValue)){
      this.datapoint = this.extractDatapoint(this.innerValue);
      if(isNotNull(this.datapoint)){
        this.time     = this.formatDate(this.datapoint.timestamp);
        this.processUpdateValueStr(this.datapoint.value);
      }
    }
  }
  
  processUpdateValueStr(value){
    if(isNull(this.duration)|| this.duration<=0){
      this.valueStr = this.formatDataPoint(value);
    }else{

      let self = this;
      svg.animate.tweenValue(this.d3JsNode,
                             this.delay,
                             this.duration,
                              self.previousData,
                              value,
                              (data)  =>{self.valueStr = this.formatDataPoint(data) },
                              (onDone)=>{self.previousData=onDone}
                            );
    }
  }


  extractDatapoint(value){
    let result= null;
    if(isNotNull(this.extractor)){
      result = this.extractor(value);
    }else{
      result = io.inugami.data.extractors.graphite.simpleValue(value);
    }
    return result;
  }

  formatDataPoint(value){
    let result = null;
    if(isNull(this.formatter)){
      result = io.inugami.formatters.truncateNumber(value);
    }else{
      result = this.formatter(value);
    }

    return result;
  }

  formatDate(value){
    let result = null;
    if(isNull(this.dateFormatter)){
      result = io.inugami.formatters.timestampToDate(value);
    }else{
      result = this.dateFormatter(value);
    }
    return result;
  }

  getStyleClass(){
    return io.inugami.rendering.alertsStyles(this.styleClass,this.alerts); 
  }
  /*****************************************************************************
  * IMPLEMENTS ControlValueAccessor
  *****************************************************************************/
  writeValue(value: any) {
    if (value !== this.innerValue) {
      this.innerValue = value;
      this.updateValue();
    }
  }

  registerOnChange(fn: any) {
    this.onChangeCallback = fn;
  }
  registerOnTouched(fn: any) {
    this.onTouchedCallback = fn;
  }
}
