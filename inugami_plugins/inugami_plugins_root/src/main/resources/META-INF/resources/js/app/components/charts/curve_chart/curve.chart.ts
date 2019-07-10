import {Component,
        Input,
        forwardRef,
        ViewChild,
        ViewEncapsulation,
        ElementRef}                                         from '@angular/core';

import {NG_VALUE_ACCESSOR,ControlValueAccessor}             from '@angular/forms';
import * as d3 from 'd3';


export const CURVE_CHART_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() =>CurveChart),
    multi: true
};


@Component({
  selector      : 'curve-chart',
  template      : `
      <div [ngClass]="'curve-chart'"
           [class]="styleClass" >

          <div class="curve-chart-container" #container></div>
      </div>
  `,
  directives    : [],
  providers     : [CURVE_CHART_ACCESSOR],
  encapsulation : ViewEncapsulation.None
})
export class CurveChart implements ControlValueAccessor,AfterViewInit {


    /***************************************************************************
    * ATTRIBUTES
    ***************************************************************************/
    @ViewChild('container') chartContainer : ElementRef;
    @Input() styleClass                    : string;
    @Input() resolution                    : number  = 288;

    //24H
    @Input() timeSlot                      : number  = 86400;
    @Input() startTime                     : number  = 18000;

    @Input() pluginName                    : string;
    @Input() event                         : string;
    @Input() eventPrevious                 : string;
    @Input() maxValue                      : number  = 0;
    @Input() autoScalMaxValue              : boolean = true;

    @Input() height                        : number  = null;
    @Input() width                         : number  = null;
    
    @Input() heightRatio                   : number  = null;
    @Input() widthRatio                    : number  = null;

    @Input() timeOffset                    : number  = 0;
    @Input() zoom                          : number  = 1;

    @Input() roundFactor                   : number  = 0.5;
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Input() nbTickLeft                    : number  = 5;
    @Input() nbTickBottom                  : number  = 12;

    @Input() nbFloatDigit                  : number  = 1;

    @Input() smallTickSizeRatio            : number  = 0.02;
    @Input() formatterTickBottom           : any     = null;
    @Input() valuesAggregator              : any     = org.inugami.data.aggregators.avg;

    @Input() tickValueFormatter            : any     = org.inugami.formatters.truncateNumber;

    @Input() tickValueAlign               : number  = 1;
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private parents                        : any[] = [];
    private dataInputCurrent               : any;
    private dataInputPrevious              : any;
    private innerFormatter                 : any;

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private values                         : any[]   = [];
    private slotSize                       : number;
    private ratios                         : any;
    private size                           : any;
    private compos                         : any;
    private position                       : any;

    private heightAuto                     : boolean;
    private widthAuto                      : boolean;
    private ratioHeight                    : number;
    private ratioWidth                     : number;

    private currentTime                    : number;
    private dataTypeCurrent                : string = "current";
    private dataTypePrevious               : string = "previous";
    
    
    /***************************************************************************
    * CONSTRUCTOR
    ***************************************************************************/
    constructor(private el: ElementRef){
          this.ratios={
              time           : 0, timeItem  : 0, timewidth : 0,
              tickTime       : 0, tickBottom: 0, 
              valueTick      : 0,
              tickValueAlign : 0.3,
              height         : 0,
              zoom           : 1,
              displayTime    : 1,
              sizeAuto       : {
                height : null,
                width  : null
              },
              margin : {
                 right     : 0.01, left            : 0.09,
                 top       : 0.02, bottom          : 0.03,
                 tickValue : 1.2 , valueTickMargin : 0.2
              }
          };
          

          
          this.size={
               right           : 0, left          : 0,
               top             : 0, bottom        : 0,
               width           : 0, height        : 0,
               timeItem        : 0,
               valueTickMargin : 0,
               valueTickSize   : 0
          };

          this.position = {
             axisX : null,
             axisY : null
          }

          this.compos = {
             svg    : null,
             defs   : null,
             layout : null,
             timeView : null,
             axis : {
                group  : null,
                x      : null, y     : null,yAxis     : null,
                xTick  : [],   yTick : [],
                yMasque: null
             },
             currentPosition : {
                groupTransform : null,
                group : null
             },
             data : {
               group       : null,
               groupCurves : null,
               groupArea   : null,
               values      : {

               }
             }
          };
    }

    

