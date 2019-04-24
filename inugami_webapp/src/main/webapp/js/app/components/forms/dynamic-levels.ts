import {
  Component,
  Input,
  ViewChild,
  ViewEncapsulation,
  ElementRef,
  AfterViewInit
} from '@angular/core';
import { ControlValueAccessor } from '@angular/forms';


import * as d3 from 'd3';
import { createContext } from 'vm';
@Component({
  templateUrl: 'js/app/components/forms/dynamic-levels.html',
  directives: [],
  encapsulation: ViewEncapsulation.None,
  selector: 'dynamic-levels',
})
export class DynamicLevels implements ControlValueAccessor {
  /***************************************************************************
   * ATTRIBUTES
   ***************************************************************************/
  @ViewChild('container') chartContainer: ElementRef;

  componentBaseStyleClass: string;
  @Input() styleClass: string;


  @Input() width: string;
  @Input() height: string;

  @Input() pluginName: string;
  @Input() event: string;

  private componentName: string = "compo";
  private ratios: any;
  private size: any;
  private compos: any;

  private _isAbsoluteWidth: boolean = false;
  private _isAbsoluteHeight: boolean = false;

  private position: any;

  private selectedDataPoint : any = false;


  /***************************************************************************
  * CONSTRUCTOR
  ***************************************************************************/
  constructor(private el: ElementRef) {
    let self = this;

    this.ratios = {
      svg: {
        height: 0,
        width: 0
      },
      margin: {
        top: 0, bottom: 0, left: 0, right: 0
      }
    };

    this.size = {
      svg: {
        height: null,
        width: null
      },
      margin: {
        top: 0, bottom: 0, left: 0, right: 0
      }
    };

    this.compos = {
      svg: null,
      child: null
    };

    this.position = {
      axisX: {},
      axisY: {},
      axisXPoints: [],
      axisYPoints: [],
      dataPoints: [],
    }
  }

  // -------------------------------------------------------------------------
  // @PostConstruct
  // -------------------------------------------------------------------------
  ngAfterContentInit() {
    this.afterContentInit();
    let self = this;
    if (isNull(this.width) || "" == this.width) {
      this.width = 1;
    }
    if (isNull(this.height) || "" == this.height) {
      this.height = 1;
    }


    if (isNaN(this.width)) {
      this._isAbsoluteWidth = this.width.endsWith("px");
    }

    if (isNaN(this.height)) {
      this._isAbsoluteHeight = this.height.endsWith("px");
    }


    org.inugami.events.addEventListener(org.inugami.events.type.RESIZE, function (data) {
      self._computeSize(data.detail);
      self.updateComponentSize(self.size.svg);
      self._processRefresh();
    });


    this.postConstruct();
    this._computeRatio();
    this._computeSize(org.inugami.values.screen);
    this._computePosition();
    this._renderLayout();
    this.updateComponentSize(this.size.svg);
    this._processRefresh();
  }


  public afterContentInit() { };
  public postConstruct() { };


  /***************************************************************************
  * COMPUTE RATIO
  ***************************************************************************/
  private _computeRatio() {
    let parents = svg.components.searchParent(this.el.nativeElement.parentElement, "BLOC");
    let localHeight = this._isAbsoluteHeight ? 1 : this.height;
    let localWidtht = this._isAbsoluteWidth ? 1 : this.width;

    this.ratios.svg = svg.math.computeDimension(parents, localHeight, localWidtht);

    this.computeRatio();
  }

  public computeRatio() { };
  public updateComponentSize(size) { };

  /***************************************************************************
  * COMPUTE SIZE
  ***************************************************************************/
  private _computeSize(dimension) {

    let localWidth = this._convertToNumber(this.width);
    let localHeight = this._convertToNumber(this.height);

    if (this._isAbsoluteWidth) {
      this.size.svg.width = localWidth;
    } else {
      this.size.svg.width = dimension.width * this.ratios.svg.width;
    }

    if (this._isAbsoluteHeight) {
      this.size.svg.height = localHeight;
    } else {
      this.size.svg.height = dimension.height * this.ratios.svg.height;
    }


    this.computeSize(this.size.svg);
  }
  public computeSize(svgContainerSize) { };

