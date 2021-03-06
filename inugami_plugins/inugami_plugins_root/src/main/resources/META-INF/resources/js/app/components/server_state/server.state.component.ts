// ANGULAR CORE MODULES --------------------------------------------------------
import {Component, Input}     from '@angular/core';

@Component({
    selector: 'server-state',
    template: `
        <div [class]="styleClass" [ngClass]="'server-state'" [title]="getTitleInfo()">
            <div [class]="getServerState()" ></div>
        </div>
    `
})
export class ServerStateComponent{
    /**************************************************************************
    * ATTRIBUTES
    **************************************************************************/
    private serverUp                    : boolean;
    
    @Input() styleClass                 : string;

    /**************************************************************************
    * CONSTRUCTORS
    **************************************************************************/
    constructor() {
        let self = this;
        io.inugami.events.addEventListener(io.inugami.sse.events.ERROR, function(event){
            self.serverUp = false;
          });
      
        io.inugami.events.addEventListener(io.inugami.sse.events.OPEN, function(event){
            self.serverUp = true;
        });
        io.inugami.events.addEventListener(io.inugami.sse.events.OPEN_OR_ALREADY_OPEN, function(event){
            self.serverUp = true;
        });
    }

    private getServerState(){
        return this.serverUp?'button-state-up grass-bg' : 'button-state-down grapefruit-bg';
    }
    private getTitleInfo(){
        let lastUpdate = null;
        let sseSocket = null;
        if(isNotNull(io.inugami.sse._inner_data.eventSources)){
            switch(io.inugami.sse._inner_data.eventSources.readyState) {
                case EventSource.CONNECTING:
                    sseSocket = "CONNECTING";
                    break;
                case EventSource.OPEN:
                    sseSocket = "OPEN";
                    break;

                default:
                    sseSocket = "CLOSED";
            }
        }
        if(io.inugami.sse.lastIncommingData>0){
            let time = io.inugami.sse.lastIncommingData/1000
            lastUpdate= io.inugami.formatters.timestampToTimeFormat(time.toFixed(0), "YYYY-MM-DD HH:mm:ss");
        }else{
            lastUpdate= "undefine";
        }

        
        return ["sseSocket state :",sseSocket, " | last incomming :",lastUpdate].join("");
    }

    
}