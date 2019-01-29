import {Component,
        Input,
        forwardRef,
        AfterViewInit}                                      from '@angular/core';

import {NG_VALUE_ACCESSOR,ControlValueAccessor}             from '@angular/forms';

export const TIME_HANDLER_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() =>TimeHandler),
    multi: true
};


@Component({
  selector      : 'time-handler',
    template      : `
      <div [ngClass]="'time-handler'"
           [class]="styleClass" >

          <div class="process">
            <div class="cursor" [style.width]="progress+'%'"></div>
          </div>
          <div *ngIf="label" class="time-label">{{label}}</div>
          <div class="last-update">
                <a href="#" [title]="timeReceivedLabel" onclick="return false;"><span class="last-update-value">Dernière mise-à-jour :{{timeLabel}}</span></a>
          </div>
      </div>
  `,
  directives    : [],
  providers     : [TIME_HANDLER_ACCESSOR]
})
export class TimeHandler implements ControlValueAccessor,AfterViewInit {


    /***************************************************************************
    * ATTRIBUTES
    ***************************************************************************/
    @Input() time                       : number;

    @Input() styleClass                 : string;
    @Input() pluginName                 : string;
    @Input() events                     : string;
    @Input() label                      : string;

    delais                              : number = 250;
    timeLeft                            : number;
    progress                            : number;
    timeLabel                           : string;
    timeReceivedLabel                   : string;

    /*****************************************************************************
    * INIT
    *****************************************************************************/
    ngAfterContentInit() {
        this.timeMs = this.time*1000;
        this.timeLeft = this.timeMs;
        let self= this;

        setInterval(function(){self.handlerDelais();}, this.delais) ;


    

        if(isNotNull(this.events)){
           
            let allEvents = this.events.split(";");
            for(let i =0; i<allEvents.length;i++){
                let currentEvent = allEvents[i].trim();
                org.inugami.events.addEventListenerByPlugin(this.pluginName,currentEvent, function(event) {
                   self.handlerEventDone(event.detail);
                });
            }


        }
    }

    initTimeLeft(){
        this.timeLeft = this.timeMs;
        this.progress = 0;
    }

    handlerDelais(){
        this.timeLeft = this.timeLeft-this.delais;
        if(this.timeLeft<=0){
            this.progress =100;
        }else{
            this.progress = ((this.timeMs-this.timeLeft)/this.timeMs)*100;
        }
    }

    handlerEventDone(event){
        this.initTimeLeft();
        
        let now = Math.round(Date.now()/1000);
        let time = null;
        if(isNotNull(event) && isNotNull(event.time)){
            time = event.time/1000;
        }
        
        if(isNull(time)){
            time = now;
        }

        this.timeLabel= org.inugami.formatters.timestampToTimeFormat(time,"HH:mm");
        
        let timeReceived= org.inugami.formatters.timestampToTimeFormat(now,"HH:mm");

        this.timeReceivedLabel = ["dernier événement reçu à : ",timeReceived].join('');
    }
    /***************************************************************************
    * IMPLEMENTS ControlValueAccessor
    ***************************************************************************/
    writeValue(value: any) {
      if (value !== this.innerValue) {
        this.innerValue = value;
        this.updateValue();
      }
    }

    registerOnChange(fn: any) {
      this.onChangeCallback = fn;
    }
    registerOnTouched(fn: any) {
      this.onTouchedCallback = fn;
    }

}
