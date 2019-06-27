import {Component, Input,forwardRef,AfterViewInit}  from '@angular/core';
import {NG_VALUE_ACCESSOR,ControlValueAccessor}     from '@angular/forms';
  
import {Value}                                      from './../value/value';
  
export const CHART_VALUE_BLOC_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => ValueBloc),
  multi: true
};
  
  
@Component({
selector      : 'value-bloc',
directives    : [],
providers     : [CHART_VALUE_BLOC_ACCESSOR]
template      : `
  <div [ngClass]="'value-bloc'"
       [class]="styleClass" >

    <div [ngClass]="' value-bloc-state '" [class]="getState()">
        <div class="value-bloc-icon" >
            <i class="fa " [ngClass]="icon" aria-hidden="true" *ngIf="icon"></i>
        </div>

        <div class="value-bloc-value-area" *ngIf="innerValue">&nbsp;{{valueStr}}{{unit}}</div>
        <div class="value-bloc-value-area-end"></div> 

        <div>
            <div *ngIf="title" class="value-bloc-title">{{title}}</div>
            <div *ngIf="time" class="time">{{time}}</div>
        </div>
        <div class="value-bloc-end"></div>
    </div>
</div>
  `
  })
  export class ValueBloc extends Value implements ControlValueAccessor,AfterViewInit {
  
    /*****************************************************************************
     * ATTRIBUTES
    *****************************************************************************/
    @Input() icon                      : string;
    @Input() alertHandler              : any;

    /*****************************************************************************
     * GETTER
    *****************************************************************************/
    getState():string{
        let result = "";
        if(isNotNull(this.alertHandler)){
            result= this.alertHandler(this.innerValue);
        }
        return result;
    }
  }
  