import {Component,
        Input,
        forwardRef,
        AfterViewInit}                                      from '@angular/core';

import {NG_VALUE_ACCESSOR,ControlValueAccessor}             from '@angular/forms';

export const EVOLUTION_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() =>EvolutionValue),
    multi: true
};


@Component({
  selector      : 'evolution-value',
  templateUrl   : 'js/app/components/charts/evolution_value/evolution.value.html',
  directives    : [],
  providers     : [EVOLUTION_VALUE_ACCESSOR]
})
export class EvolutionValue implements ControlValueAccessor,AfterViewInit {


    /***************************************************************************
    * ATTRIBUTES
    ***************************************************************************/
    private error                       : string = null;
    private logger                      : any    = LOGGER("EvolutionValue");
    private currentValue                : number;
    private previousValue               : number;
    private diff                        : number;

    private currentValueStr             : string;
    private diffPreviousStr             : string;
    private diffEvol                    : string;
    private diffEvolIcon                : string;

    @Input() styleClass                 : string;
    @Input() pluginName                 : string;
    @Input() event                      : string;
    @Input() nbDigit                    : number = 0;
    @Input() nbDigitDiff                : number = 1;
    @Input() unit                       : string;
    @Input() formatter                  : any    = io.inugami.formatters.truncateNumber;
    @Input() diffCalculator             : any;
    @Input() dataExtractor              : any;
    @Input() alerts                     : any[];


    /*****************************************************************************
    * INIT
    *****************************************************************************/
    ngAfterContentInit() {
        if(isNotNull(this.event)){
          let self= this;

          io.inugami.events.addEventListenerByPlugin(this.pluginName,this.event, function(event) {
              if (isNotNull(event.detail.data) && isNotNull(event.detail.data.values)) {
                self.alerts = event.detail.data.alerts;
                let values = event.detail.data.values;
                if(Array.isArray(values) && values.length==1){
                  self.writeValue(values[0]);
                }else{
                  self.writeValue(values);
                }

             }
          });
        }

    }


    /***************************************************************************
    * UPDATE
    ***************************************************************************/
    updateValue(){
        if(isNotNull(this.innerValue)){
          this.error=null;
          if(Array.isArray(this.innerValue)){
            this.updateFromMultiTarget();
          }else{
            this.updateFromSimpleEvent();
          }
          this.updateDiff();
          this.updateRenderingValues();
        }
    }

    updateFromMultiTarget(){
      if(this.checkDataMutliTargetValid()){
        let currentIndex = 0;
        let previousIndex = 1;

        if(this.innerValue[1].target.includes("current")){
          currentIndex=1;
          previousIndex=0;
        }

        this.currentValue = this.extractDataNumber(this.innerValue[currentIndex]);
        this.previousValue  = this.extractDataNumber(this.innerValue[previousIndex]);
      }else{
        this.logger.error("Invalid data! {0}",[this.innerValue]);
      }
    }

    checkDataMutliTargetValid(){
       return isNotNull(this.innerValue)    &&
              this.innerValue.length==2     &&
              isNotNull(this.innerValue[0]) &&
              isNotNull(this.innerValue[1]);
    }

    updateFromSimpleEvent(){
        this.previousValue = this.currentValue;
        this.currentValue = this.extractDataNumber(this.innerValue);
    }

    updateDiff(){
      if(isNull(this.previousValue)){
        this.previousValue= {value:0.0};
      }
      if(isNull(this.previousValue.value)){
        this.previousValue.value = 0.0;
      }

      if(isNull(this.currentValue)){
        this.currentValue = {value:0.0};
      }
      if(isNull(this.currentValue.value)){
        this.currentValue.value = 0.0;
      }

      if(this.previousValue.value !=0){
        if(isNull(this.diffCalculator)){
          let deltaValue = this.currentValue.value - this.previousValue.value;
          this.diff = (deltaValue / this.previousValue.value)*100;
        }else{
          this.diff = this.diffCalculator(this.currentValue.value, this.previousValue.value);
        }        
      }else{
        this.diff = 0;
      }
    }

    updateRenderingValues(){
      this.currentValueStr = this.formatNumber(this.currentValue.value,this.nbDigit);
      this.diffPreviousStr = this.formatNumber(this.diff,this.nbDigitDiff);

      if(this.diff>0){
        this.diffEvol="positive";
        this.diffEvolIcon="typcn typcn-arrow-up";
      }else if(this.diff<0){
        this.diffEvol="negative";
        this.diffEvolIcon="typcn typcn-arrow-down";
      }else{
        this.diffEvol="none";
        this.diffEvolIcon="typcn typcn-arrow-right";
      }

    }

    /***************************************************************************
    * EXTRACT & FORMAT
    ***************************************************************************/
    extractDataNumber(serie){
      let result = null;
      if(isNotNull(serie)){
        if(isNull(this.dataExtractor)){
          result = io.inugami.data.extractors.graphite.simpleValue(serie);
        }else{
          result = this.dataExtractor(serie);
        }
      }
      return result;
    }

    formatNumber(value,nbDigit){
      return this.formatter(value,nbDigit);
    }

    getStyleClass(){
      return io.inugami.rendering.alertsStyles(this.styleClass,this.alerts); 
    }
    /***************************************************************************
    * IMPLEMENTS ControlValueAccessor
    ***************************************************************************/
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
