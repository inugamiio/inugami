import {Component,
        Inject,
        OnInit,
        Input,
        Output,
        forwardRef,
        ViewChild,
        ViewEncapsulation}                                  from '@angular/core';

import {NG_VALUE_ACCESSOR,ControlValueAccessor}             from '@angular/forms';
import * as d3 from 'd3';

export const SIMPLE_GRAPH_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => SimpleGraph),
    multi: true
};


@Component({
  selector      : 'simple-graph',
  templateUrl   : 'js/app/components/charts/simple_graph/simple_graph.html',
  directives    : [],
  providers     : [SIMPLE_GRAPH_ACCESSOR],
  encapsulation : ViewEncapsulation.None
})
export class SimpleGraph implements ControlValueAccessor, OnInit {


  /*****************************************************************************
  * ATTRIBUTES
  *****************************************************************************/
  @ViewChild('container') chartContainer : ElementRef;
  @Input() styleClass                    : string;
  @Input() title                         : string;

  // values
  @Input() width                         : number;
  @Input() height                        : number;
  @Input() resolution                    : number;
  private  lastIndex                     : number;
  private  maxValue                      : number = 0;
  private  ratio                         : number = 0;
  private  widthMargin                   : number = 0.1;
  private  groundFloor                   : number = 0;

  private itemWidth                      : number;
  private itemMargin                     : number;

  // nodes
  private svg                            : any;  
  private layout                         : any;
  private values                         : number[] = [];
  private valuesNodes                    : any[]    = [];



  /*****************************************************************************
  * INIT
  *****************************************************************************/
  ngOnInit(){
  
    let container = this.chartContainer.nativeElement;

    this.svg    = d3.select(container)
                    .append('svg')
                    .attr('width',  this.width)
                    .attr('height', this.height);

    this._initValues();
    this._initLayout();
  }


  _initValues(){
      for(var i=this.resolution-1; i>=0 ; i--){
        this.values.push(0);
      }
      this._updateRatio();

      let allMargin   = this.width*0.1
      let nbItem      = this.resolution<1?1:this.resolution;
      this.itemMargin = allMargin / (nbItem<1?1:nbItem-1);
      this.itemWidth  = (this.width - allMargin) / nbItem;
  }

  /*****************************************************************************
  * RENDERING
  *****************************************************************************/
  _initLayout(){
    this.layout = this.svg.append("g")
                          .attr("class", "values");

    for(var i=0; i<this.resolution ; i++){
        this._renderItem(i);
    }
  }


  _renderItem(index){
      let node = this.layout.append("rect");
      this.valuesNodes.push(node);

      // compute X position
      let nbItems = index++;
      let posX    = (this.itemWidth*nbItems)+(this.itemMargin*index); 

      // set attr
      node.attr("class", "bar");
      node.attr("width", this.itemWidth);
      node.attr("height", 0);
      node.attr("x", posX);
  }


  _renderChartsValues(){
     for(var i=0; i<this.resolution ; i++){
        this._renderChartsValuesItem(i);
    }
  }

  _renderChartsValuesItem(index){
     let value = this.values[index];
     let node  = this.valuesNodes[index];
     let size  = value * this.ratio;

     let posY = this.groundFloor - size;
     node.attr("height", size);
     node.attr("y", posY);
  }

  /*****************************************************************************
  * UPDATE VALUES
  *****************************************************************************/
  _updateValue(value){
    var currentValue = this._notCorrectValue(value)?0:Number(value);
    this.values =this.values.slice(1, this.resolution);
    this.values.push(currentValue);

    if(currentValue> this.maxValue){
        this.maxValue = currentValue;
    }
    this._updateRatio();
    this._renderChartsValues();
  }

  _notCorrectValue(value){
    return isNull(value) || isNaN(value) || Number(value) < 0; 
  }

  _updateRatio(){
    if(this.maxValue == 0){
      this.ratio = 0;
    }else{
      this.ratio = (this.height / this.maxValue) * this.getItemRatioWithMargin(); 
    }

    this.groundFloor = this.height - ((this.height*this.widthMargin)/2);
  }


  /*****************************************************************************
  * GETTERS 
  *****************************************************************************/
  getItemRatioWithMargin(){
    return 1-this.widthMargin;
  }


  /*****************************************************************************
  * IMPLEMENTS ControlValueAccessor
  *****************************************************************************/
  writeValue(value: number) {
      this._updateValue(value);
  }

  registerOnChange(fn: any) {
    this.onChangeCallback = fn;
  }
  registerOnTouched(fn: any) {
    this.onTouchedCallback = fn;
  }
}