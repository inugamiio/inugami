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

    /**************************************************************************
     * CONSTRUCTORS
     **************************************************************************/
    constructor(private el: ElementRef) {
        super(el);

        this.groups = {
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
            axisX : 0,
            axisY : 0,
        }
    }

    /***************************************************************************
     * RENDER
     ***************************************************************************/
    private renderLayout(svgComponentChild: any){        
        this._computeSizeTable();
        this._initPositionTable();

        this.renderXAxis(svgComponentChild);
        this.renderYAxis(svgComponentChild);
    }

    private renderXAxis(svgComponentChild){
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

    private renderYAxis(svgComponentChild){
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
        this.elementSize.axisLeftMargin     = this.elementRatio.axisXLeftMargin * this.widthPx;
         
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
    /***************************************************************************
     * COMPUTE POSITION
     ***************************************************************************/
    public computeSize(svgContainerSize){
        this.heightPx = svgContainerSize.height;
        this.widthPx = svgContainerSize.width;
    };


  }