    /***************************************************************************
    * INIT
    ***************************************************************************/
    ngAfterContentInit() {
        this.heightAuto = isNotNull(this.heightRatio);
        this.widthAuto  = isNotNull(this.widthRatio);
        this.innerFormatter = this.getFormatterTickBottom();
        let self= this;

        // DATA SOURCES EVENTS LISTENERS
        if(isNotNull(this.event)){
          org.inugami.events.addEventListenerByPlugin(this.pluginName,this.event, function(event) {
              if (isNotNull(event.detail.data) && isNotNull(event.detail.data.values)) {
                self.writeValue(event.detail.data.values);
                self.processRefresh();
              }
          });
        }

        if(isNotNull(this.eventPrevious)){
          org.inugami.events.addEventListenerByPlugin(this.pluginName,this.eventPrevious, function(event) {
              if (isNotNull(event.detail.data) && isNotNull(event.detail.data.values)) {
                self.writePreviousValue(event.detail.data.values);
                self.processRefresh();
              }
          });
        }

        


        // OTHER EVENTS LISTENERS
        org.inugami.events.addEventListener(org.inugami.events.type.RESIZE, function(data){
          self.updateSize(data.detail);
          self.processRefreshWithoutSize();
        });


        org.inugami.events.addEventListener(org.inugami.events.type.UPDATE_CONFIGURATION, function(data){
          self.processRefresh();
        });

        setInterval(function(){
          self.processRefreshWithoutSize();
        }, 30000);

  
        if(this.heightAuto || this.widthAuto){
          this.computeDimension();
          this.updateSize(org.inugami.values.screen);
        }
        
        this.initData();
        this.computeRatios();
        this.renderLayout();
        this.manageCurrentPosition();
    }

    private initData(){
      this.slotSize = this.timeSlot/this.resolution;
      
      for(var i=0; i<this.resolution;  i++){
        let slotBeginValue = i*this.slotSize;
        let slotEndValue  = slotBeginValue+this.slotSize;
        if(i != (this.resolution-1)){
          slotEndValue = slotEndValue-1;
        }

        let data = {
          slotBegin: slotBeginValue,
          slotEnd  : slotEndValue,
          targets  : this.ratios.timeItem,
          index    : i
        }
        this.values.push(data);
      }
    }

    private initCurrentTime(){
       this.currentTime = this.reduceTimeFromDate(new Date()) + this.timeOffset;
    }

    /***************************************************************************
    * AUTO COMPO DIMENSION
    ***************************************************************************/
    private computeDimension(){
      let parents = svg.components.searchParent(this.el.nativeElement.parentElement, "BLOC");
      let ratio   = svg.math.computeDimension(parents,this.heightAuto,this.widthAuto);

      this.ratios.sizeAuto.height = ratio.height;
      this.ratios.sizeAuto.width  = ratio.width;
    }

    /***************************************************************************
    * COMPUTE
    ***************************************************************************/
    private computeRatios(){
        this.initCurrentTime();
        this.size.right           = this.width * this.ratios.margin.right;
        this.size.left            = this.width * this.ratios.margin.left;

        this.size.top             = this.height * this.ratios.margin.top;
        this.size.bottom          = this.height * this.ratios.margin.bottom;

        this.size.width           = this.width  - (this.size.left+this.size.right);
        this.size.height          = this.height - (this.size.top+this.size.bottom);

        

        this.ratios.zoom          = this.zoom<=1?1:this.zoom;
        this.ratios.tickTime      = this.timeSlot     / this.nbTickBottom;
        this.size.timeItem        = this.size.width   / this.resolution ;
        this.ratios.timeItem      = this.timeSlot     / this.resolution ;
        this.ratios.displayTime   = this.timeSlot     / this.ratios.zoom;

        this.ratios.time          = (this.size.width  / this.timeSlot);
        this.ratios.tickBottom    = ((this.size.width * this.ratios.zoom)/this.nbTickBottom);

        this.size.valueTickSize   = this.size.height  /(this.nbTickLeft);
        this.size.valueTickMargin = this.size.left*this.ratios.margin.valueTickMargin;

        this.position.axisX     = {
          x : this.size.left,
          y : this.height-this.size.bottom
        };

        
    }

  

    private computePointPosY(currentValue, previousValue){
      let delta =  currentValue - previousValue;
      return (delta * this.ratios.height*-1);
    }

    private computePointPositionX(index){
      let result = 0;
      if(index!=0){
        result = this.size.timeItem*this.ratios.zoom;
      }
      return result;
    }

    private computeAxisXTickItemLabel(index){
      let time = this.startTime+ (this.ratios.tickTime * index);
      return this.innerFormatter(time);
    }

