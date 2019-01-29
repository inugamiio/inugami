import {Component,OnInit,Input}                      from '@angular/core';


@Component({
  selector      : 'msg',
  template: `{{innerValue}}`,
  directives    : [],
  providers     : []
})
export class Msg implements  OnInit{

  /*****************************************************************************
  * ATTRIBUTES
  *****************************************************************************/
  private innerValue    : string;
  @Input()  key         : string;
  @Input()  values      : any[];


  /*****************************************************************************
  * ngAfterViewInit
  *****************************************************************************/
  ngOnInit(){
      this.innerValue = org.inugami.formatters.message(this.key, this.values);
  }

}
