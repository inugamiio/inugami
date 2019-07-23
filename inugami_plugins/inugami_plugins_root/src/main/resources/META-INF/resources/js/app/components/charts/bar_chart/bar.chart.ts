import {Component,
        Input,
        forwardRef,
        ViewChild,
        ViewEncapsulation,
        ElementRef}                                         from '@angular/core';

import {NG_VALUE_ACCESSOR,ControlValueAccessor}             from '@angular/forms';
import * as d3 from 'd3';


export const BAR_CHART_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() =>BarChart),
    multi: true
};


@Component({
  selector      : 'bar-chart',
  template      : `
      <div [ngClass]="'bar-chart'"
           [class]="styleClass" >

          <div class="bar-chart-container" #container></div>
          <div class="bar-chart-value-ref" *ngIf="valueRef">{{valueRef}}</div>
      </div>
  `,
  directives    : [],
  providers     : [BAR_CHART_ACCESSOR],
  encapsulation : ViewEncapsulation.None
})
export class BarChart implements ControlValueAccessor,AfterViewInit {

    /***************************************************************************
    * ATTRIBUTES
    ***************************************************************************/
    @ViewChild('container') chartContainer : ElementRef;
    @Input() height                        : number  = null;
    @Input() width                         : number  = null;
    
    @Input() heightRatio                   : number  = null;
    @Input() widthRatio                    : number  = null;

    @Input() pluginName                    : string;
    @Input() event                         : string;

    @Input() label                         : string;
    @Input() resolution                    : number   = 5;
    @Input() stack                         : boolean  = true;
    @Input() duration                      : number  = 5000;
    @Input() valueRef                      : string;
    

    // inner attributes :::::::::::::::::::::::::::::::::::::::
    private durationItem                   : number = 0;
    private ratios                         : any;
    private size                           : any;
    private compos                         : any;
    private position                       : any;

    private heightAuto                     : boolean;
    private widthAuto                      : boolean;

    private maxValue                       : number  = 0;
    private firstInitializingValue         : boolean = true;
    

    /***************************************************************************
    * iNITIALIZE
    ***************************************************************************/
    constructor(private el: ElementRef){
        this.ratios={
            sizeAuto    : {
                height : null,
                width  : null
            },
            margin : {
                right       : 0.14,  left            : 0.14,
                top         : 0.02,  bottom          : 0,
                betweenBloc : 0.06,  label           : 0.1,
            },
            font     : 0.50 ,
            label    : 0.17 ,
            mask     : 0.05,
            maskPosY : 0.80,
            roundBackground : 0.1,
            blocRound : 0.5,
            value : 0
        };
        
        this.size={
            right           : 0, left          : 0,
            top             : 0, bottom        : 0,
            width           : 0, height        : 0,
            betweenBloc     : 0,
            blocWidth       : 0, 
            roundBar        : 0,
            font            : 0,
            label           : 0,
            marginLabel     : 0,
            roundBackground : 0
        };
        
        
        this.compos = {
             svg       : null,
             layout    : null,
             animeNode : [],
             tip       : {
                mainContainer : null,
                group         : null,
                value         : null
             },
             label  : {
                 group      : null,
                 background : null,
                 align      : null,
                 title      : null
             },
             data : {
                group  : null,
                values : {},
                masks  : []
             }
        };
        
        this.position = {
            layout: { x : 0, y:0 },
            label : { x : 0, y:0 },
            data  : { x : 0, y:0 }
        };
        
    }
    

    ngAfterContentInit() {
        org.inugami.asserts.isTrue(this.resolution>0,"can't render current compoent with this resolution :"+this.resolution);
        this.heightAuto = isNotNull(this.heightRatio);
        this.widthAuto  = isNotNull(this.widthRatio);

        this.durationItem=(this.duration/this.resolution).toFixed(0);

        this.updateAutoSize();
        this.computeRatios();
        this.renderLayout();
        

        // binding events ::::::::::::::::::::::::::::::::::
        let self= this;
        if(isNotNull(this.event)){
            org.inugami.events.addEventListenerByPlugin(this.pluginName,this.event, function(event) {
                if (isNotNull(event.detail.data) && isNotNull(event.detail.data.values)) {
                  self.writeValue(event.detail.data.values);
                }
            });
          }
  
        org.inugami.events.addEventListener(org.inugami.events.type.RESIZE, function(data){
            self.updateSize(data.detail);
            self.processRefresh();
        });

        org.inugami.events.addEventListener(org.inugami.events.type.UPDATE_CONFIGURATION, function(data){
            self.updateSize(org.inugami.values.screen);
            self.processRefresh();
        });
  
        this.updateSize(org.inugami.values.screen);
        self.processRefresh();
    }

