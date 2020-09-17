import {Component,Inject,OnInit,Input,Output,ElementRef}    from '@angular/core';



@Component({
  selector      : 'image',
  template: `
<img [src]="srcImage" 
     [class]="styleClass"
     [ngClass]="'image'"
     [style.width.px]="innerWidth"
     [style.height.px]="innerHeight"
     />
  `,
  directives    : [],
})
export class Image implements AfterViewInit{

  /*****************************************************************************
  * ATTRIBUTES
  *****************************************************************************/
  @Input() styleClass                 : string;
  @Input() alt                        : string;
  @Input() title                      : string;
  @Input() src                        : string;
  
  @Input() isContextImage             : boolean = true;
  @Input() height                     : number  = 1;
  @Input() width                      : number  = 1;
  @Input() fontRatioByWidth           : boolean = false;
  @Input() keepRatioHeight            : boolean = false;
  @Input() keepRatioWidth             : boolean = false;

  private srcImage                    : string;
  private parents                     : any[]   = [];
  private ratioHeight                 : number;
  private ratioWidth                  : number;
  private innerRatioFont              : number;
  
  private innerHeight                 : number;
  private innerWidth                  : number;

  /*****************************************************************************
  * CONSTRUCTOR
  *****************************************************************************/
  constructor(private el: ElementRef){
     let self = this;
     io.inugami.events.addEventListener(io.inugami.events.type.RESIZE, function(data){
       self._updateSize(data.detail);
     });

     io.inugami.events.addEventListener(io.inugami.events.type.UPDATE_CONFIGURATION, function(data){
      self._initSize();
    });
  }

  ngAfterContentInit() {
    
    if(this.isContextImage){
        this.srcImage = [CONTEXT_PATH,this.src].join('');
    }else{
        this.srcImage = this.src;
    }

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
    this._updateSize(io.inugami.values.screen);
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

      if(this.keepRatioWidth){
        this.innerHeight = this.innerWidth;
      }
      if(this.keepRatioHeight){
        this.innerWidth = this.innerHeight;
      }
    }
}
