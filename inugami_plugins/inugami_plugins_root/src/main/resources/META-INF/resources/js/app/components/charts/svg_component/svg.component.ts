import {
  Component,
  Input,
  ViewChild,
  ViewEncapsulation,
  ElementRef,
  AfterViewInit
}                                from '@angular/core';

  
import * as d3                   from 'd3';

@Component({
  templateUrl   : 'js/app/components/charts/svg_component/svg.component.html',
  directives    : [],
  encapsulation : ViewEncapsulation.None
})
export abstract class SvgComponent implements AfterViewInit{
  /***************************************************************************
  * ATTRIBUTES
  ***************************************************************************/
  @ViewChild('container') chartContainer : ElementRef;

  componentBaseStyleClass                : string;
  @Input() styleClass                    : string;


  @Input() width                         : string;
  @Input() height                        : string;

  @Input() pluginName                    : string;
  @Input() event                         : string;

  private componentName                  : string  = "compo";
  private ratios                         : any;
  private size                           : any;
  private compos                         : any;

  private _isAbsoluteWidth               : boolean = false;
  private _isAbsoluteHeight              : boolean = false;


  /***************************************************************************
  * CONSTRUCTOR
  ***************************************************************************/
  constructor(private el: ElementRef){
    let self = this;
    
    this.ratios={
        svg:{
          height : 0,
          width  : 0
        },
        margin : {
          top : 0, bottom:0, left:0, right:0
        }  
    };

    this.size = {
        svg : {
          height : null,
          width  : null
        },
        margin : {
          top : 0, bottom:0, left:0, right:0
        }  
    };

    this.compos = {
        svg   : null,
        child : null
    };
  }

  // -------------------------------------------------------------------------
  // @PostConstruct
  // -------------------------------------------------------------------------
  ngAfterContentInit(){
    this.afterContentInit();
    let self = this;
    if(isNull(this.width)  || ""==this.width){
      this.width = 1;
    }
    if(isNull(this.height) || ""==this.height){
      this.height = 1;
    }

    
    if(isNaN(this.width)){
      this._isAbsoluteWidth  = this.width.endsWith("px");
    }
    
    if(isNaN(this.height)){
      this._isAbsoluteHeight = this.height.endsWith("px");
    }
    

    if(isNotNull(this.event)){
      io.inugami.events.addEventListenerByPlugin(this.pluginName,this.event, function(event) {
        self._handlerOnData(event);
      });
    }

    io.inugami.events.addEventListener(io.inugami.events.type.RESIZE, function(data){
      self._computeSize(data.detail);
      self.updateComponentSize(self.size.svg);
      self._processRefresh();
    });


    this.postConstruct();
    this._computeRatio();
    this._computeSize(io.inugami.values.screen);
    this._renderLayout();
    this.updateComponentSize(this.size.svg);
    this._processRefresh();
  }

  public buildEventName(){
    return io.inugami.events.buildEventFullName(this.pluginName,this.event);
  }


  public afterContentInit(){};
  public postConstruct(){};


  /***************************************************************************
  * COMPUTE RATIO
  ***************************************************************************/
  private _computeRatio(){
    let parents      = svg.components.searchParent(this.el.nativeElement.parentElement, "BLOC");
    let localHeight  = this._isAbsoluteHeight?1:this.height;
    let localWidtht  = this._isAbsoluteWidth ?1:this.width;

    this.ratios.svg  = svg.math.computeDimension(parents,localHeight,localWidtht);

    this.computeRatio();
  }

  public computeRatio(){};
  public updateComponentSize(size){};

  /***************************************************************************
  * COMPUTE SIZE
  ***************************************************************************/
  private _computeSize(dimension){

    let localWidth =this._convertToNumber(this.width);
    let localHeight =this._convertToNumber(this.height);

    if(this._isAbsoluteWidth){
      this.size.svg.width = localWidth;
    }else{
      this.size.svg.width = dimension.width * this.ratios.svg.width;
    }

    if(this._isAbsoluteHeight){
      this.size.svg.height = localHeight;
    }else{
      this.size.svg.height = dimension.height * this.ratios.svg.height;
    }


    this.computeSize(this.size.svg);
  }
  public computeSize(svgContainerSize){};

  /***************************************************************************
  * RENDER
  ***************************************************************************/
  private _renderLayout(){
    let container               = this.chartContainer.nativeElement;
    this.compos.svg             = svg.builder.svgContainer(container,this.size.height,this.size.width);
    this.compos.child           = this.compos.svg.append("g").attr("class", "child");

    this.renderLayout(this.compos.child);
  }
  public renderLayout(svg:any){};

  /***************************************************************************
  * PROCESS REFRESH
  ***************************************************************************/
  private _processRefresh(){
    this.compos.svg.attr('height' , this.size.svg.height);
    this.compos.svg.attr('width'  , this.size.svg.width);
    
    this.processRefresh();
  }

  public processRefresh(){};


  /***************************************************************************
  * COMPUTE
  ***************************************************************************/
  public onData(event:any){}

  private _handlerOnData(event){
      this.onData(event);
      this.computeSize(this.size.svg)
      this._processRefresh();
  }


  /***************************************************************************
  * TOOLS
  ***************************************************************************/
  private _convertToNumber(value:any):number {
    return isNaN(value)?Number(value.replace("px","")) : value;
  }


}