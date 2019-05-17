import { Component, ElementRef, Input, Output, EventEmitter, forwardRef,HostBinding } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

import { SvgComponent } from 'js/app/components/charts/svg_component/svg.component.ts';

@Component({
    templateUrl: 'js/app/components/forms/dynamic.levels.html',
    selector: 'dynamic-levels',
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => DynamicLevels),
            multi: true
        }
    ],
    
})
export class DynamicLevels extends SvgComponent implements ControlValueAccessor {

    @Input() width                      : number;
    @Input() height                     : number;

    @Input() formatter                  : any;
    @Input() disabled                   : boolean;
    @Input() readonly                   : boolean;
    @Input() validator                  : any;
    @Input() tabindex                   : number;
    @Input() name                       : string;
    @Input() style                      : string;
    @Input() styleClass                 : string;
    @Input() inputId                    : string;

    @Output() onChange                  = new EventEmitter<any>();
    @Output() onClick                   = new EventEmitter<any>();

    @HostBinding('class') cssClass      = "inugami-input-dynamic-levels";
    
    @HostBinding('id') rootId           = this.inputId;
    @HostBinding('tabindex') rootindex  = this.tabindex;

    private axisXPointsNumber           : any;
    private axisYPointsNumber           : any;

    private onTouched                   : any;
    private onChangeAccessor            : any;

    private heightPx                    : any;
    private widthPx                     : any;

    private groups                      : any;
    private position                    : any;
    private elementSize                 : any;
    private elementRatio                : any;

    private data                        : any;
    private maxValue                    : number;
    private minValue                    : number;
    private dynamicMode                 : boolean;

    private selectedDataPoint           : any;
    private selectedDataPointsLine      : any;

    private dblClick                    : boolean;
    /**************************************************************************
     * CONSTRUCTORS
     **************************************************************************/
    constructor(private el: ElementRef) {
        super(el);

        this.groups = {
            svgComponentChild   : {},
            axisXgroup          : {},
            axisYgroup          : {},
            axisX               : {},
            axisY               : {},
            axisYPoints         : [],
            axisXPoints         : [],
            axisYPointsGroup    : {},
            axisXPointsGroup    : {},
            dataPointsGroup     : {},
            dataPoints          : [],
            tooltip             : [],
            tooltipText         : {},
            tooltipBackground   : {},
            path                : {},
            axisXLabel          : {},
            axisYLabel          : {},
            dataGroup           : {},
        }
    }

    private afterContentInit() {

        this.position = {
            axisX               : {},
            axisY               : {},
            axisXPoints         : [],
            axisYPoints         : [],
        }

        this.elementRatio = {
            axisX               : 0.90,
            axisXLeftMargin     : 0.05,
            axisXRightMargin    : 0.05,


            axisY               : 0.8,
            axisYUpMargin       : 0.1,
            axisYDownMargin     : 0.1
        }

       

        this.elementSize = {
            axisX                       : 0,
            axisY                       : 0,
            axisLeftMargin              : 0,
            axisUpMargin                : 0,
            axisRightMargin             : 0,
            axisDownMargin              : 0,
            axisXPointsMargin           : 0,
            axisYPointsMargin           : 0,
            tooltipLeftMargin           : 0,
            tooltipUpMargin             : 0,
            tooltipHourUpMargin         : 0,
            tooltipHourLeftMargin       : 0,
            tooltipLevelpMargin         : 0,
            tooltipLevelLeftMargin      : 0,
            tooltipBackgroundMargin     : 0,
            axisXLabelUpMargin          : 0,
            axisYLabelRightMargin       : 0,
        }

        this.data = [];
        this.minValue           = 0;
        this.maxValue           = 100;


        this.axisXPointsNumber = parseInt(org.inugami.formatters.messageValue("alert.edit.dynamic.levels.axis.x.points.number"));
        this.axisYPointsNumber = parseInt(org.inugami.formatters.messageValue("alert.edit.dynamic.levels.axis.y.points.number"));
    }

    /***************************************************************************
     * RENDER
     ***************************************************************************/
    private renderLayout(svgComponentChild: any) {
        this.groups.svgComponentChild = svgComponentChild;
        this.groups.dataGroup = this.groups.svgComponentChild.append("g").attr("class", "data");
        this._computeSizeTable();
        this._initPositionTable();

        this._renderAxis(svgComponentChild);
        this._renderXAxisLabel();
        this._renderYAxisLabel();

        this._initEvent();
        this.processRefresh();
    }