    updateAutoSize(){
        if(this.heightAuto || this.widthAuto){
            this.computeDimension();
        }
    }

    /***************************************************************************
    * COMPUTE RATIO
    ***************************************************************************/
    private computeDimension(){
        
      if(isNull(this.heightRatio)){
          this.heightRatio = 1;
      }  
      if(isNull(this.widthRatio)){
          this.widthRatio = 1;
      }
      let parents = svg.components.searchParent(this.el.nativeElement.parentElement, "BLOC");
      let ratio   = svg.math.computeDimension(parents,this.heightRatio,this.widthRatio);

      this.ratios.sizeAuto.height = ratio.height;
      this.ratios.sizeAuto.width  = ratio.width;
    }

    private computeRatios(){
        this.size.label           =  0;
        if(isNotNull(this.label)){
           this.size.label            = this.height     * this.ratios.label;
           this.size.roundBackground  = this.size.label * this.ratios.roundBackground;
           this.size.font             = this.size.label * this.ratios.font;
           this.size.marginLabel = this.size.label * this.ratios.margin.label;
        }
       

        this.size.right           = this.width * this.ratios.margin.right;
        this.size.left            = this.width * this.ratios.margin.left;

        this.size.top             = this.height * this.ratios.margin.top;
        this.size.bottom          = this.height * this.ratios.margin.bottom;

        this.size.width           = this.width  - (this.size.left+this.size.right);

        this.size.height          = this.height - (this.size.top+this.size.bottom+this.size.label);
        

        let nbInnerBlocMargin     = this.resolution-1;
        this.size.betweenBloc     = this.size.width * this.ratios.margin.betweenBloc;
        this.size.blocWidth       = (this.size.width- (this.size.betweenBloc*nbInnerBlocMargin))/this.resolution;
        this.size.roundBar        = this.size.blocWidth * this.ratios.blocRound;

        this.ratios.value         = this.maxValue==0 ? 0 : this.size.height / this.maxValue;
        

        this.position.layout      = {x:0,                y:this.height};
        this.position.label       = {x:0,                y:0};
        this.position.data        = {x:this.size.left ,  y:(this.height-this.size.label-this.size.bottom)};
        
    }

    /***************************************************************************
    * REFRESH
    ***************************************************************************/
    private processRefresh(){
        this.updateAutoSize();
        this.computeRatios();
        this.refreshLayout();
        this.refreshLabel();
        this.refreshValues();
    }

    private refreshLayout(){
      this.compos.svg.attr('height', this.height);
      this.compos.svg.attr('width',  this.width);

      svg.components.translate(this.compos.data.group,this.position.data);
    }



    private refreshLabel(){
        if(isNotNull(this.compos.label.background)){
            this.compos.label.background.attr('ry',    this.size.roundBackground);
            this.compos.label.background.attr('height',this.size.label);
            this.compos.label.background.attr('width', this.width);

            svg.components.translate(this.compos.label.group,{x:0,y:this.height-this.size.label});


            this.compos.label.title.attr('style', 'font-size:'+this.size.font+'px');
            let currentTitleSize = svg.components.size(this.compos.label.title);
            let pos = {
                x : (this.width/2) - (currentTitleSize.width/2),
                y : currentTitleSize.height
            }

            svg.components.translate(this.compos.label.title,pos);
        }
    }



    private refreshValues(){
        let mask = this.buildMask();

        for(let index = this.resolution-1; index>=0; index--){
            this.refreshTagetsValues(index,mask);
        }
    }

    private refreshTagetsValues(index, mask){
        let self  = this;
        let delay =  (this.resolution-index)*this.durationItem;

        svg.animate.tween(this.compos.animeNode[index],
                          delay,
                          this.durationItem,
                          (time)=>{
                             self.processRefreshTagetsValues(time,index,mask);
                          }
                        );


        
    }
    private processRefreshTagetsValues(time,index,mask){
        let offset  = 0;
        let keysSet = Object.keys(this.compos.data.values);
        let nbTagets =keysSet.length;

        for(let iTarget=nbTagets-1; iTarget>=0; iTarget--){
            let target = this.compos.data.values[keysSet[iTarget]];

            let previousValueRaw = target.values[index].previous;
            let previousValue = (isNull(previousValueRaw)?0:previousValueRaw)

            let diff = target.values[index].value - previousValue;
            let currentValue = previousValue + (diff*time);

            this.refreshTagetValue(target,index, offset,time,currentValue);
            if(this.stack){
                offset = offset+currentValue;
            }
        }
        if(isNotNull(this.compos.data.masks) && this.compos.data.masks.length>0){
            this.refeshMask(index,mask,offset);
        }        
    }


