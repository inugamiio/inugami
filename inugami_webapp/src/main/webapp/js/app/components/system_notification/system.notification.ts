import {Component,Input,AfterViewInit,ElementRef,ViewChild}  from '@angular/core';


@Component({
    selector      : 'system-notification',
    template      : `
<div [ngClass]="'system-notification'
               "[class]="styleClass" >
    <div [ngClass]="'system-notification-container'" [class]="show">
        <ul>
            <li *ngFor="let message of messages; let i = index" [style.width.px]="message.size" [style.left.px]="message.position">
                <div [ngClass]="'system-notification-message '" [class]="message.level">
                    <div [ngClass]="'icon-level ' "[class]="message.level"></div>
                    <div class="message-label"  >{{message.label}}</div>
                </div>
            </li>
            <div class="clear"></div>
        </ul>
        <div class="system-notification-footer"></div>
        <div class="clear"></div>
    </div>
    <div class="hidden-node" #container></div>
</div>
    `,
    directives    : [],
    providers     : []
})
export class SystemNotification implements AfterViewInit {

    /*****************************************************************************
    * ATTRIBUTES
    *****************************************************************************/
    @ViewChild('container') chartContainer : ElementRef;
    @Input() styleClass                    : string;

    private canva                          : any = document.createElement("canvas");
    private messages                       : any[]  = [];
    private show                           : string = "hidden-messages";
    

    /*****************************************************************************
    * INIT
    *****************************************************************************/
    constructor(private el: ElementRef){
        let self = this;
        setInterval(function(){self.updatePosition()}, 50);
    }

    ngAfterContentInit() {
        let self = this;
        org.inugami.events.addEventListenerByPlugin(
            "globale",
            "message",
            (event)=> {
                self.onGlobaleMessage(event);
            }
        );

        org.inugami.events.addEventListenerByPlugin(
            "globale",
            "beginUpdate",
            (event)=> {
                self.addMessage(
                        {"id"    :"beginUpdate",
                         "label" :"Le système est en cours de mise à jour. Nous vous prions de nous excuser pour la gêne occasionnée."}
                );
            }
        );

        org.inugami.events.addEventListenerByPlugin(
            "globale",
            "endUpdate",
            (event)=> {
                self.removeMessage("beginUpdate");
            }
        );
    }
    

    /*****************************************************************************
    * HANDLER
    *****************************************************************************/
    private onGlobaleMessage(event){
        if(isNotNull(event)&&isNotNull(event.detail)&& isNotNull(event.detail.data)){
            this.addMessage(event.detail.data);
        }
    }

    private updatePosition(){

        for(let msg of this.messages){
            msg.position=msg.position-3;
            if(msg.position < -msg.size){
                msg.position = org.inugami.values.screen.width;
            }
        }
        
    }

    /*****************************************************************************
    * ADD/REMOVE MESSAGE
    *****************************************************************************/
    private addMessage(message){
        if(isNotNull(message) && isNotNull(message.label)&& isNotNull(message.id)){
            let self = this;
            var localMessage = {
                "id"       : message.id,
                "level"    : isNull(message.level)?"info":message.level,
                "label"    : message.label,
                "duration" : isNull(message.level)?60:message.duration,
                "created"  : Date.now(),
                "size"     : self.computeTextSize(message.label),
                "position" : org.inugami.values.screen.width

            };

            if(!org.inugami.checks.contains(message.id,
                                         this.messages,
                                         (ref,value)=>{return ref.id==value})){
                this.messages.push(localMessage);
                this.enableShowNotification();
            }
            
        }
    }

    private removeMessage(messageId){
        if(isNotNull(messageId) && this.messages.length>0){
            org.inugami.services.removeFromList(messageId,this.messages,(ref,value)=>{return ref.id==value});
        }
        this.checkIfMustClose();
    }

    /*****************************************************************************
    * DISPLAY
    *****************************************************************************/
    private computeTextSize(label):number{
        let result = this.getTextWidth(label,"bold 30px Avenir" );
        return result+40;
    }

    private getTextWidth(text, font) {
        var context = this.canva.getContext("2d");
        context.font = font;
        var metrics = context.measureText(text);
        return metrics.width;
    }

    private searchMaxLevel():string{
        let result = "";
        if(this.messages.length>0){
            for(let msg of this.messages){
                if("error"==msg.level){
                    result = msg.level;
                    break;
                }
                else if("warn"==msg.level){
                    result =msg.level;
                }else if("info"==msg.level && result!="warn"){
                    result =msg.level;
                }
            }
        }
        return result; 
    }
    private enableShowNotification(){
        let level = this.searchMaxLevel();
        this.show=["show-messages",level].join(" ");
    }
    private disableShowNotification(){
        this.show="hidden-messages";
    }

    private checkIfMustClose(){
        if(this.messages.length==0){
            this.disableShowNotification();
        }
    }
}
  