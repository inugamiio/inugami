import {Component,
        Input,
        forwardRef,
        ViewChild,
        ViewEncapsulation,
        ElementRef,
        AfterViewInit}                                from '@angular/core';
      
import {NG_VALUE_ACCESSOR,ControlValueAccessor}       from '@angular/forms';
import {SvgComponent}                                 from './../svg_component/svg.component';

export const CHART_VALUE_CHART_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => ValueChart),
  multi: true
};
  
  
@Component({
selector      : 'value-chart',
directives    : [],
providers     : [CHART_VALUE_CHART_ACCESSOR],
encapsulation : ViewEncapsulation.None,
template      : `
<div [ngClass]="' value-chart svg-component '+styleClass"
     [class]="alertLevel+' '+cssNullValue" 
     [style.width.px]="size.svg.width">
    <span style="display:none">event name : {{buildEventName()}}</span>
    <div [ngClass]="componentBaseStyleClass" >
        <div  [ngClass]="'msg-level '" [class]="alertLevel">
            <div *ngIf="icon" 
                 class="value-chart-icon" >
                <i [class]="icon" [style.width.px]="size.svg.width"></i>
            </div>
            
            <div *ngIf="valueStr"
                 class="value-chart-value"
                 [style.width.px]="size.svg.width">
                <div class="value-chart-value-value">{{valueStr}}<span class="value-chart-value-unit" *ngIf="notNullValue()">{{unit}}</span></div>
                <div class="value-chart-value-end"></div>
            </div>

            <div class="svg-container"
                 #container
                 [style.height.px]="size.svg.height"
                 [style.width.px]="size.svg.width"></div>
            <div class="svg-container-end"></div>
            <div class="value-chart-alert" *ngIf="alertLevel">
                <div class="icon"></div>
            </div>
        </div>
    </div>
    <div class="svg-component-end"></div>
</div>
`
})
export class ValueChart extends SvgComponent implements ControlValueAccessor,AfterViewInit {
    
    /*****************************************************************************
     * ATTRIBUTES
    *****************************************************************************/
    @ViewChild('container') chartContainer  : ElementRef;
    
    @Input() icon                           : string;
    @Input() definition                     : number = 5;
    @Input() dataExtractor                  : any;
    @Input() unit                           : string;
    @Input() minValue                       : number;
    @Input() maxValue                       : number;
    @Input() chartRatio                     : number = 0.8;
    @Input() formatter                      : any ;
    @Input() nullValue                      : string = "--";
    @Input() valueLimit                     : number;
    @Input() valueLimitLabel                : string;


    private lastIndex                       : number = 4;
    private innerMinValue                   : number;
    private innerMaxValue                   : number;
    private cssNullValue                    : string = "";

    private animeCurrentValueDuration       : number = 1000;
    private previousValue                   : number = null;
    private currentValue                    : number = null;
    private valueStr                        : string;


    private alertLevel                      : string = "";
    private data                            : any  = {};

    // SVG components    
    private layoutCompo : any = {
        layout              : null,
        data                : null,
        areas               : null,
        curves              : null

    }

    // size
    private localSize : any = {
        svgContainerSize    : null,
        itemMargin          : 0,
        ratioItemHeight     : 0,
        marginTop           : 0.9
    };
    

    /*****************************************************************************
    * POST CONSTRUCT
    *****************************************************************************/
    afterContentInit(){
        let self = this;
        if(isNull(this.width)){
            this.width = '100px';
        }
        if(isNull(this.height)){
            this.height = '150px';
        }
        this.lastIndex = this.definition-1;
    }

    /*****************************************************************************
    * HANDLER EVENT
    *****************************************************************************/
    /*@Override*/
    public onData(event:any){
        if(isNotNull(event.detail.data) && 
           isNotNull(event.detail.data.values)){
                this.processInCommingData(event.detail.data);
        }
    }

    private processInCommingData(data){
        this.checkIfalertPresent(data);

        if(isArray(data.values)){
            for(let i=0; i< data.values.length; i++){
                if(isNotNull(data.values[i]) && isNotNull(data.values[i].target)){
                    this.processInitOrUpdateData(data.values[i]);
                }
            }
        }else{
            this.processInitOrUpdateData(data.values);
        }
        
        this.computeComponentRatio();

        let keys = Object.keys(this.data);
        for(let key of keys){
            let currentData= this.data[key];
            this.reRenderCurveAndArea(currentData);
            this.updateOldValues(currentData);
        }

        this.updateCurrentValue();
    }


    private updateOldValues(currentData){
        currentData.values.old=currentData.values.current;
    }

