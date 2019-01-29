import {Component,
        Input,
        Output,
        forwardRef,
        AfterViewInit,
        ViewChild,
        ElementRef}                                      from '@angular/core';
import {NG_VALUE_ACCESSOR,ControlValueAccessor}             from '@angular/forms';
import * as d3 from 'd3';

export const DOUBLE_BUBBLE_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() =>DoubleBubble),
    multi: true
};


@Component({
  selector      : 'double-bubble',
  templateUrl   : 'js/app/components/charts/double_bubble/double.bubble.html',
  directives    : [],
  providers     : [DOUBLE_BUBBLE_VALUE_ACCESSOR]
})
export class DoubleBubble implements ControlValueAccessor,AfterViewInit {
    /***************************************************************************
    * ATTRIBUTES
    ***************************************************************************/
    @Input() styleClass                 : string;
    
    @Input() pluginName                 : string;
    @Input() event                      : string;

    @Input() title                      : string;
    @Input() subtitle                   : string;

    @Input() nbDigit                    : number = 1;
    @Input() unit                       : string;
    @Input() mainValueLabel             : string;
    @Input() secondValueLabel           : string;
    @Input() mainValueFormatter         : any     = org.inugami.formatters.truncateNumber;
    @Input() secondValueFormatter       : any     = null;
    @Input() secondValueUnit            : string  = "%";
    @Input() diffCalculator             : any;
    @Input() dataExtractor              : any;
    @Input() alerts                     : any[];
    
    @ViewChild('container') chartContainer : ElementRef;
    private d3JsNode                    : any;
    private mainPreviousValue           : number = 0;
    private secondPreviousValue         : number = 0;

    private valueStr                    : string;
    private previousStr                 : string;
    private previousStyleClass          : string;
    private previousUnit                : string;
    private mainLabelStyleClass         : string = "without-label";


    private values                      : any;

    private innerValue                  : any;


    /***************************************************************************
    * iNITIALIZE
    ***************************************************************************/
    constructor(){
        this.values = {
            currentValue :{
              previous :  0,
              current :  0
            },
            previousValue :{
              previous :  0,
              current :  0
            }
        };
            
    }
    /*****************************************************************************
    * INIT
    *****************************************************************************/
    ngAfterContentInit() {
        let container    = this.chartContainer.nativeElement;
        this.d3JsNode    = d3.select(container);

        if(isNotNull(this.event)){
          let self= this;

          org.inugami.events.addEventListenerByPlugin(this.pluginName,this.event, function(event) {
            if (isNotNull(event.detail.data)){ 
              self.alerts = event.detail.data.alerts;  
              if (isNotNull(event.detail.data.values)) {
                self.writeValue(event.detail.data.values);
              }
            }
          });
        }

        if(isNotNull(this.mainValueLabel)){
          this.mainLabelStyleClass="with-label";
        }
    }

    /***************************************************************************
    * UPDATE
    ***************************************************************************/
    private updateValues(){
        let currentTarget  = this.searchTarget('current');
        if(isNotNull(currentTarget)){
          this.updateCurrentValue(currentTarget);
        }
        let previousTarget = this.searchTarget('previous');
        if(isNotNull(previousTarget)){
          this.updatePreviousValue(previousTarget);
        }
    }


    private updateCurrentValue(data){
      
      this.values.currentValue.previous = this.values.currentValue.current;      
      this.values.currentValue.current = this.extractData(data).value;
      if(isNotNull(this.values.currentValue) && isNotNull(this.values.currentValue.current)){

        let self = this;
        svg.animate.tweenValue(this.d3JsNode,
                               0,
                               500,
                               self.mainPreviousValue,
                               self.values.currentValue.current,
                               (data)  =>{self.valueStr = org.inugami.formatters.truncateNumber(data ,self.nbDigit) },
                               (onDone)=>{self.mainPreviousValue=self.values.currentValue.current}
                              );
      }
      
    }

  
    private updatePreviousValue(data){
      this.values.previousValue.previous = this.values.previousValue.current;      
      this.values.previousValue.current = this.extractData(data).value;

      let secondData = null;
      if(isNull(this.secondValueFormatter)){
        secondData = this.processFormatSecondValue(this.values.currentValue.current,this.values.previousValue.current);
      }else{
        secondData = this.secondValueFormatter(this.values.currentValue.current,this.values.previousValue.current);
      }


      if(isNotNull(secondData)){
        this.previousStr        = secondData.value;
        this.previousStyleClass = secondData.styleClass;
        this.previousUnit       = secondData.unit;
      }
    }


    private extractData(data){
      if(isNull(this.dataExtractor)){
        return org.inugami.data.extractors.graphite.simpleValue(data);
      }else{
        return this.dataExtractor(data);
      }
    }

    private processFormatSecondValue(current, previous){
      let localCurrent  = isNull(current)?0:current;
      let localPrevious = isNull(previous)?0:previous;

      let deltaValue = localCurrent- localPrevious;

      let diff = 0;

      if(isNotNull(localPrevious)){
        if(isNull(this.diffCalculator)){
          diff = (deltaValue / localPrevious)*100;
        }else{
          diff = this.diffCalculator(localCurrent,localPrevious);
        }
      }
      
      let valueResult =  org.inugami.formatters.truncateNumber(diff,this.nbDigit);
      let styleClass = "";
      if(diff<0){
        styleClass="negative";
      }else if(diff>=0){
        styleClass="positive";
      }else{
        styleClass="none";
      }

      let unit = this.secondValueUnit;
      return {
        'value'      : valueResult,
        'styleClass' : styleClass,
        'unit'       : unit
      }
    }
    /***************************************************************************
    * TOOLS
    ***************************************************************************/
    private searchTarget(targetName){
       let result = null;
       if(isNotNull(this.innerValue) && Array.isArray(this.innerValue)){
        for(let target of this.innerValue){
            if(target.target.includes(targetName)){
              result = target; 
              break;
            }
        }
       }
       return result;
    }
    
    getStyleClass(){
      return org.inugami.rendering.alertsStyles(this.styleClass,this.alerts); 
    }
    /***************************************************************************
    * IMPLEMENTS ControlValueAccessor
    ***************************************************************************/
    writeValue(value: any) {
      if (value !== this.innerValue) {
        this.innerValue = value;
        this.updateValues();
      }
    }

    registerOnChange(fn: any) {
      this.onChangeCallback = fn;
    }
    registerOnTouched(fn: any) {
      this.onTouchedCallback = fn;
    }
}