    private computeTickXPosition(index){
      let posAxisX = this.position.axisX;
      let itemPosX = (posAxisX.x)+(this.ratios.tickBottom * index);
      return "translate(" + itemPosX + ",0)";
    }


    /***************************************************************************
    * UPDATE
    ***************************************************************************/

    
    private processRefresh(){
      if(this.heightAuto || this.widthAuto){
          this.computeDimension();
          this.updateSize(org.inugami.values.screen);
      }
      this.processRefreshWithoutSize();
    }

    private processRefreshWithoutSize(){
      this.computeRatios();
      this.updateHeightRatio();
      this.updateCurvesDataRatio();
     
      //rendering
      this.updateLayout();
      this.drawAllCurves();
      this.renderAxisY();
      this.manageCurrentPosition();
      
    }

      private manageCurrentPosition(){
      if(isNotNull(this.compos.currentPosition.path)){
        let timeRatio = ((this.size.width*this.ratios.zoom)/this.timeSlot);
        let localTime = this.currentTime-this.startTime;
        
        let timeViewOffset = this.computeTimeViewOffset(localTime) * timeRatio; 
        let offset    = localTime * timeRatio;

        this.compos.currentPosition.path.attr("transform","translate(" + offset + ",0)");
        this.compos.timeView.attr("transform","translate(" + timeViewOffset + ",0)");
      }


    }

    private computeTimeViewOffset(time){
        let displayTime         = this.timeSlot/this.ratios.zoom;
        let halfDisplayTime     = displayTime/2;
        let lastDisplayTime     = this.timeSlot - halfDisplayTime;


        let result = 0;

        if(time<=halfDisplayTime || time >this.timeSlot){
          result=0;
        }else if(time>=lastDisplayTime){
          result=lastDisplayTime-halfDisplayTime;
        
        }else{
          result =time-halfDisplayTime;
        }

        return result*-1;
    }

    private updateSize(data){

      if(isNotNull(this.ratios.sizeAuto.height)){
        this.height = data.height * this.ratios.sizeAuto.height;
      } 
      
      if(isNotNull(this.ratios.sizeAuto.width)){
        this.width  = data.width  * this.ratios.sizeAuto.width;
      }
    }

    
        
    

    private updateHeightRatio(){
      this.ratios.valueTick = this.maxValue    / this.nbTickLeft;
      this.ratios.height    = this.size.height / this.maxValue;
    }


    private updateCurveData(curveData,datapoint){
       let bucketIndex = this.searchBucketIndex(curveData, datapoint.timestamp, curveData.lastTimestamp);
       if(isNotNull(bucketIndex)){
          curveData.datapoints[bucketIndex].values.push(datapoint.value);
       }
       return bucketIndex;
    }

    
    /***************************************************************************
    * SEARCH
    ***************************************************************************/
    private searchForMaxValue(inputData){
      let currentMaxValue = 1;
      
      let targetName =isNull(this.compos.data)?[]:Object.keys(this.compos.data.values);
      if(targetName.length>0){
        for(let iTarget=targetName.length-1; iTarget>=0; iTarget--){
          let target =this.compos.data.values[targetName[iTarget]];
          if(isNotNull(target)){
            for(let indexData=target.datapoints.length-1; indexData>=0; indexData--){
              let data =target.datapoints[indexData];
  
              if(currentMaxValue<data.value){
                currentMaxValue=data.value;
              }
            }
          }else{
            console.log(inputData);
          }
          
        }
      }

     

      if(currentMaxValue!=this.maxValue){
        this.maxValue=currentMaxValue;
        this.updateHeightRatio();
      }
    }

    
    private searchBucketIndex(curveData, timestamp, targetLastTimestamp){
       let result = null;
       
        let simpleTime = this.reduceTime(timestamp);
            result = this.searchBucketByTime(simpleTime,curveData);
       
      return result;
    }

    private searchBucketByTime(time,curveData){
      let result = null;
      for(let i=curveData.datapoints.length-1; i>=0; i--){
          let bucketDataPoint = curveData.datapoints[i];
          if(time>=bucketDataPoint.begin && time<bucketDataPoint.end){
            result = i;
            break;
          }
      }
        return result;
    }

    /***************************************************************************
    * UPDATE RENDERING
    ***************************************************************************/
    private updateLayout(){
      this.compos.svg.attr('height',  this.height);
      this.compos.svg.attr('width',  this.width);
      

      let posAxisX = this.position.axisX;
      let transformData = "translate("   + posAxisX.x + "," + posAxisX.y + ")";
      let transform     = "translate(0," + posAxisX.y + ")";

      this.compos.data.group.attr("transform", transformData);
      this.compos.axis.x    .attr("transform", transform);
      if(isNotNull(this.compos.axis.y)){
        this.compos.axis.y    .attr("transform", transformData);
      }
      this.updateAxisX();
    }

