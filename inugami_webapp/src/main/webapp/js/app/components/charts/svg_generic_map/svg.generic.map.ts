import {Component,
  Inject,
  OnInit,
  Input,
  Output,
  forwardRef,
  AfterViewInit,
  ViewChild,
  ElementRef}                                         from '@angular/core';

import * as d3 from 'd3';
import {NG_VALUE_ACCESSOR,ControlValueAccessor}       from '@angular/forms';
import {SvgComponent}                                 from './../svg_component/svg.component';
import {HttpServices}                                 from './../../../services/http/http.services';
import {Http}                                         from '@angular/http';
import {SvgGenericMapEventIncomming}                  from './sgv.generic.map.event.incomming';

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

  public  componentBaseStyleClass        : string = "i-svg-generic-map";
  private innerValue                     : any    = null;
  

 
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
    }
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
