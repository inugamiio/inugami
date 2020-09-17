import {Component,
    Input,
    forwardRef,
    AfterViewInit}                                      from '@angular/core';
  
  import {NG_VALUE_ACCESSOR,ControlValueAccessor}       from '@angular/forms';
  
  import {GraphiteDatapoint}                            from './../../../models/graphite.datapoint';
  
  export const CHART_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => Value),
  multi: true
  };
  
  
  @Component({
  selector      : 'value',
  template      : `
  <span [ngClass]="'value-component'" [class]="getStyleClass()" ><span class="data"><span *ngIf="alerts" class="alert-icon"></span><span class="current-value">{{valueStr}}</span><span class="unit">{{unit}}</span><span class="value-area-end"></span></span><span class="time-area"><span class="time">{{time}}</span><span class="time-are-end"></span></span><span class="value-component-end"></span></span>
  `,
  directives    : [],
  providers     : [CHART_VALUE_ACCESSOR]
  })
  export class Value implements ControlValueAccessor,AfterViewInit {
  
  /*****************************************************************************
  * ATTRIBUTES
  *****************************************************************************/
  private innerValue                  : any  = null;
  private datapoint                   : GraphiteDatapoint  = null;
  private valueStr                    : string = null;
  private time                        : string = null;
  private styleStr                    : string;
  @Input() styleClass                 : string;
  @Input() title                      : string;
  @Input() unit                       : string;
  @Input() pluginName                 : string;
  @Input() event                      : string;
  @Input() extractor                  : any;
  @Input() formatter                  : any;
  @Input() dateFormatter              : any;
  @Input() inverseOrderData           : boolean;
  @Input() alerts                     : any[];
  
  
  /*****************************************************************************
  * INIT
  *****************************************************************************/
    ngAfterContentInit() {
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
          this.valueStr = this.formatDataPoint(this.datapoint.value);
          this.time     = this.formatDate(this.datapoint.timestamp);
        }
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
        result = io.inugami.formatters.timestampToHour(value);
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
  