    private updateCurvesDataRatio(){
      let keys = Object.keys(this.compos.data.values);
      for(let key of  keys){
        let target  = this.compos.data.values[key];
        this.updateCurvesDataRatioTarget(target);
      }
    }

    private updateCurvesDataRatioTarget(target){
      for(let i=0; i<target.datapoints.length; i++){
        this.updateCurvesDataRatioTargetDatapoint(target.datapoints[i],i);
      }
    }

    private updateCurvesDataRatioTargetDatapoint(datapoint, index){
      datapoint.x=this.computePointPositionX(index);
    }

    private updateAxisX(){
        for(let i=0; i<this.compos.axis.xTick.length; i++){
          let tick = this.compos.axis.xTick[i];
          tick.group.attr("transform",this.computeTickXPosition(i));
        }
    }
    /***************************************************************************
    * RENDER
    ***************************************************************************/
    private renderLayout(){
      let container               = this.chartContainer.nativeElement;
      this.compos.svg             = svg.builder.svgContainer(container,this.height,this.width);

      this.compos.defs            = this.compos.svg.append("defs");
      this.compos.layout          = this.compos.svg.append("g").attr("class", "layout");
      
      this.compos.timeView        = this.compos.layout.append("g").attr("class", "time-view");
      var posAxisX                = this.position.axisX;
      this.compos.data.group      = this.compos.timeView.append("g")
                                                        .attr("class", "data")
                                                        .attr("transform", "translate(" + posAxisX.x + "," + posAxisX.y + ")");

      this.compos.data.groupArea        = this.compos.data.group.append("g").attr("class", "data-area");
      this.compos.data.groupCurves      = this.compos.data.group.append("g").attr("class", "data-curves");
      this.compos.currentPosition.group = this.compos.data.group.append("g").attr("class", "current-position");

      this.compos.axis.group            = this.compos.layout.append("g").attr("class", "axis");

      this.renderAxisX();
      this.renderAxisY();
      this.renderCurrentPosition();
    }



    private renderAxisX(){
      var posAxisX = this.position.axisX;

      if(isNull(this.compos.axis.x)){
        this.compos.axis.x = this.compos.timeView.append("g").attr("class", "axis-x");
        this.compos.axis.x.attr("transform", "translate(0," + posAxisX.y + ")");
      }

      if(this.compos.axis.xTick.length==0){
         for(let i=0; i<this.nbTickBottom; i++){
            let tickGrp        = this.compos.axis.x.append("g").attr("class", "axis-x-tick");
            let tickLabelAlign = tickGrp.append("g");
            let tickLabel      = tickLabelAlign.append("svg:text");
                tickLabel.text(this.computeAxisXTickItemLabel(i));

            let textSize = svg.components.size(tickLabel);

            let tick           = tickGrp.append("path")
                                   .attr("class","tick-small")
                                   .attr("d",this.drawLine([this.buildPoint(0,0),this.buildPoint(0,-textSize.height*2)]));

            this.compos.axis.xTick.push(
              {
                "group":tickGrp,
                "align":tickLabelAlign,
                "label":tickLabel,
                "tick" :tick
              }
            );
         }
      }

      
      for(let i=0; i<this.compos.axis.xTick.length; i++){
          this.renderAxisXItem(i);

      }
    }

    private renderAxisXItem(index){
      let itemPosX = this.computeTickXPosition(index);
      let item  = this.compos.axis.xTick[index];
          item.group.attr("transform",itemPosX );

      let textSize = svg.components.size(item.label);
      let alignText = (textSize.width/2)*-1;
      item.align.attr("transform","translate(" + alignText + ",0)");
    }



    private renderAxisY(){
      this.updateAxisYSize();
      if(this.maxValue!=0 && isNotNull(this.nbTickLeft) && this.nbTickLeft>0){

        if(isNull(this.compos.axis.y)){
          let posAxisX = this.position.axisX;
          this.compos.axis.y = this.compos.axis.group.append("g");
          this.compos.axis.y.attr("class", "axis-y");
          this.compos.axis.y.attr("transform", "translate(" + posAxisX.x + "," + posAxisX.y + ")");
          this.initRenderAxisY();
        }
  
        for(let i=0; i<this.nbTickLeft; i++){
          this.updateRenderAxisYTick(i);
        }
      }
    }