    private checkIfalertPresent(data){
        let alert = io.inugami.data.extractors.alertMax(data);
        if(isNotNull(alert)){
            this.alertLevel = [alert.levelType, alert.level].join(' ');
        }else{
            this.alertLevel = null;
        }
    }


    private updateCurrentValue(){
        this.cssNullValue = "";
        let currentData = this.getCurrentData();
        if(isNotNull(currentData)){
            this.previousValue = 0;
            if(isNotNull(this.currentValue)){
                this.previousValue =  this.currentValue;
            }

            let lastValue= currentData.rawData[currentData.rawData.length-1];
            if(isNull(lastValue)){
                this.valueStr = this.nullValue;
                this.cssNullValue = "null-value";
            }else{
                this.currentValue = this.extractData(isNotNull(lastValue.value)?lastValue.value:0);
                this.animateCurrentValue();
            }
        }
    }

    private animateCurrentValue(){
        let self = this;
        let localFormatter = isNotNull(this.formatter)?this.formatter:function(value){
            return io.inugami.formatters.truncateNumber(value,1);
        }

        let enableLimit = isNotNull(this.valueLimit);

        svg.animate.tweenValue(this.layoutCompo.data,0,
            this.animeCurrentValueDuration,
            this.previousValue,
            this.currentValue,
            (data)  =>{
                if(enableLimit && data>= self.valueLimit){
                    self.valueStr = self.valueLimitLabel;
                }else{
                    self.valueStr = localFormatter(data);
                }
            }
        );
    }

    /*****************************************************************************
    * MANAGE DATA
    *****************************************************************************/
    private processInitOrUpdateData(targetData){
        let currentData = this.data[targetData.target];

        if(isNull(currentData)){
            currentData = this.processInitData(targetData);
        }
        this.processUpdateData(currentData,targetData);
    }


    private processInitData(targetData){
        let area  = this.layoutCompo.areas.append("path").attr("class" , "area-path "+targetData.target);
        let curve = this.layoutCompo.curves.append("path")
                                           .attr("shape-rendering", "auto")
                                           .attr("class", "curve-path "+ targetData.target);
        

        let currentValues = [];
        let rawDataItems  = [];
        for(let i=0; i<this.definition; i++){
            currentValues.push(null);
            rawDataItems.push(null);
        }

        this.data[targetData.target] = {
            name   : targetData.target,
            values : {
                old     : [].concat(currentValues),
                current : [].concat(currentValues)
            },
            rawData : rawDataItems,
            components : {
                "curve" : curve,
                "area"  : area
            }
        }
        return this.data[targetData.target];
    }
    


    private processUpdateData(currentData,targetData){
        
        let cursor          = this.definition;
        let nbItemAdded     = 0;
        let datapoints      = this.cleanDataPoints(targetData.datapoints);
        currentData.rawData = datapoints;

        for(let i=datapoints.length-1; i>=0; i--){
            cursor--;
            if(cursor<0){break;}

            let value                          = this.extractData(datapoints[i].value);
            currentData.rawData[cursor]        = datapoints[i];
            currentData.values.current[cursor] = value;
            this.computeMinMaxValue(value);
            nbItemAdded++;            
        }

        if(nbItemAdded<this.definition){
            let diff = this.definition - nbItemAdded;
            for(let i=0; i<diff; i++){
                cursor--;
                if(cursor<0){break;}

                let value                          = this.extractData(0);
                currentData.rawData[cursor]        = null;
                currentData.values.current[cursor] = value;
                this.computeMinMaxValue(value);
            }
        }
    }

    private cleanDataPoints(datapoints){
        let result = [];
        if(isNotNull(datapoints)){
            let size = datapoints.length;
            for(let i=0; i<size; i++){
                if(isNotNull(datapoints[i]) && isNotNull(datapoints[i].value)){
                    result.push(datapoints[i]);
                }
            }
        }
        return result;
    }

    private computeMinMaxValue(value){
        if(isNull(this.innerMinValue) || this.innerMinValue>value){
            this.innerMinValue = value;
        }

        if(isNull(this.innerMaxValue) || this.innerMaxValue<value){
            this.innerMaxValue =value;
        }
    }
    
    private extractData(value){
        let result = value;
        if(isNotNull(this.dataExtractor)){
            result= this.dataExtractor(value);
        }
        return result;
    }
    /*****************************************************************************
    * COMPUTE SIZE
    *****************************************************************************/
    /*@Override*/
    private computeSize(size){
        this.localSize.svgContainerSize=size;
        
        
        let nbPointsInComponents = (this.definition-1);
        if(nbPointsInComponents<=0){
            nbPointsInComponents = 1;
        }
        this.localSize.itemMargin = size.width / nbPointsInComponents;

    }