    private _renderAxis(svgComponentChild) {
        let axisGroup = svgComponentChild.append("g").attr("class", "layout").lower();
        this._renderXAxis(axisGroup);
        this._renderYAxis(axisGroup);
    }

    private _renderXAxis(svg) {
        let axisXGroup = svg.append("g").attr("class", "axis-x");
        let axisX = axisXGroup.append("line");
        let axisXPoints = axisXGroup.append("g").attr("class", "axisXPoints");
        for (let i = 0; i < this.position.axisXPoints.length; i++) {
            let axisXPoint = axisXPoints.append("circle").attr("cx", this.position.axisXPoints[i].x)
                .attr("cy", this.position.axisXPoints[i].y)
                .attr("r", 2)
                .attr("class", "tick")
                .lower();
            this.groups.axisXPoints.push(axisXPoint);

        }

        this.groups.axisXGroup = axisXGroup;
        this.groups.axisX = axisX;
        this.groups.axisXPointsGroup = axisXPoints
    }

    private _renderYAxis(svg) {
        let axisYGroup = svg.append("g").attr("class", "axis-y");
        let axisY = axisYGroup.append("line");
        let axisYPoints = axisYGroup.append("g").attr("class", "axisYPoints");
        for (let i = 0; i < this.position.axisYPoints.length; i++) {
            let axisYPoint = axisYPoints.append("circle")
                .attr("cy", this.position.axisYPoints[i].y)
                .attr("r", 2)
                .attr("class", "tick");
            this.groups.axisYPoints.push(axisYPoint);
        }
        this.groups.axisYGroup = axisYGroup;
        this.groups.axisY = axisY;
        this.groups.axisYPointsGroup = axisYPoints
    }

    private _renderYAxisLabel() {
        this.groups.axisYLabel = {};
        for (let i = 0; i < this.groups.axisYPoints.length; i++) {
            if (i == 0  || i== Math.floor(this.axisYPointsNumber/2) || i == this.axisYPointsNumber - 1) {
                let label = this.groups.axisYGroup.append("text")
                    .attr("class", "tick-label")
                    .text((this.maxValue - this.minValue) * i / (this.groups.axisYPoints.length - 1) + this.minValue); 
                this.groups.axisYLabel[i] = label;
            }
        }
    }

    private _renderXAxisLabel() {
        this.groups.axisXLabel = {};
        for (let i = 0; i < this.groups.axisXPoints.length; i++) {
            if (  i == 0 || i== Math.floor(this.axisXPointsNumber/4) || i== Math.floor(this.axisXPointsNumber/2) || i == this.axisXPointsNumber - 1) {
                let label = this.groups.axisXGroup.append("text")
                    .attr("class", "tick-label")
                    .text(i + "h");

                let textWidth = label.node().getBoundingClientRect().width;
                let textHeight = label.node().getBoundingClientRect().height;
                this.groups.axisXLabel[i] = label;
            }

        }
    }

    /***************************************************************************
     * INITIALIZATION
     ***************************************************************************/