    private initRenderAxisY(){
      this.compos.axis.yMasque =this.compos.axis.y.append("rect").attr("class", "axis-y-mask");
      this.refreshYMasque();                                                  
      
      let axis =  this.compos.axis.y.append("path")
                                    .attr("class", "axis-y-path");

      this.compos.axis.y.yAxis=axis;
      this.updateAxisYSize();
      for(let i=1; i<=this.nbTickLeft; i++){
        this.initRenderAxisYTick(i);
      }
    }

    private updateAxisYSize(){
      let path = [
          this.buildPoint(0,0),
          this.buildPoint(0,-this.size.height)
      ];
      if(isNotNull(this.compos.axis.y)){
        this.compos.axis.y.yAxis.attr("d",this.drawLine(path));
      }
    }
    private refreshYMasque(){
      this.compos.axis.yMasque.attr("height", this.height);
      this.compos.axis.yMasque.attr("width" , this.width);
      this.compos.axis.yMasque.attr("x"     ,-this.width);
      this.compos.axis.yMasque.attr("y"     ,-this.height-this.size.bottom);
    }

    private initRenderAxisYTick(tickIndex){
      let posY = tickIndex * this.size.valueTickSize * -1;
      let groupCss  = ['axis-y-tick'];
      if(tickIndex %2==0){
        groupCss.push("even");
      }

      let group = this.compos.axis.y.append("g")
                                    .attr("class",groupCss.join(" "))
                                    .attr("transform", "translate(0," +posY+")");

      let align = group.append("g").attr("class","align");
      let text  = align.append("svg:text").attr("class","tick-value");
      let rule  = group.append("path")
                      .attr("class","tick-rule")
                      .attr("d",this.drawLine([this.buildPoint(0,0),this.buildPoint(this.size.width+this.size.left,0)]));

      let smallTickSize = this.size.width * this.smallTickSizeRatio;
      let smallTickPosX = (smallTickSize/2)*-1;
      let smallRule = group.append("path")
                           .attr("class","tick-small-rule")
                           .attr("transform", "translate("+smallTickPosX+",0)")
                           .attr("d",this.drawLine([this.buildPoint(0,0),this.buildPoint(smallTickSize,0)]));


      let result = {
        'group':group,
        'text':text,
        'align':align,
        'rule':rule,
        'smallRule':smallRule
      };    

      this.compos.axis.yTick.push(result);
    }

    private updateRenderAxisYTick(tickIndex){
      let tick = this.compos.axis.yTick[tickIndex];
      let value = this.ratios.valueTick*(tickIndex+1);
      let grpPosY = ((tickIndex+1) * this.size.valueTickSize)* -1;

      let strValue = this.tickValueFormatter(value, this.nbFloatDigit);
      
      tick.text.text(strValue);

      let textSize = svg.components.size(tick.text);
      

      let posX     = -(textSize.width+this.size.valueTickMargin);
      let posY     = (textSize.height*this.ratios.tickValueAlign);
      tick.align.attr("transform", "translate("+posX+","+posY+")");
      

      tick.group.attr("transform", "translate(0," +grpPosY+")");
    }



    private renderCurrentPosition(){
      this.compos.currentPosition.path= this.compos.currentPosition.group.append("path");
      this.compos.currentPosition.path.attr("class","tick-small-rule");
      this.compos.currentPosition.path.attr("d",this.drawLine([this.buildPoint(0,0),this.buildPoint(0,-this.size.height)]));
    }

    /***************************************************************************
    * RENDER CURVES
    ***************************************************************************/
    private renderCurves(){
        this.updateCurvesData(this.dataInputCurrent,  this.dataTypeCurrent);
        this.updateCurvesData(this.dataInputPrevious, this.dataTypePrevious);
    }

    private updateCurvesData(data, dataType){
        if(isNotNull(data)){
          if(Array.isArray(data)){
            for(let tIndex=0;tIndex <data.length; tIndex++ ){
              let targetData =data[tIndex];
              if(isNotNull(targetData)){
                 this.renderCurveTarget(data[tIndex],dataType);
              }
           }
          }else{
            this.renderCurveTarget(data,dataType);
          }
            
        }
    }