    /*@Override*/
    public updateComponentSize(size){

    }

    private computeComponentRatio(){
        let min = isNull(this.minValue)? this.innerMinValue:this.minValue;
        let max = isNull(this.maxValue)? this.innerMaxValue:this.maxValue;

        let totalValue                 = max - min;
        this.localSize.compoHeight     = this.localSize.svgContainerSize.height*this.chartRatio;
        this.localSize.ratioItemHeight = (this.localSize.svgContainerSize.height*this.chartRatio)/totalValue;

        svg.transform.translateY(this.layoutCompo.layout, (this.localSize.svgContainerSize.height-this.localSize.compoHeight));
        
    }
    /*****************************************************************************
    * RENDERING
    *****************************************************************************/
    private renderLayout(svgComponent){
        this.layoutCompo.layout=svgComponent.append("g").attr("class", "value-chart-layout");
        this.layoutCompo.data=this.layoutCompo.layout.append("g");
        this.layoutCompo.areas=this.layoutCompo.layout.append("g").attr("class", "value-chart-area-layout");
        this.layoutCompo.curves=this.layoutCompo.layout.append("g").attr("class", "value-chart-curves-layout");
    }

    private reRenderCurveAndArea(data){
        this.processAnimation(this.layoutCompo.layout,data,this.lastIndex)
            
    }


    private processAnimation(component,data, cursor){
        let self     = this;
        let duration = 200;

        if(cursor>=0){
            svg.animate.tween(component,0,duration,
                function(time){
                    self.animateRenderCurveAndArea(data,time,cursor);
                },
                null,
                function(){
                    let newCursor = cursor - 1;
                    self.processAnimation(component,data,newCursor);
                }
            ); 
        }
    }

    private animateRenderCurveAndArea(data, diffOffset, index){
        let curveShape = [];
        for(let i =this.lastIndex; i>=0; i--){
            let posX               = i==this.lastIndex ? this.localSize.svgContainerSize.width : (this.localSize.itemMargin*-1);
            let previousPointData  = i==this.lastIndex ? 0 : data.values.current[i+1];
            
            let posY = this.coumputeDiffItemValue(data.values.current[i],
                                                  data.values.old[i],
                                                  diffOffset,
                                                  previousPointData,
                                                  i,
                                                  index);

            curveShape.push([posX,posY].join(","));
        }


    
        data.components.curve.attr('d', svg.builder.path(curveShape,false))

        let areaShape = [].concat(curveShape);
        areaShape.push(svg.builder.point(0,this.minValue));
        areaShape.push(svg.builder.point(this.localSize.svgContainerSize.width,0));

        data.components.area.attr('d', svg.builder.path(areaShape,true))


    }

    /*****************************************************************************
    * TOOLS
    *****************************************************************************/
    private coumputeDiffItemValue(currentValue, oldValue,diffOffset, previousPointData,index, loopCursor){
        
        let localCurrentValue      = this.computeLimiteValue(currentValue);
        let localPreviousPointData = this.computeLimiteValue(previousPointData);

        let min            = isNull(this.minValue)?this.innerMinValue:this.minValue;
        let max            = isNull(this.maxValue)?this.innerMaxValue:this.maxValue;

        let diff           = (localCurrentValue-localPreviousPointData);
        let localeOldValue = isNull(oldValue)?min:oldValue;
        let diffMax        = max-localPreviousPointData;
        let diffOld        = localeOldValue-localPreviousPointData;
        let result         = diffOld;

        if(index == loopCursor){
            diff = diff * diffOffset;
        }

        if(index == this.lastIndex){
            result = max - diff;
        }else if(index<loopCursor){
            result = diffOld > diffMax? diffMax : diffOld;
        }
        else{
            result = -diff;   
        }

        return result * this.localSize.ratioItemHeight;
    }

    private computeLimiteValue(value){
        let result = value;
        if(value!=0 && isNotNull(this.minValue) && value<this.minValue){
            result = this.minValue;
        }else if(value!=0 &&  isNotNull(this.maxValue) && value>this.maxValue){
            result = this.maxValue;
        }
        return result;
    }

    private getCurrentData(){
        let result = null;

        let keys = Object.keys(this.data);
        if(keys.length==1){
            result = this.data[keys[0]];
        }else{
            for(let i=keys.length-1;i>=0; i--){
                if(isNotNull(keys[i]) && "current" == keys[i].toLowerCase()){
                    result = this.data[keys[i]];
                }
            }
        }
        return result;
    }

    private notNullValue(){
        return this.valueStr != this.nullValue;
    }
}
  