  /***************************************************************************
  * RENDER
  ***************************************************************************/
  private _renderLayout() {
    let container = this.chartContainer.nativeElement;
    this.compos.svg = svg.builder.svgContainer(container, this.size.height, this.size.width);
    this.compos.child = this.compos.svg.append("g").attr("class", "child");

    this.renderLayout(this.compos.child);
  }
  public renderLayout(svg: any) {
    this.renderYaxis(svg);
    this.renderXaxis(svg);

    this.renderDataPoints(svg);

  }
  public renderYaxis(svg: any) {
    svg.append("line").attr("x1", this.position.axisY.x1).attr("y1", this.position.axisY.y1)
      .attr("x2", this.position.axisY.x2).attr("y2", this.position.axisY.y2).attr("stroke-width", 1).attr("stroke", "black");
    for (let i = 0; i < this.position.axisYPoints.length; i ++) {
      svg.append("circle").attr("cx", this.position.axisYPoints[i].x)
        .attr("cy",  this.position.axisYPoints[i].y)
        .attr("r", 2);
    }



  }

  public renderXaxis(svg: any) {
    svg.append("line").attr("x1", 10).attr("y1", 300).attr("x2", 600).attr("y2", 300).attr("stroke-width", 1).attr("stroke", "black");
    for (let i = 0;   i < this.position.axisXPoints.length; i++) {
      svg.append("circle").attr("cx", this.position.axisXPoints[i].x)
        .attr("cy", this.position.axisXPoints[i].y)
        .attr("r", 2);
    }
  }

  public renderDataPoints(svg: any) {
    //let dragHandler = d3.drag().on("drag",function(){
      
   // })
    let svgDataPoints = svg.append("g").attr("class","dataPoints ");
    for(let points of this.position.dataPoints){
      let dataPoint = svgDataPoints.append("circle")
                                    .attr("cx",points.x).attr("cy",points.y)
                                    .attr("r",5)
                                    .call(d3.drag().on("drag",function(){   }));
    }
  }



  /***************************************************************************
  * PROCESS REFRESH
  ***************************************************************************/
  private _processRefresh() {
    this.compos.svg.attr('height', this.size.svg.height);
    this.compos.svg.attr('width', this.size.svg.width);

    this.processRefresh();
  }

  public processRefresh() { };
  /***************************************************************************
  * COMPUTE POSITION
  ***************************************************************************/
  public _computePosition() {
    this.computeAxisXPosition();
    this.computeAxisYPosition();
    this.computeaxisXPointsPosition();
    this.computeaxisYPointsPosition();

    this.computeDataPointsPosition();
  }
  public computeAxisXPosition() {
    this.position.axisX.x1 = 10;
    this.position.axisX.y1 = 300;
    this.position.axisX.x2 = 600;
    this.position.axisX.y2 = this.position.axisX.y1;
  }

  public computeAxisYPosition() {
    this.position.axisY.x1 = this.position.axisX.x1;
    this.position.axisY.y1 = 10;
    this.position.axisY.x2 = this.position.axisX.x1;
    this.position.axisY.y2 = this.position.axisX.y1;
  }
  public computeaxisXPointsPosition() {

    let leftMargin = this.position.axisX.x1;
    let upMargin = this.position.axisX.y1;
    let spacing = (this.position.axisX.x2 - this.position.axisX.x1) / 24
    for (let i = 0; i < 24; i++) {
      this.position.axisXPoints.push({
        x: leftMargin + i * spacing,
        y: upMargin
      });
    }
  }
  public computeaxisYPointsPosition() {
    let leftMargin = this.position.axisX.x1;
    let upMargin = this.position.axisY.y1;
    let spacing = (this.position.axisY.y2 - this.position.axisY.y1) / 13
    for (let i = 13; i > 0; i--) {
      this.position.axisYPoints.push({
        x: leftMargin,
        y: upMargin + i * spacing
      });
    }
  }

  //methode juste pour tester un premier affichag et manip de point, faudra extraire la logique pour pouvoir rajouter des points dynamiquement 
  public computeDataPointsPosition(){
    let self = this;
    this.position.axisXPoints.forEach(element => {
      self.position.dataPoints.push({x:element.x,
                                        y:self.position.axisYPoints[2].y})
    });
  }

  /***************************************************************************
  * COMPUTE
  ***************************************************************************/
  public onData(event: any) { }

  private _handlerOnData(event) {
    this.onData(event);
    this.computeSize(this.size.svg)
    this._processRefresh();
  }


  /***************************************************************************
  * TOOLS
  ***************************************************************************/
  private _convertToNumber(value: any): number {
    return isNaN(value) ? Number(value.replace("px", "")) : value;
  }


}