    private refreshTagetValue(target, index, offset,time,currentValue){
        let data = target.values[index];
        let dateTime = "";
        let compo =data.bar;

        if(isNotNull(data.timestamp)){
            dateTime  = new Date(data.timestamp).toISOString();
        }

        let height       =  currentValue * this.ratios.value;//(data.value*time) * this.ratios.value;
        let offsetHeight = offset     * this.ratios.value;
        
        compo.attr("width", this.size.blocWidth);
        compo.attr("height",height);
        compo.attr("data-value",data.value);
        compo.attr("data-time",dateTime);
        
        let posX = (index*this.size.blocWidth)+(index*this.size.betweenBloc);
        let posY = -height -offsetHeight;

        svg.components.translate(compo,{x:posX,y:posY});

    }

    private refeshMask(index,shape,offset){
        let halfH = this.size.height*this.ratios.mask;

        let currentMask = this.compos.data.masks[index];
        let posX = (index*this.size.blocWidth)+(index*this.size.betweenBloc);
        let posY = -(offset * this.ratios.value)+(halfH*this.ratios.maskPosY);

        currentMask.attr('d',shape);
        svg.components.translate(currentMask,{x:posX,y:posY});
    }
    /***************************************************************************
    * UPDATE
    ***************************************************************************/
    private updateSize(data){
      if(this.heightAuto){
        this.height = data.height * this.ratios.sizeAuto.height;
      } 
      
      if(this.widthAuto){
        this.width  = data.width  * this.ratios.sizeAuto.width;
      }
    }
    private updateValues(data){

        this.extractData(data);
        if(this.firstInitializingValue){
            this.firstInitializingValue =false;
        }
        this.searchMaxValue();
        this.computeRatios();
        this.refreshValues();
    }

    private extractData(data){
        let sortData = org.inugami.data.extractors.graphite.sortTargets(data);
        for(let target of sortData){
            this.extractTargetData(target);
        }
    }

    private extractTargetData(target){
        if(this.firstInitializingValue){
            this.initTargetData(target);
            this.renderMasks();
        }else{
            this.updateTargetData(target);
        }
    }


    private initTargetData(data){
        let key = data.target;
        let specificStyleClass = org.inugami.data.extractors.style.decomposeStyleClass(key, '-');
                               
        let group              = this.compos.data.points.append("g").attr("class","datapoint "+specificStyleClass);

        let values             = [];
        let realDatapoints     = org.inugami.data.extractors.graphite.cleanDatapoints(data.datapoints);

        let resolutionDelta    = realDatapoints.length - this.resolution;
        let cursor             = this.resolution;
        let cursorBar          = 0;
        
    
        if(resolutionDelta<0){
            for(let i=-resolutionDelta; i>0; i--){
                cursorBar++;
                values.push(this.initTargetPointData(0,group,(cursorBar)));
            }
        }

        for(let i=realDatapoints.length-1; i>=0; i--){
            cursor--;
            cursorBar++;
            if(cursor<0){
                break;
            }
            values.push(this.initTargetPointData(realDatapoints[i].value,group, cursorBar));
        }

        let timestamp = null;
        if(isNotNull(realDatapoints) && isNotNull(realDatapoints[realDatapoints.length-1])){
            timestamp = realDatapoints[realDatapoints.length-1].timestamp;
        }

        this.compos.data.values[key]={
            'group'     : group,
            'timestamp' : timestamp,
            'values'    : values
        }
    }

    private initTargetPointData(value, group, index){
        let styleClass= ["bar"];
            styleClass.push(index%2==0?"event":"odd");
            styleClass.push("bar-item-"+index);

        if(index == 1){
            styleClass.push("first");
        }
        if(index == this.resolution){
            styleClass.push("last");
        }
        let self= this;
        
        let bar = group.append("rect").attr("class",styleClass.join(" "));
            bar.on('mouseover', function(){self.showTip(bar)})
            bar.on('mouseout' ,  function(){self.hideTip(bar)})

        return {
            'value' : value,
            'bar'   : bar
        }
    }


    private updateTargetData(data){
        let key       = data.target;
        if(isNull(this.compos.data.values[key])){
            this.initTargetData(data);
        }

        let element   = this.compos.data.values[key];

        if(isNotNull(element)){
            
            let timestamp = null;
            if(isNotNull(data.datapoints)){
                var datapointsSize = data.datapoints.length;
                let size = element.values.length;

                for(let index = 1; index <= this.resolution; index++){
                    if (datapointsSize == 0) {
                        let currentElementValue              = element.values[size-index].value;
                        element.values[size-index].previous  = isNull(currentElementValue)?0:currentElementValue;
                        element.values[size-index].value     = 0;
                        element.values[size-index].timestamp = 0;
                        
                    } else if (isNotNull(data.datapoints[datapointsSize-index])) {
                        let pointValue                       = data.datapoints[datapointsSize-index].value;
                        let currentElementValue              = element.values[size-index].value;
                        element.values[size-index].previous  = isNull(currentElementValue)?0:currentElementValue ;
                        element.values[size-index].value     = isNull(pointValue)?0: pointValue;
                        element.values[size-index].timestamp = data.datapoints[datapointsSize-index].timestamp;
                    }
                }
            }
        }
        
    }



