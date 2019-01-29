import {Component,Input,ElementRef}    from '@angular/core';

@Component({
  selector      : 'bloc',
  template: `
  <div [class]="styleClass"
       [ngClass]="'bloc'"
       [style.width.px]="innerWidth"
       [style.height.px]="innerHeight"
       [style.font-size.px]="fontSize">

    <ng-content></ng-content>

  </div>
    `,
  directives    : [],
})
export class Bloc implements AfterViewInit{

  /*****************************************************************************
  * ATTRIBUTES
  *****************************************************************************/
  @Input() styleClass                 : string;
  @Input() height                     : number  = 1;
  @Input() width                      : number  = 1;
  @Input() fontRatio                  : number  = null;
  @Input() fontRatioByWidth           : boolean = false;

  @Input() keepRatioHeight            : boolean  = false;
  @Input() keepRatioWidth             : boolean  = false;

  private parents                     : any[]   = [];
  private ratioHeight                 : number;
  private ratioWidth                  : number;
  private innerRatioFont              : number;
  
  private innerHeight                 : number;
  private innerWidth                  : number;
  private fontSize                    : number;

  /*****************************************************************************
  * CONSTRUCTOR
  *****************************************************************************/
  constructor(private el: ElementRef){
     let self = this;
     org.inugami.events.addEventListener(org.inugami.events.type.RESIZE, function(data){
       self._updateSize(data.detail);
     });

     org.inugami.events.addEventListener(org.inugami.events.type.UPDATE_CONFIGURATION, function(data){
      self._initSize();
    });
  }

  ngAfterContentInit() {
    if(this.keepRatioHeight && this.keepRatioWidth ){
      this.keepRatioHeight=false;
    }

    this.parents = svg.components.searchParent(this.el.nativeElement.parentElement, "BLOC");
    this._computeDimension();
    
    this._initSize();
  }

  /*****************************************************************************
  * INITIALIZE
  *****************************************************************************/
  private _initSize(){
    this._updateSize(org.inugami.values.screen);
  }
  private _computeDimension(){
       let ratio            = svg.math.computeDimension(this.parents,this.height,this.width, this.fontRatio);
       this.ratioHeight     = ratio.height;
       this.ratioWidth      = ratio.width;
       this.innerRatioFont  = ratio.font;
    }

    private _updateSize(data){
      this.innerHeight = data.height * this.ratioHeight;
      this.innerWidth  = data.width  * this.ratioWidth;

      if(isNotNull(this.fontRatio)){
        this.fontSize = svg.math.computeFontSize(this.innerRatioFont,
                                                 this.innerHeight,
                                                 this.innerWidth);
      }

      if(this.keepRatioWidth){
        this.innerHeight = this.innerWidth;
      }
      if(this.keepRatioHeight){
        this.innerWidth = this.innerHeight;
      }
    }
}
