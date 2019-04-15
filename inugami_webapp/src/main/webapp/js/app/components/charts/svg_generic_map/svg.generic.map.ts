import {Component,
  Input,
  Output,
  EventEmitter,
  forwardRef,
  AfterViewInit,
  ElementRef}                                         from '@angular/core';

import * as d3 from 'd3';
import {NG_VALUE_ACCESSOR,ControlValueAccessor}       from '@angular/forms';
import {SvgComponent}                                 from './../svg_component/svg.component';
import {HttpServices}                                 from './../../../services/http/http.services';
import {Http}                                         from '@angular/http';
import {SvgGenericMapEventIncomming}                  from './svg.generic.map.event.incomming';
import {SvgGenericMapMouseEvent}                      from './svg.generic.map.mouse.event';


export const SvgGenericMap_ACCESSOR: any = {
provide: NG_VALUE_ACCESSOR,
useExisting: forwardRef(() => SvgGenericMap),
multi: true
};


@Component({
selector      : 'i-svg-generic-map',
templateUrl   : 'js/app/components/charts/svg_component/svg.component.html',
directives    : [],
providers     : [SvgGenericMap_ACCESSOR]
})
export class SvgGenericMap extends SvgComponent implements ControlValueAccessor,AfterViewInit {

  /*****************************************************************************
  * ATTRIBUTES
  *****************************************************************************/
  @Input() src                           : string;
  @Input() cleanSvgStyle                 : boolean = true;
  @Input() handler                       : any = null;
  @Input() bddSelector                   : any = "g.bdd";
  @Input() connectorsSelector            : any = "path.path-connector";

  public  componentBaseStyleClass        : string = "i-svg-generic-map";
  private innerValue                     : any    = null;
  @Output() click                        : EventEmitter<any> = new EventEmitter();
  @Output() onHover                      : EventEmitter<any> = new EventEmitter();
  @Output() onFocusOut                   : EventEmitter<any> = new EventEmitter();
 
  /*****************************************************************************
  * INIT
  *****************************************************************************/
  constructor(private el: ElementRef,private http  : Http,private httpSerivce    : HttpServices){
    super(el);
  }

  afterContentInit(){
    let self = this;
    let url = [CONTEXT_PATH, this.src].join('');

    this.http.get(url).toPromise().then(data =>{
      self.initSvgMap(data);
    });
  }

  private initSvgMap(data){
    if(isNotNull(data._body)){
      let viewBox = this.extractViewBox(data._body); 
      let content = this.cleanContent(data._body);
 
      this.compos.svg.html(content);
      if(isNotNull(viewBox)){
        this.compos.svg.attr('viewBox',viewBox);
      }

      let self = this;
      
      this.compos
          .svg
          .selectAll(this.bddSelector)
          .on("click"     , function(){self.onClick(this,"bdd")})
          .on("mouseover" , function(){self.onMouseover(this,"bdd")})
          .on("mouseout" , function(){self.onMouseout(this,"bdd")});

      this.compos
          .svg
          .selectAll(this.connectorsSelector)
          .on("click"     , function(){self.onClick(this,"connector")})
          .on("mouseover" , function(){self.onMouseover(this,"connector")})
          .on("mouseout" , function(){self.onMouseout(this,"connector")});
          
    }
  }


  /*****************************************************************************
  * EVENTS
  *****************************************************************************/
  private onClick(node,type){
    this.click.emit(new SvgGenericMapMouseEvent(this.compos.svg,node,type));
  }
  private onMouseover(node,type){
    node.attributes.class.value = 'on-over '+node.attributes.class.value;
    this.onHover.emit(new SvgGenericMapMouseEvent(this.compos.svg,node,type));
  }
  
  private onMouseout(node,type){
    node.attributes.class.value = node.attributes.class.value.split('on-over ').join('');
    this.onFocusOut.emit(new SvgGenericMapMouseEvent(this.compos.svg,node,type));
  }
  /*****************************************************************************
  * TOOLS
  *****************************************************************************/
  private extractViewBox(content){
    let result = null;
    if( isNotNull(content)){
      let regex = /(?:viewBox\s*=\s*")([^"]+)(?:")/g;
      let matcher =  content.match(regex);
      if(isNotNull(matcher)){
        result = matcher[0].split('=')[1].split('"').join('');
      }
    }
    return result;
  }

  private cleanContent(content){
    let result = "";
    
      if( isNotNull(content)){
        result = content.replace(new RegExp('<[?]xml[^>]+>', 'gm'),'');
        result = result.replace(new RegExp('<svg[^>]+>', 'gm'),'');
        result = result.replace(new RegExp('</svg[^>]+>', 'gm'),'');
        if(this.cleanSvgStyle){
          result = result.replace(new RegExp('style\\s*=\\s*"[^"]+"', 'gm'),'');
        }
      }

    return result;
  }
  /*****************************************************************************
  * IMPLEMENTS ControlValueAccessor
  *****************************************************************************/
  public onData(event:any){
    if(isNotNull(this.handler) && "function" ==(typeof this.handler)){
      this.handler(new SvgGenericMapEventIncomming(
                          this.compos.svg,
                          event.detail,
                          this.innerValue
      ));
    }

    this.innerValue = JSON.parse(JSON.stringify(event.detail));
  }
}
