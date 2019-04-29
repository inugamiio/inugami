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
        }
    }

    private afterContentInit() {

        this.position = {
            axisX                   : {},
            axisY                   : {},
            axisXPoints             : [],
            axisYPoints             : [],
            dataPoints              : [],
        }

        this.elementRatio = {
            axisX               : 0.8,
            axisXLeftMargin     : 0.1,
            axisXRightMargin    : 0.1,


            axisY               : 0.8,
            axisYUpMargin       : 0.1,
            axisYDownMargin     : 0.1
        }
        
        this._initElementRatio();

        this.elementSize = { 
            axisX               : 0,
            axisY               : 0,
            axisLeftMargin      : 0,
            axisUpMargin        : 0,
            axisRightMargin     : 0,
            axisDownMargin      : 0,
            axisXPointsMargin   : 0,
            axisYPointsMargin   : 0,
        }

        this.data = [];
        

        // FAIRE GAFFE A CA , SI ON LNENVOIT VIA DU BINDING FAUT QUE LE BIDNIG PRENNE LE DESSUS
        this.staticMode         = true;
        this.minValue           = 0;
        this.maxValue           = 100;
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
        for(let i = 12 ; i >= 0; i-- ){
            this.position.axisYPoints.push({
                x: this.position.axisY.x1,
                y: this.elementSize.axisUpMargin + i * this.elementSize.axisYPointsMargin,
              });
        }
    }

    private _computeSizeTable() {
        this.elementSize.axisX              = this.elementRatio.axisX * this.widthPx;
        this.elementSize.axisY              = this.elementRatio.axisY * this.heightPx;
        this.elementSize.axisXPointsMargin  = this.elementSize.axisX / 24;
        this.elementSize.axisYPointsMargin  = this.elementSize.axisY / 13;
        this.elementSize.axisUpMargin       = this.elementRatio.axisYUpMargin * this.heightPx;
        this.elementSize.axisDownMargin     = this.elementRatio.axisYDownMargin * this.heightPx;
        this.elementSize.axisLeftMargin     = this.elementRatio.axisXLeftMargin * this.widthPx;
        this.elementSize.axisRightMargin    = this.elementRatio.axisXRightMargin * this.widthPx;
         
    }

    private _initEvent(){
        this.compos.svg._groups[0][0].addEventListener('mousemove',event => { this._moveDataPoints(event.clientY);});
        this.compos.svg._groups[0][0].addEventListener('mouseleave',event => {this.selectedDataPoint = null});
        document.body.onmouseup = event => {this._setSelectedDataPoint(null)
                                            this._setSelectedDataPointsLine(null);}
    }

    /***************************************************************************
     * NEW DATA LINE 
     ***************************************************************************/
    public addNewData(lineLevel : string){
        if(isNull(this.data.find(function(element){
            return element.name == lineLevel;
        }))){
            let dataObject =  {"name" : lineLevel,
                               "data" : []};
            dataObject["data"] = [];
            this.data.push(dataObject);
            
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
                let tooltip = dataPointGroup.append("g").attr("class","tooltips");
                tooltip.append("text").text("TEST DE TEXT").attr("x",this.position.axisXPoints[i].x).attr("y",initialYposition);
                dataObject.data.push({"hour" : i,
                                      "level": this.computeDataPointsValue(this.position.axisY.y1,this.position.axisY.y2 - this.elementSize.axisYPointsMargin,
                                        initialYposition,this.minValue,this.maxValue)});
                console.log(JSON.stringify(dataObject));
            }
            for(let dataPoint of this.groups.dataPoints[lineLevel]){
                this._addMouse(dataPoint,this.groups.dataPoints[lineLevel]);
            } 
        }
    }

    // pour checker si la ligne est vide et donc si on peut ajouter ici la nouvelle 
    private _canAdd(indexY){
        return true;
    }

    private _addMouse(dataPoint,dataPointsLine){
        let self = this;
        dataPoint.on('mousedown',function(){
            self._mouseDown(dataPoint,dataPointsLine)});
        dataPoint._groups[0][0].addEventListener("click", event => {this._alignAfter(event,dataPoint)});
        dataPoint._groups[0][0].addEventListener("dblclick", event => {this._alignBefore(event,dataPoint)})
    }
    
    private _mouseDown(dataPoint,dataPointsLine){
        this._setSelectedDataPoint(dataPoint);
        this._setSelectedDataPointsLine(dataPointsLine);
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
     * MOVE DATA POINTS
    ***************************************************************************/

    private _setSelectedDataPoint(dataPoint : any){
        this.selectedDataPoint = dataPoint;
    }
    private _setSelectedDataPointsLine(dataPointsLine : any){
        this.selectedDataPointsLine = dataPointsLine;
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
        var CTM = this.compos.svg._groups[0][0].getScreenCTM();
        return y = (y - CTM.f) / CTM.d;
    }
    private transformMouseCoordToscreen(y) {
        var CTM = this.compos.svg._groups[0][0].getScreenCTM();
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
    /***************************************************************************
     * COMPUTE POSITION
     ***************************************************************************/
    public computeSize(svgContainerSize){
        this.heightPx = svgContainerSize.height;
        this.widthPx = svgContainerSize.width;
    };

    /***************************************************************************
     * TOOLS
     ***************************************************************************/

     public computeDataPointsPosition(y1,y2,dataValue,dataMin,dataMax){
        return (((y2-y1) * (dataMax - dataValue)) / (dataMax - dataMin)) + y1;
     }
     public computeDataPointsValue(y1,y2,dataY,dataMin,dataMax){
        return (((dataMax - dataMin) * (y2 - dataY)) / (y2 - y1)) + dataMin;
     }

  }