    private searchMaxValue(){
        let localMaxValue = null;
        let keysSet = Object.keys(this.compos.data.values);
        
        for(let posI=this.resolution-1; posI>=0;posI--){
            let totalStack = 0;

            for(let targetName of keysSet){
                let target = this.compos.data.values[targetName];
                totalStack += target.values[posI].value;
            }

            if(isNull(localMaxValue) || totalStack >localMaxValue){
                localMaxValue = totalStack;
            }
        }
        this.maxValue =localMaxValue;
    }

    /***************************************************************************
    * RENDERING
    ***************************************************************************/
    private renderLayout(){
        let container                  = this.chartContainer.nativeElement;
        this.compos.svg                = svg.builder.svgContainer(container,this.height,this.width);
        

        this.compos.layout             = this.compos.svg.append("g").attr("class", "layout");
        this.compos.data.group         = this.compos.layout.append("g").attr("class", "data-grp");
        this.compos.data.points        = this.compos.data.group.append("g").attr("class", "data");
        this.compos.data.masksGrp      = this.compos.data.group.append("g").attr("class", "masks");
        this.compos.label.group        = this.compos.layout.append("g").attr("class", "label");

        for(let i=this.resolution-1; i>=0; i--){
            this.compos.animeNode.push(this.compos.data.group.append("g"));
        }

        this.compos.tip                = svg.components.render.tip(container,{x:10,y:0});
        this.refreshLayout();
    
        if(isNotNull(this.label)){
            this.renderLabel();
        }
    }

    

    private renderMasks(){
        if(isNull(this.compos.data.masks) || this.compos.data.masks.length==0){
            for(let i=this.resolution-1; i>=0; i--){
                this.renderMask(i);
            }
        }
        
    }

    private renderMask(index){
        let mask = this.compos.data.masksGrp.append('path').attr("class", "mask");
        this.compos.data.masks.push(mask)
    }


    
    private buildMask(){
        let points = [];
        let halfH = this.size.height*this.ratios.mask;
        let angle = -90;
        
        points.push(svg.builder.point(0,0));
        points.push(svg.builder.point(0,-this.size.height));
        points.push(svg.builder.point(this.size.blocWidth,0));
        points.push(svg.builder.point(0,this.size.height));

        points.push('C');

        points.push(svg.builder.point(this.size.blocWidth,-halfH));
        points.push(svg.builder.point(0,-halfH));
        points.push(svg.builder.point(0,0));

        let result = svg.builder.path(points,true);
        
        return result;
    }

    private renderLabel(){
       this.compos.label.background = this.compos.label.group.append("rect").attr("class", "background");
       this.compos.label.title      = this.compos.label.group.append("svg:text").attr("class", "title");
       this.compos.label.title.text(this.label);


       this.refreshLabel();
    }


    /***************************************************************************
    * ACTIONS
    ***************************************************************************/
    private showTip(node){
        let value = svg.components.attribute(node,'data-value');
        let time  = svg.components.attribute(node,'data-time');

        let render = [];

        render.push('<div class="current-value">');
        render.push(value);
        render.push('</div>');

        if(isNotNull(time) && ""!=time){
            render.push('<div class="current-time">');
            render.push(time);
            render.push('</div>');
        }
        
    
        let html = render.join("");
        this.compos.tip.value.html(html);
        this.compos.tip.group.attr("class","show");
        this.compos.tip.group.attr("style",this.compos.tip.position());
    }

    private hideTip(node){
        this.compos.tip.group.attr("class","hidden");
    }
    
    private refreshTip(node){
        let value = "<strong>Frequency:</strong> <span style='color:red'>" + node + "</span>";
    }

    /***************************************************************************
    * IMPLEMENTS ControlValueAccessor
    ***************************************************************************/
    writeValue(value: any) {
      if(isNotNull(value)){
         let localValue =  null;
         if(!Array.isArray(value)){
            localValue = [value];
         }else{
            localValue =  value;
         }

         this.updateValues(localValue);
         this.processRefresh();
      }
    }
    registerOnChange(fn: any)  {this.onChangeCallback = fn;}
    registerOnTouched(fn: any) {this.onTouchedCallback = fn;}
}