    private _initPositionTable() {
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

    private _initAxisXPointsPosition() {
        this.position.axisXPoints = [];
        for (let i = 1; i <= this.axisXPointsNumber; i++) {
            this.position.axisXPoints.push({
                x: this.elementSize.axisLeftMargin + i * this.elementSize.axisXPointsMargin,
                y: this.position.axisX.y1,
            });
        }
    }
    private _initAxisYPointsPosition() {
        this.position.axisYPoints = [];
        for (let i = this.axisYPointsNumber - 1; i >= 0; i--) {
            this.position.axisYPoints.push({
                x: this.position.axisY.x1,
                y: this.elementSize.axisUpMargin + i * this.elementSize.axisYPointsMargin,
            });
        }
    }

    private _computeSizeTable() {


        this.elementSize.axisX = this.elementRatio.axisX * this.widthPx;
        this.elementSize.axisY = this.elementRatio.axisY * this.heightPx;
        this.elementSize.axisXPointsMargin = this.elementSize.axisX / this.axisXPointsNumber;
        this.elementSize.axisYPointsMargin = this.elementSize.axisY / this.axisYPointsNumber;
        this.elementSize.axisUpMargin = this.elementRatio.axisYUpMargin * this.heightPx;
        this.elementSize.axisDownMargin = this.elementRatio.axisYDownMargin * this.heightPx;
        this.elementSize.axisLeftMargin = this.elementRatio.axisXLeftMargin * this.widthPx;
        this.elementSize.axisRightMargin = this.elementRatio.axisXRightMargin * this.widthPx;
        this.elementSize.tooltipLeftMargin = - this.elementSize.axisXPointsMargin / 4;
        this.elementSize.tooltipUpMargin = this.elementSize.axisYPointsMargin;
        this.elementSize.tooltipHourLeftMargin = 0;
        this.elementSize.tooltipHourUpMargin = this.elementSize.axisYPointsMargin / 4;
        this.elementSize.tooltipLevelLeftMargin = this.elementSize.axisXPointsMargin;
        this.elementSize.tooltipLevelUpMargin = this.elementSize.axisYPointsMargin / 4;
        this.elementSize.tooltipBackgroundMargin = this.elementSize.axisXPointsMargin / 3;
        this.elementSize.axisXLabelUpMargin = this.elementSize.axisXPointsMargin / 5;
        this.elementSize.axisYLabelRightMargin = this.elementSize.axisYPointsMargin / 5;

    }

    private _initEvent() {
        this.compos.svg.node().addEventListener('mousemove', event => { if (!this.readonly && !this.disabled) { this._moveDataPoints(event.clientY); }});
        this.compos.svg.node().addEventListener('mouseleave', event => { if (!this.readonly && !this.disabled) { this._unselectDataPoint()}});
        document.body.onmouseup = event => { this._unselectDataPoint() };
    }

    /***************************************************************************
     * NEW DATA LINE 
     ***************************************************************************/
    public addNewData(name: string) {
        if (!this.disabled && !this.readonly) {
            let line = this.data.find(function (element) {
                return element.name == name;
            });

            if (this._emptyOrNull(line)) {
                let dataObject = {
                    "name": name,
                    "data": []
                };
                dataObject["data"] = [];
                this.data.push(dataObject);
                let targetPoint = - 1;
                for (let i = 0; i < this.groups.axisYPoints.length; i++) {
                    if (targetPoint == - 1 ) {
                        if (this._canAdd(i)) {
                            targetPoint = i;
                        }
                    }
                }
                if(targetPoint == -1){
                    targetPoint = 0;
                }
                let dataPointGroup = this.groups.dataGroup.append("g")
                    .attr("class", "level " + name);
                this.groups.dataPoints[name] = [];
                let targetValue = targetPoint * ((this.maxValue - this.minValue)/(this.groups.axisYPoints.length - 1))  + this.minValue;
                let initialYposition = this.computeDataPointsPositionFromLevel(this.position.axisY.y1, this.position.axisY.y2 - this.elementSize.axisYPointsMargin
                    , targetValue, this.minValue, this.maxValue);
                if (this.groups.tooltipText === undefined) {
                    this.groups.tooltipText = {};
                }
                if (this.groups.tooltipBackground === undefined) {
                    this.groups.tooltipBackground = {};
                }
                if (this.groups.tooltip === undefined) {
                    this.groups.tooltip = {};
                }
                if (this.groups.dataPointGroup === undefined) {
                    this.groups.dataPointGroup = {};
                }
                this.groups.tooltipText[name] = [];
                this.groups.tooltipBackground[name] = [];
                this.groups.tooltip[name] = [];

                let pathValues = [];
                let dotsGroup = dataPointGroup.append("g").attr("class", "dots");
                let level = this.computeDataPointsLevel(this.position.axisY.y1, this.position.axisY.y2 - this.elementSize.axisYPointsMargin,
                    initialYposition, this.minValue, this.maxValue);
                for (let i = 0; i < this.groups.axisXPoints.length; i++) {
                    let dataPoint = dotsGroup.append("circle")
                        .attr("r", 4) 
                        .attr("lineName", name)
                        .attr("hour", i)
                        .attr("class", "dot hour-" + i + " " + name);
                    this.groups.dataPoints[name].push(dataPoint);
                    dataPoint.lower();
                    this.groups.dataPointsGroup[name] = dataPointGroup;
                    pathValues.push(this.position.axisXPoints[i].x);
                    pathValues.push(initialYposition);
                    this._newTooltips(dataPointGroup, name, level, i);

                    dataObject["data"].push({
                        "hour": i,
                        "level": level
                    });
                }
                for (let dataPoint of this.groups.dataPoints[name]) {
                    this._addMouse(dataPoint, this.groups.dataPoints[name]);
                }
                this._newPath(dataPointGroup, name, pathValues);
                this.processRefresh();
            }
        }
    }

    private _newTooltips(dataPointGroup, name, level, i) {
        let tooltip = dataPointGroup.append("g").attr("class", "tooltips");
        let tooltipBackground = tooltip.append("rect");
        let tooltipNameText = tooltip.append("text").text(name);

        let tooltipHourText = tooltip.append("text").text(this._convertToHour(i));
        let tooltipLevelText = tooltip.append("text").text(level);


        let tooltipTextGroup = {
            "name": tooltipNameText,
            "hour": tooltipHourText,
            "level": tooltipLevelText,
        };


        this.groups.tooltipText[name].push(tooltipTextGroup);
        this.groups.tooltipBackground[name].push(tooltipBackground);
        this.groups.tooltip[name].push(tooltip);
    }

    private _canAdd(indexY) {
        let canAdd = true;
        let isFree = [];
        for(let i = 0; i < this.axisXPointsNumber; i++){
            isFree[i] = true;
            for(let dataLine of this.data){
                for(let datapoint of dataLine.data){
                    this.checkIfFree(datapoint,indexY,isFree,i);
                }
            }
        }
        return isFree.indexOf(true) != -1;
    }

    private checkIfFree(datapoint,indexY,isFree,i){
        if(datapoint.level == ((indexY) * ((this.maxValue - this.minValue)/(this.groups.axisYPoints.length - 1)) ) + this.minValue){
            isFree[i] = false;        
        }  
    }

    private _newPath(dataPointGroup, name, pathValues) {
        pathValues = this._formatPathValues(pathValues);
        let path = dataPointGroup.append("path")
            .attr("d", pathValues)
            .attr("fill", "none")
            .attr("class", "curve");
        path.lower();

        if (this.groups.path[name] === undefined) {
            this.groups.path[name] = {};
        }
        this.groups.path[name] = path;
    }

    private _addMouse(dataPoint, dataPointsLine) {
        let self = this;
        dataPoint.on('mousedown', function () {
            self._mouseDown(dataPoint, dataPointsLine)
        });
        dataPoint.node().addEventListener("click", event => { if (!this.readonly && !this.disabled) { this._alignAfter(event, dataPoint); this._emitPointInfo(dataPoint) } });
        dataPoint.node().addEventListener("dblclick", event => { if (!this.readonly && !this.disabled) { this._alignBefore(event, dataPoint) } });
        dataPoint.node().addEventListener("mouseenter", event => { if (!this.disabled) { this._showTooltips(dataPoint) } });
        dataPoint.node().addEventListener("mouseleave", event => { if (!this.disabled) { this._hideTooltips(dataPoint) } });
    }

    private _mouseDown(dataPoint, dataPointsLine) {
        if (!this.readonly && !this.disabled) {
            this._setSelectedDataPoint(dataPoint);
            this._setSelectedDataPointsLine(dataPointsLine);
        }
    }

    private _alignAfter(event, dataPoint) {
        if (event.shiftKey) {

            if (!this.dblClick) {
                this.dblClick = true;
                let self = this;
                setTimeout(function () {
                    if (self.dblClick) {
                        self._moveAllPointsAfter(dataPoint);
                        self._readjustPosition();
                        self.dblClick = false;
                    }
                }, 300);
            } else {
                this.dblClick = false;
            }
        }
    }

    private _alignBefore(event, dataPoint) {
        if (event.shiftKey) {
            this._moveAllPointsBefore(dataPoint);
            this._readjustPosition();
        }
    }

    private _emitPointInfo(dataPoint) {
        let dataline = this.data.find(function (element) {
            return element.name == dataPoint.attr("lineName");
        });

        let pointData = dataline.data.find(function (element) {
            return element.hour == dataPoint.attr("hour");
        })

        let emitedObject = {
            "name": dataline.name,
            "hour": pointData.hour,
            "level": pointData.level,
        };
        this.onClick.emit(emitedObject);
    }
    /***************************************************************************
     * DELETE LINE 
     ***************************************************************************/

    public deleteLine(name) {
        let index = this.data.findIndex(function (element) {
            return element.name == name;
        })
        if (index > -1) {
            this.data.splice(index, 1);
            this.groups.dataPoints[name] = [];
            this.groups.tooltip[name] = [];
            this.groups.tooltipText[name] = [];
            this.groups.tooltipBackground[name] = [];
            this.groups.path[name] = {};
            this.groups.dataPoints[name] = [];

            this.groups.dataPointsGroup[name].remove();
            this.processRefresh();
        }
    }

    /***************************************************************************
     * MOVE DATA POINTS
    ***************************************************************************/

    private _setSelectedDataPoint(dataPoint: any) {
        this.selectedDataPoint = dataPoint;
        dataPoint.attr("class", dataPoint.attr("class") + " selected")
        this._showTooltips(dataPoint);
    }
    private _setSelectedDataPointsLine(dataPointsLine: any) {
        this.selectedDataPointsLine = dataPointsLine;

    }

    private _unselectDataPoint() {
        if (this.selectedDataPoint != null) {
            let selectedPoint = this.selectedDataPoint;
            let selectedClass = this.selectedDataPoint.attr("class").replace(" selected", "");
            this.selectedDataPoint.attr("class", selectedClass);
            this.selectedDataPoint = null;
            this.selectedDataPointsLine = null;
            this._hideTooltips(selectedPoint);
        }
    }

    private _moveDataPoints(yPos: any) {
        org.inugami.asserts.isTrue(this.minValue < this.maxValue, "min value should be lower than max value");
        if (isNotNull(this.selectedDataPoint)) {

            if (!this.dynamicMode) {
                this._moveDataPointsLine(yPos);
            } else {
                this._moveSingleDataPoint(yPos);
            }
            this._readjustLinePosition(this.selectedDataPoint);
        }
    }

    private _moveDataPointsLine(yPos) {

        let pos = this.transformMouseCoordToSVG(yPos);
        for (let datapoint of this.selectedDataPointsLine) {
            let name = this.selectedDataPoint.attr("lineName");
            let lineData = this.data.find(function (element) {
                return element.name == name;
            })
            org.inugami.asserts.notNull(lineData, "no line with level same as selected point found");

            let self = this;

            for (let pointData of lineData.data) {
                pointData.level = this.computeDataPointsLevel(this.position.axisY.y1, this.position.axisY.y2 - this.elementSize.axisYPointsMargin,
                    pos, this.minValue, this.maxValue);
            }
        }
    }

    private _moveSingleDataPoint(yPos) {
        let pos = this.transformMouseCoordToSVG(yPos);
        let name = this.selectedDataPoint.attr("lineName");
        let lineData = this.data.find(function (element) {
            return element.name == name;
        })
        org.inugami.asserts.notNull(lineData, "no line with level same as selected point found");

        let self = this;
        let pointData = lineData.data.find(function (element) {
            return element.hour == self.selectedDataPoint.attr("hour");
        })
        org.inugami.asserts.notNull(pointData, "no datapoint with same hour as selected point found");


        pointData.level = this.computeDataPointsLevel(this.position.axisY.y1, this.position.axisY.y2 - this.elementSize.axisYPointsMargin,
            pos, this.minValue, this.maxValue);
    }

    private _moveAllPointsAfter(dataPoint) {
        
        let name = dataPoint.attr("lineName");
        let lineData = this.data.find(function (element) {
            return element.name == name;
        })
        org.inugami.asserts.notNull(lineData, "no line with level same as selected point found");

        let self = this;

        for (let pointData of lineData.data) {
            if (parseInt(pointData.hour) > parseInt(dataPoint.attr("hour"))) {
                pointData.level = this.computeDataPointsLevel(this.position.axisY.y1, this.position.axisY.y2 - this.elementSize.axisYPointsMargin,
                    dataPoint.attr("cy"), this.minValue, this.maxValue);
            }
        }
        this.processRefresh();
    }

    private _moveAllPointsBefore(dataPoint) {
        let name = dataPoint.attr("lineName");
        let lineData = this.data.find(function (element) {
            return element.name == name;
        })
        org.inugami.asserts.notNull(lineData, "no line with level same as selected point found");

        let self = this;

        for (let pointData of lineData.data) {
            if (parseInt(pointData.hour) < parseInt(dataPoint.attr("hour"))) {
                pointData.level = this.computeDataPointsLevel(this.position.axisY.y1, this.position.axisY.y2 - this.elementSize.axisYPointsMargin,
                    dataPoint.attr("cy"), this.minValue, this.maxValue);
            }
        }
    }

    private transformMouseCoordToSVG(y) {
        var ctm = this.compos.svg.node().getScreenCTM();
        return y = (y - ctm.f) / ctm.d;
    }
    private transformMouseCoordToscreen(y) {
        var ctm = this.compos.svg.node().getScreenCTM();
        return y = (y * ctm.d) - ctm.f;
    }

    private _readjustPosition() {
        for (let dataPointsLine of this.data) {
            for (let datapoint of dataPointsLine.data) {
                this._putPointInBound(datapoint);
            }
        }
        this.processRefresh();
    }

    private _readjustLinePosition(dataPoint) {
        let dataline = this.data.find(function (element) {
            return element.name == dataPoint.attr("lineName");
        });
            for (let datapoint of dataline.data) {
                this._putPointInBound(datapoint);
            }
        this.processLineRefresh(dataline);
    }

    private _putPointInBound(datapoint){
        if (datapoint.level > this.maxValue) {
            datapoint.level = this.maxValue;
        } else if (datapoint.level < this.minValue) {
            datapoint.level = this.minValue;
        }
    }

    /***************************************************************************
     * REFRESH
     ***************************************************************************/
    public processRefresh() {
        this._computeSizeTable();
        this._initPositionTable();
        this._refreshValues();
        if (isNotNull(this.validator)) {
            let error = this.validator(this.data);
            this._setErrorState(error);
        }
        if (isNotNull(this.onChangeAccessor)) {
            this.onChangeAccessor(this.data);
        }
        this.onChange.emit(this.data)
    }

    public processLineRefresh(dataLine) {
        this._refreshLineValues(dataLine);
        if (isNotNull(this.validator)) {
            let error = this.validator(this.data);
            this._setErrorState(error);
        }
        if (isNotNull(this.onChangeAccessor)) {
            this.onChangeAccessor(this.data);
        }
        this.onChange.emit(this.data)
    }

    private _refreshValues() {
        this._refreshAxisValues();
        this._refreshAxisXPointsValues();
        this._refreshAxisYPointsValues();
        this._refreshDataPointsPosition();
        this._refreshTooltipsValue();
        this._refreshTooltipsPosition();
        this._refreshPathPosition();
        this._refreshXAxisLabel();
        this._refreshYAxisLabel();
    }

    private _refreshLineValues(dataLine){
        this._refreshLineDataPointsPosition(dataLine);
        this._refreshLineTooltipsValue(dataLine);
        this._refreshLineTooltipsPosition(dataLine);
        this._refreshLinePathPosition(dataLine);
    }

    private _refreshAxisValues() {
        this.groups.axisX.attr("x1", this.position.axisX.x1)
            .attr("y1", this.position.axisX.y1)
            .attr("x2", this.position.axisX.x2)
            .attr("y2", this.position.axisX.y2);

        this.groups.axisY.attr("x1", this.position.axisY.x1)
            .attr("y1", this.position.axisY.y1)
            .attr("x2", this.position.axisY.x2)
            .attr("y2", this.position.axisY.y2);
    }

    private _refreshAxisXPointsValues() {
        for (let i = 0; i < this.groups.axisXPoints.length; i++) {
            this.groups.axisXPoints[i].attr("cx", this.position.axisXPoints[i].x);
            this.groups.axisXPoints[i].attr("cy", this.position.axisXPoints[i].y);
        }
    }
    private _refreshAxisYPointsValues() {
        for (let i = 0; i < this.groups.axisYPoints.length; i++) {
            this.groups.axisYPoints[i].attr("cx", this.position.axisYPoints[i].x);
            this.groups.axisYPoints[i].attr("cy", this.position.axisYPoints[i].y);
        }
    }

    private _refreshDataPointsPosition() {
        for (let dataLine of this.data) {
            this._refreshLineDataPointsPosition(dataLine);
        }
    }

    private _refreshLineDataPointsPosition(dataLine) {
        for (let j = 0; j < dataLine.data.length; j++) {
            this._computeDataPointsPosition(dataLine,j);
        }
    }

    private _computeDataPointsPosition(dataLine,pointIndex){
        let yPos = this.computeDataPointsPositionFromLevel(this.position.axisY.y1, this.position.axisY.y2 - this.elementSize.axisYPointsMargin, dataLine.data[pointIndex].level, this.minValue, this.maxValue);
                this.groups.dataPoints[dataLine.name][pointIndex].attr("cy", yPos)
                    .attr("cx", this.position.axisXPoints[pointIndex].x);
    }

    private _refreshTooltipsValue() {
        for (let dataLine of this.data) {
            this._refreshLineTooltipsValue(dataLine);
        }
    }

    private _refreshLineTooltipsValue(dataLine) {
        for (let dataPoint of dataLine.data) {
            this.groups.tooltipText[dataLine.name][dataPoint.hour].level.text(dataPoint.level);
        }
    }

    private _refreshTooltipsPosition() {
        for (let dataLine of this.data) {
            this._refreshLineTooltipsPosition(dataLine);
        }
    }

    private _refreshLineTooltipsPosition(dataLine){
        for (let dataPoint of dataLine.data) {
            this._computeTooltipsPosition(dataLine,dataPoint);
        }
    }

    private _computeTooltipsPosition(dataLine,dataPoint){
        let tooltipText = this.groups.tooltipText[dataLine.name][dataPoint.hour];
        let x = this.position.axisXPoints[dataPoint.hour].x + this.elementSize.tooltipLeftMargin;
        let y = parseFloat(this.groups.dataPoints[dataLine.name][dataPoint.hour].attr("cy")) + tooltipText.name.node().getBoundingClientRect().height + this.elementSize.tooltipUpMargin;
        tooltipText.name.attr("x", x)
            .attr("y", y);
        tooltipText.hour.attr("x", parseFloat(tooltipText.name.attr("x")) + this.elementSize.tooltipHourLeftMargin)
            .attr("y", parseFloat(tooltipText.name.attr("y")) + this.elementSize.tooltipHourUpMargin + tooltipText.name.node().getBoundingClientRect().height);
        tooltipText.level.attr("x", parseFloat(tooltipText.name.attr("x")) + this.elementSize.tooltipLevelLeftMargin + tooltipText.name.node().getBoundingClientRect().width)
            .attr("y", parseFloat(tooltipText.name.attr("y")) + this.elementSize.tooltipLevelUpMargin);

        let tooltipBackground = this.groups.tooltipBackground[dataLine.name][dataPoint.hour];
        let width = tooltipText.name.node().getBoundingClientRect().width + this.elementSize.tooltipLevelLeftMargin + tooltipText.level.node().getBoundingClientRect().width;
        let height = tooltipText.name.node().getBoundingClientRect().height + this.elementSize.tooltipHourUpMargin + tooltipText.hour.node().getBoundingClientRect().height
        tooltipBackground.attr("x", x - this.elementSize.tooltipBackgroundMargin)
            .attr("y", y - tooltipText.name.node().getBoundingClientRect().height - this.elementSize.tooltipBackgroundMargin)
            .attr("width", width + 2 * this.elementSize.tooltipBackgroundMargin)
            .attr("height", height + 2 * this.elementSize.tooltipBackgroundMargin);
    }

    private _refreshPathPosition() {
        for (let dataLine of this.data) {
            this._refreshLinePathPosition(dataLine);
        }
    }

    private _refreshLinePathPosition(dataLine) {
        let pathValues = [];
        for (let dataPoint of dataLine.data) {
            this._computePathPosition(dataPoint, pathValues);
        }
        let path = this.groups.path[dataLine.name];
        path.attr("d", this._formatPathValues(pathValues));

    }



    private _computePathPosition(dataPoint,pathValues){
        pathValues.push(this.position.axisXPoints[dataPoint.hour].x)
        pathValues.push(this.computeDataPointsPositionFromLevel(this.position.axisY.y1, this.position.axisY.y2 - this.elementSize.axisYPointsMargin, dataPoint.level
            , this.minValue, this.maxValue));
    }

    private _showTooltips(datapoint) {
        let index = datapoint.attr("hour");
        let name = datapoint.attr("lineName");

        let selectedClass = this.groups.tooltip[name][index].attr("class").replace(" show", "");
        selectedClass = selectedClass + " show";
        this.groups.tooltip[name][index].attr("class", selectedClass);
    }

    private _hideTooltips(datapoint) {
        if (datapoint != this.selectedDataPoint) {
            let index = datapoint.attr("hour");
            let name = datapoint.attr("lineName");
            let selectedClass = this.groups.tooltip[name][index].attr("class").replace(" show", "");
            this.groups.tooltip[name][index].attr("class", selectedClass);
        }
    }

    private _refreshXAxisLabel() {
        for (let key of Object.keys(this.groups.axisXLabel)) {
            let textWidth = this.groups.axisXLabel[key].node().getBoundingClientRect().width;
            let textHeight = this.groups.axisXLabel[key].node().getBoundingClientRect().height;
            this.groups.axisXLabel[key].attr("x", this.position.axisXPoints[key].x - textWidth / 2).attr("y", this.position.axisXPoints[key].y + textHeight + this.elementSize.axisXLabelUpMargin);
        }
    }

    private _refreshYAxisLabel() {
        for (let key of Object.keys(this.groups.axisYLabel)) {
            let label = this.groups.axisYLabel[key];
            let text = (this.maxValue - this.minValue) * parseInt(key) / (this.groups.axisYPoints.length - 1) + this.minValue;
            text =  isNull(this.formatter) ?   Math.round(text * 100 + Number.EPSILON) / 100 : this.formatter(text);
            
            label.text(text);
            let textWidth = label.node().getBoundingClientRect().width;
            let textHeight = label.node().getBoundingClientRect().height;
            label.attr("x", this.position.axisYPoints[key].x - textWidth - this.elementSize.axisYLabelRightMargin)
                .attr("y", this.position.axisYPoints[key].y + textHeight / 2);
                
        }
    }

    private _setErrorState(error) {
        this.cssClass = this.cssClass.replace(" validation-error","");
       if(isNotNull(error)){
            this.cssClass+=" validation-error";
       }
    }

    /***************************************************************************
     * COMPUTE POSITION
     ***************************************************************************/
    public computeSize(svgContainerSize) {
        this.heightPx = svgContainerSize.height;
        this.widthPx = svgContainerSize.width;
    };
    /***************************************************************************
     * CONTROL VALUE ACCESSOR IMPLEMENTATION
     ***************************************************************************/

    public seDisabledState(isDisabled: boolean) {
        this.disabled = isDisabled;
    }

    public writeValue(data) {
            if (isNull(data)) {
                data = [];
            }
            if (isNotNull(this.elementSize)) {
                for (let dataLine of data) {
                    org.inugami.asserts.notNull(dataLine.name, "data lines must have names");
                    org.inugami.asserts.isTrue(dataLine.data.length == this.axisXPointsNumber, "there must be the same number of points as there is of x axis points");
                }

                for (let keys of Object.keys(this.groups.dataPoints)) {
                    this.deleteLine(keys);
                }
                for (let dataLine of data) {
                    this.addNewData(dataLine.name);
                }
                this.data = data;
                this._readjustPosition();
            }
    }

    registerOnTouched(fn: () => void) {
        this.onTouched = fn;
    }

    registerOnChange(fn: (data) => void): void {
        this.onChangeAccessor = fn;
    }

    /***************************************************************************
     * TOOLS
     ***************************************************************************/

    public computeDataPointsPositionFromLevel(y1, y2, dataValue, dataMin, dataMax) {
        return (((y2 - y1) * (dataMax - dataValue)) / (dataMax - dataMin)) + y1;
    }

    public computeDataPointsLevel(y1, y2, dataY, dataMin, dataMax) {
        let level = (((dataMax - dataMin) * (y2 - dataY)) / (y2 - y1)) + dataMin;
        return isNull(this.formatter) ?   Math.round(level * 100 + Number.EPSILON) / 100 :  this.formatter(level);
    }

    private _convertToHour(i) {
        if (i < 10) {
            return "0" + i.toString() + ":00";
        }else {
            return i.toString() + ":00";
        }
    }

    private _emptyOrNull(value) {
        return isNull(value) || value.length == 0 ;

    }

    private _formatPathValues(pathValues) {
        for (let i = pathValues.length - 1; i > 1; i--) {
            pathValues[i] = pathValues[i] - pathValues[i - 2];
        }
        return  svg.builder.path(pathValues, false);
        
    }

    setMinValue(minValue){
        if(isNaN(minValue)){
            minValue = this.maxValue -100;
        }
        this.minValue =minValue;
        this._readjustPosition();
    }


    setMaxValue(maxValue){
        if(isNaN(maxValue)){
            maxValue = this.minValue + 100;
        }
        this.maxValue = maxValue;
        this._readjustPosition();
    }
}