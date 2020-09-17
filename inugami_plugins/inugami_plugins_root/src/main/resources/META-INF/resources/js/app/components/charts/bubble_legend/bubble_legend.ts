import {
  Component,
  Inject,
  OnInit,
  Input,
  Output,
  forwardRef,
  ViewChild,
  ViewEncapsulation
} from '@angular/core';

import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { GraphiteDatapoint } from 'js/app/models/graphite.datapoint.ts';

export const BUBBLE_LEGEND_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => BubbleLegend),
  multi: true
};


@Component({
  selector: 'bubble-legend',
  template: `
  <div [class]="getStyleClass()" [ngClass]="'bubble-legend'" *ngIf="currentValueStr">
      <div class="counter-container">
        <div class="counter">{{currentValueStr}}
          <span *ngIf="unit" class="unit">{{unit}}</span>
        </div>
        <div class="label" *ngIf="label">{{label}}</div>
      </div>

      <div class="counter-type">
        <i class="fa" [class]="bubbleLegendIcon" *ngIf="bubbleLegendIcon"></i>
        <span  class="tag" *ngIf="tag">{{tag}}</span>
      </div>
  </div>
  `,
  directives: [],
  providers: [BUBBLE_LEGEND_VALUE_ACCESSOR],
  encapsulation: ViewEncapsulation.None
})
export class BubbleLegend implements ControlValueAccessor, OnInit {


  /*****************************************************************************
  * ATTRIBUTES
  *****************************************************************************/
  private error              : string   = null;
  private logger             : any      = LOGGER("BubbleLegend");
  private currentValue       : number;
  private currentValueStr    : string;
  private bubbleLegendIcon   : string;
  
  @Input() styleClass        : string;

  @Input() pluginName        : string;
  @Input() event             : string;

  @Input() icon              : string;
  @Input() label             : string;
  @Input() tag               : string;
  @Input() unit              : string;
  @Input() numberSize        : number   = 0;
  @Input() formatter         : any      = io.inugami.formatters.truncateNumber;
  @Input() dataExtractor     : any;
  @Input() alerts            : any[];

  /*****************************************************************************
  * INIT
  *****************************************************************************/
  ngOnInit() {
    if (isNotNull(this.icon)) {
      this.bubbleLegendIcon = "fab fa-" + this.icon;
    }

    if (isNotNull(this.event)) {
      let self = this;
      io.inugami.events.addEventListenerByPlugin(this.pluginName, this.event, function (event) {
        self.processEvent(event);
      });
    }

  }


  /***************************************************************************
  * UPDATE
  ***************************************************************************/
  updateValue() {
    if (isNotNull(this.innerValue)) {
      if(isNull(this.dataExtractor)){
        this.currentValue = io.inugami.data.extractors.graphite.simpleValue(this.innerValue);
      }else{
        this.currentValue = this.dataExtractor(this.innerValue);
      }
      
      if(isNull(this.currentValue.value)){
        this.currentValue.value = 0;
      }
      this.currentValueStr = this.formatter(this.currentValue.value);
    }
  }


  /*****************************************************************************
  * IMPLEMENTS ControlValueAccessor
  *****************************************************************************/
  processEvent(event)  {
    if (isNotNull(event.detail.data)) {
      this.alerts = event.detail.data.alerts;
      if(isNotNull(event.detail.data.values)){
        let values = event.detail.data.values;
        if (Array.isArray(values) && values.length >= 1) {
          this.writeValue(values[0]);
        } else {
          this.writeValue(values);
        }
      }
    }
  }

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

  getStyleClass(){
    return io.inugami.rendering.alertsStyles(this.styleClass,this.alerts); 
  }

}