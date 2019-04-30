import {Component, ElementRef, Input}                 from '@angular/core';
import {ControlValueAccessor}                         from '@angular/forms';

import {SvgComponent}                                 from 'js/app/components/charts/svg_component/svg.component.ts';

@Component({
    templateUrl: 'js/app/components/forms/dynamic-levels.html',
    selector: 'dynamic-levels',
  })
  export class DynamicLevels extends SvgComponent implements ControlValueAccessor {

    @Input() width                              : number;
    @Input() height                             : number;

    @Input() formatter                          : any;
    @Input() disabled                           : boolean;
    @Input() readonly                           : boolean;

    private onTouched                           : any;
    private onChange                            : any;

    private heightPx                            : any;
    private widthPx                             : any;

    private groups                              : any;
    private position                            : any;
    private elementSize                         : any;
    private elementRatio                        : any;

    private data                                : any;
    private maxValue                            : any;
    private minValue                            : any;
    private staticMode                          : boolean;

    private selectedDataPoint                   : any;
    private selectedDataPointsLine              : any;


    private dblClick                            : boolean;
    /**************************************************************************
     * CONSTRUCTORS
     **************************************************************************/
    constructor(private el: ElementRef) {
        super(el);

        this.groups = {
            svgComponentChild       : {},
            axisXgroup              : {},
            axisYgroup              : {},
            axisX                   : {},
            axisY                   : {},
            axisYPoints             : [],
            axisXPoints             : [],
            axisYPointsGroup        : {},
            axisXPointsGroup        : {},
            dataPointsGroup         : {},
            dataPoints              : [],
            tooltip                 : [],
            tooltipText             : {},
            tooltipBackground       : {},
        }
    }

    private afterContentInit() {

        this.position = {
            axisX                   : {},
            axisY                   : {},
            axisXPoints             : [],
            axisYPoints             : [],
        }

        this.elementRatio = {
            axisX                   : 0.8,
            axisXLeftMargin         : 0.1,
            axisXRightMargin        : 0.1,


            axisY                   : 0.8,
            axisYUpMargin           : 0.1,
            axisYDownMargin         : 0.1
        }
        
        this._initElementRatio();

        this.elementSize = { 
            axisX                   : 0,
            axisY                   : 0,
            axisLeftMargin          : 0,
            axisUpMargin            : 0,
            axisRightMargin         : 0,
            axisDownMargin          : 0,
            axisXPointsMargin       : 0,
            axisYPointsMargin       : 0,
            tooltipLeftMargin       : 0,
            tooltipUpMargin         : 0,
            tooltipHourUpMargin     : 0,
            tooltipHourLeftMargin   : 0,
            tooltipValueUpMargin    : 0,
            tooltipValueLeftMargin  : 0,
        }

        this.data = [];
        

        // FAIRE GAFFE A CA , SI ON LNENVOIT VIA DU BINDING FAUT QUE LE BIDNIG PRENNE LE DESSUS
        this.staticMode             = true;
        this.minValue               = 0;
        this.maxValue               = 100;
        this.disabled = false;
        this.readonly = false;
    }

    /***************************************************************************
     * RENDER
     ***************************************************************************/
    private renderLayout(svgComponentChild: any){
        this.groups.svgComponentChild = svgComponentChild;        
        this._computeSizeTable();
        this._initPositionTable();

        this._renderXAxis(svgComponentChild);
        this._renderYAxis(svgComponentChild);

        this._initEvent();
    }

    private _renderXAxis(svgComponentChild){
        let axisXGroup =  svgComponentChild.append("g").attr("class","axisXGroup");
        let axisX = axisXGroup.append("line").attr("x1", this.position.axisX.x1)
                                            .attr("y1", this.position.axisX.y1)
                                            .attr("x2", this.position.axisX.x2)
                                            .attr("y2", this.position.axisX.y2)
                                            .attr("stroke-width", 1)
                                            .attr("stroke", "black");
        let axisXPoints = axisXGroup.append("g").attr("class","axisXPoints");
        for (let i = 0;   i < this.position.axisXPoints.length; i++) {
            let axisXPoint = axisXPoints.append("circle").attr("cx", this.position.axisXPoints[i].x)
            .attr("cy", this.position.axisXPoints[i].y)
            .attr("r", 2);
            this.groups.axisXPoints.push(axisXPoint);
        }

        this.groups.axisXGroup          = axisXGroup;
        this.groups.axisX               = axisX;
        this.groups.axisXPointsGroup    = axisXPoints 
    }

    private _renderYAxis(svgComponentChild){
        let axisYGroup =  svgComponentChild.append("g").attr("class","axisYGroup");
        let axisY = axisYGroup.append("line").attr("x1", this.position.axisY.x1)
                                            .attr("y1", this.position.axisY.y1)
                                            .attr("x2", this.position.axisY.x2)
                                            .attr("y2", this.position.axisY.y2)
                                            .attr("stroke-width", 1)
                                            .attr("stroke", "black");
        let axisYPoints = axisYGroup.append("g").attr("class","axisYPoints");
        for (let i = 0;   i < this.position.axisYPoints.length; i++) {
           let axisYPoint = axisYPoints.append("circle").attr("cx", this.position.axisYPoints[i].x)
            .attr("cy", this.position.axisYPoints[i].y)
            .attr("r", 2);
            this.groups.axisYPoints.push(axisYPoint);
        }
        this.groups.axisYGroup          = axisYGroup;
        this.groups.axisY               = axisY;
        this.groups.axisYPointsGroup    = axisYPoints 
    }


    /***************************************************************************
     * INITIALIZATION
     ***************************************************************************/

    private _initElementRatio() {

    }

    private _initPositionTable(){
        this.position.axisX.x1 = this.elementSize.axisLeftMargin;
        this.position.axisX.x2 = this.elementSize.axisX + this.position.axisX.x1;
        
        this.position.axisY.y1 = this.elementSize.axisUpMargin;
        this.position.axisY.y2 = this.elementSize.axisY + this.position.axisY.y1;

        this.position.axisX.y1 = this.position.axisY.y2;
        this.position.axisX.y2 = this.position.axisX.y1;

        this.position.axisY.x1 = this.position.axisX.x1;
        this.position.axisY.x2 = this.position.axisX.x1;

        this._initAxisXPointsPosition();
        this._initAxisYPointsPosition();
    }

    private _initAxisXPointsPosition(){
        this.position.axisXPoints = [];
        for(let i = 1; i <= 24; i++){
            this.position.axisXPoints.push({
                x: this.elementSize.axisLeftMargin + i * this.elementSize.axisXPointsMargin,
                y: this.position.axisX.y1,
              });
        }
    }
    private _initAxisYPointsPosition(){
        this.position.axisYPoints = [];
        for(let i = 9 ; i >= 0; i-- ){
            this.position.axisYPoints.push({
                x: this.position.axisY.x1,
                y: this.elementSize.axisUpMargin + i * this.elementSize.axisYPointsMargin,
              });
        }
    }

    private _computeSizeTable() {
        this.elementSize.axisX                  = this.elementRatio.axisX * this.widthPx;
        this.elementSize.axisY                  = this.elementRatio.axisY * this.heightPx;
        this.elementSize.axisXPointsMargin      = this.elementSize.axisX / 24;
        this.elementSize.axisYPointsMargin      = this.elementSize.axisY / 10;
        this.elementSize.axisUpMargin           = this.elementRatio.axisYUpMargin * this.heightPx;
        this.elementSize.axisDownMargin         = this.elementRatio.axisYDownMargin * this.heightPx;
        this.elementSize.axisLeftMargin         = this.elementRatio.axisXLeftMargin * this.widthPx;
        this.elementSize.axisRightMargin        = this.elementRatio.axisXRightMargin * this.widthPx;
        this.elementSize.tooltipLeftMargin      = - this.elementSize.axisXPointsMargin / 4;
        this.elementSize.tooltipUpMargin        = this.elementSize.axisYPointsMargin;
        this.elementSize.tooltipHourLeftMargin  = 0;
        this.elementSize.tooltipHourUpMargin    = this.elementSize.axisYPointsMargin / 4;
        this.elementSize.tooltipValueLeftMargin = this.elementSize.axisXPointsMargin;
        this.elementSize.tooltipValueUpMargin   = this.elementSize.axisYPointsMargin / 4;
       
    }

    private _initEvent(){
        this.compos.svg.node().addEventListener('mousemove',event => {if(!this.readonly && !this.disabled){this._moveDataPoints(event.clientY);}});
        this.compos.svg.node().addEventListener('mouseleave',event => {if(!this.readonly && !this.disabled){this._unselectDataPoint()}});
        document.body.onmouseup = event => {this._unselectDataPoint()};
    }

    /***************************************************************************
     * NEW DATA LINE 
     ***************************************************************************/
    public addNewData(lineLevel : string){
        if(!this.disabled && !this.readonly){
            let line = this.data.find(function(element){
                return element.name == lineLevel;
            });
            
            if(this._emptyOrNull(line)){
                let dataObject =  {"name" : lineLevel,
                                   "data" : []};
                dataObject["data"] = [];
                this.data.push(dataObject);
                
    
                //a checker, quest ce que ca fait
                let targetPoint = 0;
                for(let i = 0; i < this.groups.axisYPoints.length; i++){
                    if( targetPoint !== 0 ){
                        if(this._canAdd(i)){
                            targetPoint = i;
                        }
                    }
                }
                let dataPointGroup = this.groups.svgComponentChild.append("g")
                                                                    .attr("class",lineLevel);
                this.groups.dataPoints[lineLevel] = [];
                let initialYposition = this.computeDataPointsPosition(this.position.axisY.y1,this.position.axisY.y2 - this.elementSize.axisYPointsMargin
                                                                        ,0,this.minValue,this.maxValue);
                if(this.groups.tooltipText === undefined){
                    this.groups.tooltipText = {};
                }
                if(this.groups.tooltipBackground === undefined){
                    this.groups.tooltipBackground = {};
                }
                if(this.groups.tooltip === undefined){
                    this.groups.tooltip = {};
                }
                if(this.groups.dataPointGroup === undefined){
                    this.groups.dataPointsGroup = {};
                }
                this.groups.tooltipText[lineLevel] = [];
                this.groups.tooltipBackground[lineLevel] = [];
                this.groups.tooltip[lineLevel] = [];
                for(let i = 0; i < this.groups.axisXPoints.length; i++){
                    let dataPoint = dataPointGroup.append("circle")
                                                    .attr("r",4)
                                                    .attr("cy",initialYposition)
                                                    .attr("cx",this.position.axisXPoints[i].x)
                                                    //.attr("stroke","black")
                                                    .attr("level",lineLevel)
                                                    .attr("hour",i)
                                                    .attr("class","hour-"+i+" "+lineLevel);
                    this.groups.dataPoints[lineLevel].push(dataPoint);
                    dataPoint.lower();
                    this.groups.dataPointsGroup[lineLevel] = dataPointGroup;
    
                    let level = this.computeDataPointsValue(this.position.axisY.y1,this.position.axisY.y2 - this.elementSize.axisYPointsMargin,
                                                            initialYposition,this.minValue,this.maxValue)
    
                    this._newTooltips(dataPointGroup,lineLevel,level,initialYposition,i);
    
                    dataObject.data.push({"hour" : i,
                                          "level": level});
                }
                for(let dataPoint of this.groups.dataPoints[lineLevel]){
                    this._addMouse(dataPoint,this.groups.dataPoints[lineLevel]);
                } 
            }
        }
        
    }

    private _newTooltips(dataPointGroup,lineLevel,level,initialYposition,i){        
        let tooltip = dataPointGroup.append("g").attr("class","tooltips");
        let tooltipBackground = tooltip.append("rect");
        let tooltipLevelText  = tooltip.append("text").text(lineLevel)
                                                        .attr("x",this.position.axisXPoints[i].x + this.elementSize.tooltipLeftMargin)
                                                        .attr("y",initialYposition + this.elementSize.tooltipUpMargin);
        let tooltipHourText  = tooltip.append("text").text(this._convertToHour(i))
                                                        .attr("x",this.position.axisXPoints[i].x + this.elementSize.tooltipLeftMargin + this.elementSize.tooltipHourLeftMargin)
                                                        .attr("y",initialYposition + this.elementSize.tooltipUpMargin + this.elementSize.tooltipHourUpMargin + tooltipLevelText.node().getBoundingClientRect().height);
        let tooltipValueText  = tooltip.append("text").text(level)
                                                        .attr("x",this.position.axisXPoints[i].x + this.elementSize.tooltipLeftMargin + this.elementSize.tooltipValueLeftMargin + tooltipLevelText.node().getBoundingClientRect().width)
                                                        .attr("y",initialYposition + this.elementSize.tooltipUpMargin + this.elementSize.tooltipValueUpMargin);
       
       let tooltipTextGroup = { "level" :tooltipLevelText,
                                "hour"  :tooltipHourText,
                                "value" :tooltipValueText,};
        
        
        this.groups.tooltipText[lineLevel].push(tooltipTextGroup);
       

       tooltipBackground.attr("x",this.position.axisXPoints[i].x + this.elementSize.tooltipLeftMargin)
                                .attr("y",initialYposition + this.elementSize.tooltipUpMargin - tooltipLevelText.node().getBoundingClientRect().height)
                                .attr("width",tooltipLevelText.node().getBoundingClientRect().width + this.elementSize.tooltipValueLeftMargin + tooltipValueText.node().getBoundingClientRect().width)
                                .attr("height",tooltipLevelText.node().getBoundingClientRect().width + this.elementSize.tooltipHourUpMargin + tooltipHourText.node().getBoundingClientRect().height);

        this.groups.tooltipBackground[lineLevel].push(tooltipBackground);
        this.groups.tooltip[lineLevel].push(tooltip);
    }

    // pour checker si la ligne est vide et donc si on peut ajouter ici la nouvelle 
    private _canAdd(indexY){
        return true;
    }

    private _addMouse(dataPoint,dataPointsLine){
        let self = this;
        dataPoint.on('mousedown',function(){
            self._mouseDown(dataPoint,dataPointsLine)});
        dataPoint.node().addEventListener("click", event => {if(!this.readonly && !this.disabled){this._alignAfter(event,dataPoint)}});
        dataPoint.node().addEventListener("dblclick", event => {if(!this.readonly && !this.disabled){this._alignBefore(event,dataPoint)}});
        dataPoint.node().addEventListener("mouseenter",event => {if(!this.disabled){this._showTooltips(dataPoint)}});
        dataPoint.node().addEventListener("mouseleave",event => {if(!this.disabled){this._hideTooltips(dataPoint)}});
    }
    
    private _mouseDown(dataPoint,dataPointsLine){
        if(!this.readonly && !this.disabled){
            this._setSelectedDataPoint(dataPoint);
            this._setSelectedDataPointsLine(dataPointsLine);
        }
    }

    private _alignAfter(event,dataPoint){
        if(event.shiftKey){
            
            if(!this.dblClick){
                this.dblClick = true;
                let self = this;
                setTimeout(function(){
                    if(self.dblClick){
                        self._moveAllPointsAfter(dataPoint);
                        self._readjustPosition();
                        self.dblClick = false;
                    }
                },300);
            }else{
                this.dblClick = false;
            }
            
        }
    }

    private _alignBefore(event,dataPoint){
        if(event.shiftKey){
            this._moveAllPointsBefore(dataPoint);
            this._readjustPosition();
        }
    }

    /***************************************************************************
     * DELETE LINE 
     ***************************************************************************/

     public deleteLine(lineLevel){
        let index = this.data.findIndex(function(element){
            return element.name == lineLevel;
        })
        if(index > -1){
            this.data.splice(index,1);
        }
        this.groups.dataPoints[lineLevel]           = [];
        this.groups.tooltip[lineLevel]              = [];
        this.groups.tooltipText[lineLevel]          = [];
        this.groups.tooltipBackground[lineLevel]    = [];

        this.groups.dataPointsGroup[lineLevel].remove();
     }

    /***************************************************************************
     * MOVE DATA POINTS
    ***************************************************************************/

    private _setSelectedDataPoint(dataPoint : any){
        this.selectedDataPoint = dataPoint;
        dataPoint.attr("class",dataPoint.attr("class")+" selected")
        this._showTooltips(dataPoint);
    }
    private _setSelectedDataPointsLine(dataPointsLine : any){
        this.selectedDataPointsLine = dataPointsLine;
        
    }

    private _unselectDataPoint(){
        if(this.selectedDataPoint != null){
            let selectedPoint = this.selectedDataPoint;
            let selectedClass = this.selectedDataPoint.attr("class").replace(" selected","");
            this.selectedDataPoint.attr("class",selectedClass);
            this.selectedDataPoint = null;
            this.selectedDataPointsLine = null;
            this._hideTooltips(selectedPoint);
        }
    }
     
    private _moveDataPoints( yPos : any){
        org.inugami.asserts.isTrue(this.minValue < this.maxValue, "min value should be lower than max value");
        if(isNotNull(this.selectedDataPoint)){
            
            if(this.staticMode){
                this._moveDataPointsLine(yPos);
            }else{
                this._moveSingleDataPoint(yPos);
            }
           
            this._readjustPosition();
            this._refreshTooltipsValue();
            this._refreshTooltipsPosition();
        }
        if(isNotNull(this.onChange)){
            this.onChange(this.data);
        }
    }

    private _moveDataPointsLine(yPos){
         
        let pos = this.transformMouseCoordToSVG(yPos);
        for(let datapoint of this.selectedDataPointsLine){
            datapoint.attr("cy",pos);

            let lineLevel  = this.selectedDataPoint.attr("level");
            let lineData = this.data.find(function(element){
                return element.name == lineLevel;
            })
            org.inugami.asserts.notNull(lineData,"no line with level same as selected point found");

            let self = this;
            
            for(let pointData of lineData.data){
                pointData.level = this.computeDataPointsValue(this.position.axisY.y1,this.position.axisY.y2 - this.elementSize.axisYPointsMargin,
                    this.selectedDataPoint.attr("cy"),this.minValue,this.maxValue);
            }
        }
        if(isNotNull(this.onChange)){
            this.onChange(this.data);
        }
        
    }

    private _moveSingleDataPoint(yPos){
            let pos = this.transformMouseCoordToSVG(yPos);
            this.selectedDataPoint.attr("cy",pos);
            let lineLevel  = this.selectedDataPoint.attr("level");
            let lineData = this.data.find(function(element){
                return element.name == lineLevel;
            })
            org.inugami.asserts.notNull(lineData,"no line with level same as selected point found");

            let self = this;
            let pointData = lineData.data.find(function(element){
                return element.hour == self.selectedDataPoint.attr("hour");
            })
            org.inugami.asserts.notNull(pointData,"no datapoint with same hour as selected point found");

            
            pointData.level = this.computeDataPointsValue(this.position.axisY.y1,this.position.axisY.y2 - this.elementSize.axisYPointsMargin,
                this.selectedDataPoint.attr("cy"),this.minValue,this.maxValue);
           
    }

    private _moveAllPointsAfter(dataPoint){
        let lineLevel  = dataPoint.attr("level");
        let lineData = this.data.find(function(element){
            return element.name == lineLevel;
        })
        org.inugami.asserts.notNull(lineData,"no line with level same as selected point found");

        let self = this;
            
        for(let pointData of lineData.data){
            if(pointData.hour > dataPoint.attr("hour")){
                pointData.level = this.computeDataPointsValue(this.position.axisY.y1,this.position.axisY.y2 - this.elementSize.axisYPointsMargin,
                    dataPoint.attr("cy"),this.minValue,this.maxValue);
            }
        }
    }

    private _moveAllPointsBefore(dataPoint){
        let lineLevel  = dataPoint.attr("level");
        let lineData = this.data.find(function(element){
            return element.name == lineLevel;
        })
        org.inugami.asserts.notNull(lineData,"no line with level same as selected point found");

        let self = this;
            
        for(let pointData of lineData.data){
            if(pointData.hour < dataPoint.attr("hour")){
                pointData.level = this.computeDataPointsValue(this.position.axisY.y1,this.position.axisY.y2 - this.elementSize.axisYPointsMargin,
                    dataPoint.attr("cy"),this.minValue,this.maxValue);
            }
        }
    }

    private transformMouseCoordToSVG(y) {
        var CTM = this.compos.svg.node().getScreenCTM();
        return y = (y - CTM.f) / CTM.d;
    }
    private transformMouseCoordToscreen(y) {
        var CTM = this.compos.svg.node().getScreenCTM();
        return y = (y* CTM.d) - CTM.f;
    }

    private _readjustPosition(){
        for(let dataPointsLine of this.data){
            for(let datapoint of dataPointsLine.data){
                if(datapoint.level > this.maxValue){
                    datapoint.level = this.maxValue;
                }else if(datapoint.level < this.minValue){
                    datapoint.level = this.minValue;
                }
            }
        }
        this._refreshDataPointsPosition();
    }

    /***************************************************************************
     * REFRESH
     ***************************************************************************/
    public processRefresh(){
        this._computeSizeTable();
        this._initPositionTable();
        this._refreshValues();

    }

    private _refreshValues(){
        this._refreshAxisValues();
        this._refreshAxisXPointsValues();
        this._refreshAxisYPointsValues();
        this._refreshDataPointsPosition();
        this._refreshTooltipsValue();
        this._refreshTooltipsPosition()
    }

    private _refreshAxisValues(){
        this.groups.axisX.attr("x1", this.position.axisX.x1)
                        .attr("y1", this.position.axisX.y1)
                        .attr("x2", this.position.axisX.x2)
                        .attr("y2", this.position.axisX.y2);

        this.groups.axisY.attr("x1", this.position.axisY.x1)
                        .attr("y1", this.position.axisY.y1)
                        .attr("x2", this.position.axisY.x2)
                        .attr("y2", this.position.axisY.y2);                
    }

    private _refreshAxisXPointsValues(){
        for(let i = 0;i < this.groups.axisXPoints.length; i++){
            this.groups.axisXPoints[i].attr("cx",this.position.axisXPoints[i].x);
            this.groups.axisXPoints[i].attr("cy",this.position.axisXPoints[i].y);
        }
    }
    private _refreshAxisYPointsValues(){
        for(let i = 0;i < this.groups.axisYPoints.length; i++){
            this.groups.axisYPoints[i].attr("cx",this.position.axisYPoints[i].x);
            this.groups.axisYPoints[i].attr("cy",this.position.axisYPoints[i].y);
        }
    }

    private _refreshDataPointsPosition(){
        for(let i = 0; i < this.data.length; i++ ){
            for(let j = 0; j < this.data[i].data.length; j++ ){
                let yPos = this.computeDataPointsPosition(this.position.axisY.y1,this.position.axisY.y2 - this.elementSize.axisYPointsMargin,this.data[i].data[j].level,this.minValue,this.maxValue);
                this.groups.dataPoints[this.data[i].name][j].attr("cy",yPos)
                                                            .attr("cx",this.position.axisXPoints[j].x);
            }
        }
    }
    private  _refreshTooltipsValue(){
        for(let dataLine of this.data){
            for(let dataPoint of dataLine.data){
                this.groups.tooltipText[dataLine.name][dataPoint.hour].value.text(dataPoint.level);
            }
        }
    }
// a generaliser avec x et y
    private _refreshTooltipsPosition(){
        for(let dataLine of this.data){
            for(let dataPoint of dataLine.data){
                let tooltipText = this.groups.tooltipText[dataLine.name][dataPoint.hour];
                let x = this.position.axisXPoints[dataPoint.hour].x + this.elementSize.tooltipLeftMargin;
                let y = parseFloat(this.groups.dataPoints[dataLine.name][dataPoint.hour].attr("cy")) + this.elementSize.tooltipUpMargin;
                tooltipText.level.attr("x",this.position.axisXPoints[dataPoint.hour].x + this.elementSize.tooltipLeftMargin)
                                .attr("y",parseFloat(this.groups.dataPoints[dataLine.name][dataPoint.hour].attr("cy")) + this.elementSize.tooltipUpMargin);
                tooltipText.hour.attr("x",parseFloat(tooltipText.level.attr("x")) + this.elementSize.tooltipHourLeftMargin)
                                .attr("y",parseFloat(tooltipText.level.attr("y")) + this.elementSize.tooltipHourUpMargin + tooltipText.level.node().getBoundingClientRect().height);
                tooltipText.value.attr("x",parseFloat(tooltipText.level.attr("x")) + this.elementSize.tooltipValueLeftMargin + tooltipText.level.node().getBoundingClientRect().width)
                                .attr("y",parseFloat(tooltipText.level.attr("y"))+ this.elementSize.tooltipValueUpMargin);
                                
                let tooltipBackground = this.groups.tooltipBackground[dataLine.name][dataPoint.hour];
                let width = tooltipText.level.node().getBoundingClientRect().width + this.elementSize.tooltipValueLeftMargin + tooltipText.value.node().getBoundingClientRect().width;
                tooltipBackground.attr("x",x)
                                    .attr("y",y - tooltipText.level.node().getBoundingClientRect().height)
                                    .attr("width",width)
            }
        }
    }

    private _showTooltips(datapoint){
        let index = datapoint.attr("hour");
        let level = datapoint.attr("level");

        let selectedClass = this.groups.tooltip[level][index].attr("class").replace(" show","");
        selectedClass = selectedClass+" show";
        this.groups.tooltip[level][index].attr("class",selectedClass);
    }

    private _hideTooltips(datapoint){
        if(datapoint != this.selectedDataPoint){
            let index = datapoint.attr("hour");
            let level = datapoint.attr("level");
            let selectedClass = this.groups.tooltip[level][index].attr("class").replace(" show","");
            this.groups.tooltip[level][index].attr("class",selectedClass);
        }
    }


    /***************************************************************************
     * COMPUTE POSITION
     ***************************************************************************/
    public computeSize(svgContainerSize){
        this.heightPx = svgContainerSize.height;
        this.widthPx = svgContainerSize.width;
    };
    /***************************************************************************
     * CONTROL VALUE ACCESSOR IMPLEMENTATION
     ***************************************************************************/

    public seDisabledState(isDisabled: boolean){
        this.disabled = isDisabled;
    }

    public writeValue(data){
        this.data  = data;
        this.processRefresh();
    }

    registerOnTouched(fn: () => void){
        this.onTouched = fn;
    }
   
    registerOnChange(fn: (data) => void): void {
        this.onChange = fn;
    }
    
     
    /***************************************************************************
     * TOOLS
     ***************************************************************************/

     public computeDataPointsPosition(y1,y2,dataValue,dataMin,dataMax){
        return (((y2-y1) * (dataMax - dataValue)) / (dataMax - dataMin)) + y1;
     }
     public computeDataPointsValue(y1,y2,dataY,dataMin,dataMax){
         let value = (((dataMax - dataMin) * (y2 - dataY)) / (y2 - y1)) + dataMin;
        if(isNull(this.formatter)){
            return Math.round( value * 100 + Number.EPSILON ) / 100
        }else{
            return this.formatter(value);
        }
     }

     private _convertToHour(i){
         if(i < 10){
             return "0"+i.toString()+":00";
         }else{
             return i.toString()+":00";
         }
     }

     private _emptyOrNull(value){
         if(isNull(value)){
             return true;
         }else if(value.length == 0){
             return true;
         }else{
             return false;
         }
     }
  }