    private renderCurveTarget(target, dataType){
        let key = [dataType, target.target].join('_');
        let curveData = this.compos.data.values[key];
        
        if(isNull(curveData)){
          curveData = this.buildCurveData(target.target, dataType,key);
          this.compos.data.values[key]=curveData;
        }
        

        let hasError = false;
        let bucketIndexies = [];
        let previousBucketIndex = null;

        let bucketIndexies = [];
        for(let index =0;index< target.datapoints.length; index ++){
          let datapoint = target.datapoints[index];
          let  bucketIndex = this.updateCurveData(curveData,datapoint);
          

          if(isNull(previousBucketIndex)){
            previousBucketIndex =bucketIndex; 
          }
          let bucketDiff =  bucketIndex-previousBucketIndex;
          hasError = bucketDiff>1 || bucketDiff<-1; 

          if(hasError && isNotNull(bucketIndex)){
            bucketIndexies.push(bucketIndex);
          }
          previousBucketIndex =bucketIndex; 

        }

        for(let index = 0; index<previousBucketIndex;index++){
          let currentValue = this.extractCurveData(curveData,index);
          if(isNull(currentValue)) {
              bucketIndexies.push(index);
          }
        }
        
        if(bucketIndexies.length>0){
          this.resolveErroneousData(bucketIndexies,target, dataType,curveData);
        }

        this.aggregateAndReduceValues();
        this.drawCurves(dataType);
    }

    private resolveErroneousData(bucketIndexies,target, dataType,curveData){
        let indexies = this.sortBucketIndexies(bucketIndexies)                                          
        let groups = this.groupBucketIndexies(indexies);
            groups = this.searchBucketBounds(groups,curveData);
                   
        for(let group of groups){
          let nbDiffBucket = group.indexies.length;
          let valueStart = this.extractCurveData(curveData,group.begin);
          let valueEnd   = this.extractCurveData(curveData,group.end);
          let diffValue = (valueEnd - valueStart)/nbDiffBucket;

           for(let virtualBucketIndex=1;virtualBucketIndex<=nbDiffBucket;virtualBucketIndex++){
             let virtualPointValue = valueStart+(diffValue*virtualBucketIndex);
             curveData.datapoints[group.indexies[virtualBucketIndex-1]].values.push(virtualPointValue);
          }
        }

    }
   

    private sortBucketIndexies(indexies){
      return indexies.sort(function compare(a, b) {
                           if (a <b){return -1;}
                           if (a >b){return 1;}
                           return 0;
                         });
    }

    private groupBucketIndexies(indexies){
        let result = [];
        let groups = [];
        let cursor = null;
        let lastIndex = indexies.length-1;

        for(let i=0;i<indexies.length; i++){
          let currentIndex =  indexies[i];
          if(cursor==null){
            cursor = currentIndex;
          }

          let diff = cursor-currentIndex;

          if(diff>1 || diff<-1){
            result.push({"indexies":groups})
            groups = [];
          }
          
          groups.push(currentIndex);
          cursor = currentIndex;
          if(i==lastIndex){
            result.push({"indexies":groups})
          }
        }
        return result;
    }

    private searchBucketBounds(groups,curveData){
      let result = [];
      for(let group of groups){
         let sortedIndexies =  this.sortBucketIndexies(group.indexies);
         let begin = sortedIndexies[0]-1;
         let end = sortedIndexies[sortedIndexies.length-1]+1;
         let datapointsSize = curveData.datapoints.length;

         if(end>datapointsSize){
           end = 0;
         }
          if(begin<0){
            begin = datapointsSize-1;
          }

          result.push({
            "begin":begin,
            "end":end,
            "indexies":group.indexies
          });
         
      }
        return result;
    }

    private extractCurveData(curveData:any, index:number){
      let data = curveData.datapoints[index];
      return this.valuesAggregator(data.values);; 
    }

    // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    // 
    // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private drawAllCurves(){
      this.drawCurves(this.dataTypePrevious);
      this.drawCurves(this.dataTypeCurrent);
    }
    

    private drawCurves(dataType){
      let keys = Object.keys(this.compos.data.values);
      for(let key of  keys){
        if(key.startsWith(dataType)){
          let target  = this.compos.data.values[key];
          this.drawTargetCurves(target,dataType);
        }
      }
    }


    /**
    * for draw a curve we will use a SVG:path. All points will be define in "d"
    * attribut. The area has same datapoints than curve, but closed. For
    * close  a path you must end points delaration by "z" char and have one more
    * point at y=0.
    .
    * All path must begin by "m" char.
    **/
    private drawTargetCurves(target,dataType){
      let isCurrent = this.dataTypeCurrent== dataType;

      let shape = this.buildShape(target.datapoints,isCurrent);

      if(isNotNull(shape) && ""!=shape){
        let area = this.buildShapeArea(shape,target.datapoints,isCurrent);
        target.compos.curve.attr("d","m "+shape);
        target.compos.area.attr("d","m "+ area + " z");
      }
    }
    
    


    /***************************************************************************
    * AGGREGATE VALUES
    ***************************************************************************/
    private aggregateAndReduceValues(){
      let keys = Object.keys(this.compos.data.values);
      for(let key of  keys){
        let target  = this.compos.data.values[key];
        this.aggregateTargetValuesAndReduce(target);
      }
    }

    private aggregateTargetValuesAndReduce(target){
      for(let i =target.datapoints.length-1; i>=0;i--){
         let result = target.datapoints[i]; 
         let value = this.valuesAggregator(result.values);
         if(isNotNull(value)){
            result.valuePrevious = result.value;
            result.value         = value;
            
            target.datapoints[i] = result;
         }
      }
    }



    /***************************************************************************
    * BUILD 
    ***************************************************************************/
    private buildPoint(x,y){
       return [x,y].join(',');
    }

    private drawLine(points){
      return "m "+points.join(" ");
    }

    private buildTimeSlot(index){
      let timeBegin = (index*this.ratios.timeItem)+this.startTime;
      if(timeBegin>=this.timeSlot){
         let timeOverlay = Math.floor(timeBegin/this.timeSlot);
         timeBegin  = timeBegin-(timeOverlay *this.timeSlot);
      }

      let timeEnd   = timeBegin+this.ratios.timeItem;
      return {
        begin : timeBegin,
        end   : timeEnd
      }
    }

     /**
    * for draw a curve we will use a SVG:path. All points will be define in "d"
    * attribut. The area has same datapoints than curve, but closed. For
    * close  a path you must end points delaration by "z" char and have one more
    * point at y=0.
    .
    * All path must begin by "m" char.
    **/
    private buildCurveData(targetName, currentStyleClass, compoId){
      let curveCss     =  ["data-target",currentStyleClass, targetName].join(' ');

      this.buildGradientDefinition(compoId);


      let currentGrpArea = this.compos.data.groupArea.append("g")
                                                     .attr("class",curveCss);
      
      let currentGrpCurve = this.compos.data.groupCurves.append("g")
                                                        .attr("class",curveCss);          
                                         
      let targetComponents = [];

      for(let i=0; i<this.resolution; i++){
         let timeSlot = this.buildTimeSlot(i);

         let posX = this.computePointPositionX(i);
         let datapoint  = {
            x:posX,
            y:0,
            begin:timeSlot.begin,
            end:timeSlot.end,
            value:null,
            valuePrevious:null,
            values:[]
         };

         targetComponents.push(datapoint);
      }

      return {
        lastTimestamp:0,
        name:targetName,
        styleClass:currentStyleClass,
        datapoints : targetComponents,
        compos : {  
           curve : currentGrpCurve.append("path").attr("class","curve"),
           area  : currentGrpArea.append("path")
                                 .attr("class","area")
                                 .attr("fill",["url(#",compoId,")"].join(""))
        }
      }
    }




    private buildGradientDefinition(compoId){
      let gradient = this.compos.defs.append("linearGradient")
                                     .attr("id",compoId)
                                     .attr("x1","0%")
                                     .attr("y1","0%")
                                     .attr("x2","0%")
                                     .attr("y2","100%");

      let offsetBegin  = gradient.append("stop")
                                 .attr("offset","0%")
                                 .attr("class",compoId+"-begin");

      let offsetEnd  = gradient.append("stop")
                               .attr("offset","100%")
                               .attr("class",compoId+"-end");


    }

    /**
     * In SVG, a shape is define by points. One point define its coordinates
     * by string representation : <strong>x,y</strong>. All points are separate
     * by space char. 
     * 
     * For 2 points define by :
     * <ul>
     *  <li>first point : X=70.7 and Y=262.42289</li>
     *  <li>first point : X=57.57869 and Y=-95.96449</li>
     * </ul>
     * 
     * Path will be define by :
     * <pre>
     * <code>
     *    70.7,262.42289 57.57869,-95.96449
     * </code>
     * </pre>
     * 
     * <strong>All points positions in shape are relative from first point</strong>
     * 
     *     TODO: to round  : https://developer.mozilla.org/en-US/docs/Web/SVG/Tutorial/Paths
                      http://blogs.sitepointstatic.com/examples/tech/svg-curves/cubic-curve.html
     * @param datapoints current target data points
     */
    private buildShape(datapoints,isCurrentCurve){
      let result = [];

      let size = datapoints.length;
      for(let i = 0;i<size;i++){
          let point = [];
          let data = datapoints[i];

          if(isCurrentCurve){
            if(this.isDataAfterCurrentTime(i,datapoints)){
              break;
            }
          }
      
          let posY = 0;
          if(i==0){
             posY = (data.value * this.ratios.height*-1);
          }else{
            posY = this.computePointPosY(data.value,datapoints[i-1].value); 
          }

          point.push(data.x);
          point.push(posY);

          result.push(point.join(","));
      }

      return result.join(" ");
    }

    private isDataAfterCurrentTime(index,datapoints){
      let isAfter = datapoints[index].begin>this.currentTime;
      let hasMoreData = false;
      if(!isAfter) {
         let size = datapoints.length;
         for(let nextIndex = index+1; nextIndex<size;nextIndex++){
            hasMoreData = isNotNull(datapoints[nextIndex]) && 
                          isNotNull(datapoints[nextIndex].values) && 
                          datapoints[nextIndex].values.length>0;
            if(hasMoreData){
              break;
            }
         }
      }
      return isAfter || !hasMoreData ;
    }

    private buildShapeArea(shape,datapoints, isCurrentCurve){
      let result = []; 
      let first = "";
      let last  = "";

      if(datapoints.length>0){
        let firstPoint = datapoints[0];
        let lastIndex = this.extractLastDataPointIndex(datapoints,isCurrentCurve);
        let lastPoint = datapoints[lastIndex];
        let lastValue = isNull(lastPoint)? 0:lastPoint.value;

        first = this.buildPoint(0,0);
        last  = this.buildPoint(0,this.computePointPosY(0,lastValue));

      }

      result.push(first);
      result.push(shape);
      result.push(last);

      return result.join(" ");
    }


    private extractLastDataPointIndex(datapoints,isCurrentCurve){
      let size =  datapoints.length;
      let result = size-1;

      if(isCurrentCurve){
          for(let i = 0;i<size;i++){
            let data = datapoints[i];
            if(this.isDataAfterCurrentTime(i,datapoints)){
              result = i-1;
              break;
            }
          }
      }

      return result;
    }

    /***************************************************************************
    * TOOLS
    ***************************************************************************/
    private reduceTime(timestamp){
      let time = new Date(timestamp   * 1000);
      return this.reduceTimeFromDate(time);
    }

    private reduceTimeFromDate(date){
      let result  = (date.getHours()  * 3600);
      result += date.getMinutes() * 60;
      result += date.getSeconds();

  return result;
    }

    /***************************************************************************
    * IMPLEMENTS ControlValueAccessor
    ***************************************************************************/
    writePreviousValue(value: any) {
      if(isNotNull(value)){
        this.cleanCacheValues(this.dataTypePrevious);
        this.dataInputPrevious = value;
        this.searchForMaxValue(this.dataInputPrevious);
        this.updateCurvesData(this.dataInputPrevious, this.dataTypePrevious); 
      }
    }


    

    writeValue(value: any) {
      if(isNotNull(value)){
        this.cleanCacheValues(this.dataTypeCurrent);
        this.dataInputCurrent = value;
        this.searchForMaxValue(this.dataInputCurrent);
        this.updateCurvesData(this.dataInputCurrent,  this.dataTypeCurrent);
      }
    }

    

    registerOnChange(fn: any) {
      this.onChangeCallback = fn;
    }
    registerOnTouched(fn: any) {
      this.onTouchedCallback = fn;
    }

    /***************************************************************************
    * CLEAN
    ***************************************************************************/
    private cleanCacheValues(dataType){
      let keys = Object.keys(this.compos.data.values);
      for(let key of  keys){
        if(key.startsWith(dataType)){
          let target  = this.compos.data.values[key];
          this.cleanCacheValuesOnTarget(target);
        }
      }
    }


    private cleanCacheValuesOnTarget(target){
      for(let datapoint of  target.datapoints){
        datapoint.values=[];
      }
    }

    private cleanAllCaches(){
      let keys = Object.keys(this.compos.data.values);
      for(let key of keys){
         let dataCurve = this.compos.data.values[key];
         
         for(let datapoint of dataCurve.datapoints){
            datapoint.valuePrevious = datapoint.value;
            datapoint.value=null;
            datapoint.values=[];
         }
      }
    }

    /***************************************************************************
    * GETTERS
    ***************************************************************************/
    private getFormatterTickBottom(){
       return org.inugami.formatters.selectFormatter(this.formatterTickBottom,
                                                  org.inugami.formatters.time.simpleTimeMin);

    }
}
