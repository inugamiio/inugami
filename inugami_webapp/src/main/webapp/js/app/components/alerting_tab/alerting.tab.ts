import {Component,
    Inject,
    OnInit,
    Input,
    Output,
    EventEmitter,
    forwardRef,
    ViewChild,
    ViewEncapsulation,
    ElementRef}                             from '@angular/core';
import * as d3 from 'd3';
import {SvgComponent}                       from 'js/app/components/charts/svg_component/svg.component.ts';
import {AlertingMessage}                    from 'js/app/models/alerting.message.ts';
import {AlertingTagLabel}                   from 'js/app/components/alerting_tab/alerting.tag.label.ts';
import {AlertingTag}                        from 'js/app/models/alerting.tag.ts';


@Component({
selector      : 'alerting-tab',
template      : `
<div class="svg-component">
    <div [ngClass]="componentBaseStyleClass" [class]="styleClass" >
        <div  [ngClass]="'msg-level '" [class]="messageLevel">
            <div class="alerting-tab-top"></div>

            <div class="svg-container"  
                    [style.height.px]="size.svg.height" 
                    [style.width.px]="size.svg.width" 
                    #container></div>
            
            <div class="svg-container-end"></div>
        </div>
    </div>
    <div class="svg-component-end"></div>
</div>
`,
directives    : [],
providers     : [],
encapsulation : ViewEncapsulation.None
})
export class AlertingTab extends SvgComponent {
    
    /*****************************************************************************
    * ATTRIBUTES
    *****************************************************************************/
    private initialBaseStyleClass          : string = "alerting-tab";
    componentBaseStyleClass                : string;

    @Input()  fontRatio                    : number = 0.4;
    @Output() ttlDisplay                   : number = 10;

    @Output() onMessageOpen                : EventEmitter<any> = new EventEmitter();
    @Output() onMessageClose               : EventEmitter<any> = new EventEmitter();
    @Output() onOpen                       : EventEmitter<any> = new EventEmitter();
    @Output() onClose                      : EventEmitter<any> = new EventEmitter();

    private alertTitle                     : string = org.inugami.formatters.message("title.alert").toUpperCase();
    private nbMessages                     : number = 0;
    private currentMsgId                   : string = null;
    
    private ttlDisplayLeft                 : number = 10;
    private fontSize                       : number;
    private fontSizeMessage                : number;
    private messages                       : any  = {};
    private messagesGroup                  : any;
    private messageLevel                   : string;
    private defs                           : any = {};
    

    // SVG components    
    private layoutCompo : any = {
        layout              : null,
        defs                : null,
        borderTop           : null,
        msgTypeArea         : null,
        alertTitleTxt       : null,
        nbMessages          : null,
        msgTypeBackground   : null
    }


    // size
    private localSize : any = {
        svgContainerSize        : null,
        topBannerSize           : 0, borderTop        : 0, topMargin        : 0,
        marginLeft              : 0,
        msgTypeAreaWidth        : 0
    };



    /*****************************************************************************
    * POST CONSTRUCT
    *****************************************************************************/
    postConstruct(){
        let self = this;
        setInterval(function(){self.updatePosition()}, 1000);
        this.refreshComponentBaseStyleClass();

        org.inugami.events.addEventListener("alert-control", function(event){
            if(isNotNull(event.detail) && isNotNull(event.detail.data) && isNotNull(event.detail.data.data)){
                self.deleteMessages(event.detail.data.data);
            }
        });
    }
    

    /*****************************************************************************
    * HANDLER EVENT
    *****************************************************************************/
    /*@Override*/
    public onData(event:any){
        if(isNotNull(event.detail.data) && 
           isNotNull(event.detail.data.values) && 
           isNotNull(event.detail.data.values.id)){
                this.addMessage(event.detail.data.values);
        }
    }
    
    /*****************************************************************************
    * COMPUTE SIZE
    *****************************************************************************/
    /*@Override*/
    private computeSize(size){
        this.localSize.svgContainerSize=size;
        this.fontSize        = size.height   * this.fontRatio;
        this.fontSizeMessage = this.fontSize * 0.75;
        if(isNotNull(this.messagesGroup)){
            svg.builder.fontSize(this.messagesGroup,this.fontSizeMessage);
        }

        this.localSize.topBannerSize           = size.height                       * 0.002;
        this.localSize.marginLeft              = size.width                        * 0.041;
        this.localSize.marginLeftMessage       = this.localSize.marginLeft + (this.localSize.marginLeft* 0.2);
        this.localSize.msgTypeAreaWidth        = this.localSize.marginLeft         * 1.05;
        this.localSize.borderTop               = size.height                       * 0.03; 
        this.localSize.topMargin               = size.height                       * 0.5;

        
    }

    /*@Override*/
    public updateComponentSize(size){
        this.layoutCompo.borderTop.attr("height",this.localSize.borderTop)
                                  .attr("width",size.width);


        this.layoutCompo.msgTypeArea.attr("height",size.height)
                                    .attr("width",this.localSize.msgTypeAreaWidth);
        

        this.layoutCompo.msgTypeBackground.attr("cx",0)
                                          .attr("cy",(size.height/2))
                                          .attr("r",size.height*1.304);

        let middleTypeArea = this.localSize.msgTypeAreaWidth / 2;
        svg.builder.fontSize(this.layoutCompo.alertTitleTxt,this.fontSize*0.7);
        let layoutTitleSize = svg.data.size(this.layoutCompo.alertTitleTxt);
        svg.transform.translate(this.layoutCompo.alertTitleTxt,
                                (middleTypeArea-(layoutTitleSize.width/2)),
                                this.localSize.topMargin);
        
        svg.builder.fontSize(this.layoutCompo.nbMessages,this.fontSize*0.65);

    }

    private updateNbMessagesPosition(){
        let layoutTitleSize = svg.data.size(this.layoutCompo.alertTitleTxt);
        let middleTypeArea = this.localSize.msgTypeAreaWidth / 2;
        let nbMessageSize = svg.data.size(this.layoutCompo.nbMessages);

        svg.transform.translate(this.layoutCompo.nbMessages,
                                (middleTypeArea-(nbMessageSize.width/2)),
                                this.localSize.topMargin+(layoutTitleSize.height*1.05));
    }
    /*****************************************************************************
    * POSITONS & ANIMATIONS 
    *****************************************************************************/
    private positionDelta(){
        return (this.fontSizeMessage*5)-this.localSize.borderTop;
    }
    private refreshComponentBaseStyleClass(){
        if(this.nbMessages==0){
            this.componentBaseStyleClass = [this.initialBaseStyleClass, "close"].join(" ");
            let messagesKeys   = Object.keys(this.messages);
            for(let i=0;i<messagesKeys.length; i++){
            let item = this.messages[messagesKeys[i]];
            if(isNotNull(item)){
                this.deleteMessage(messagesKeys[i]);
            }
        }
            this.onClose.emit();
        }else{
            this.componentBaseStyleClass = [this.initialBaseStyleClass, "open"].join(" ");
            this.onOpen.emit();
        }
    }
    private initMessagePosition(id:string){
        let compo = this.messages[id];
        if(isNotNull(compo)){
            // init position is in overlay
            svg.transform.translate(compo.component, this.localSize.marginLeftMessage, -this.positionDelta());
        }
    }

    
    private animateShow(msg){
        let positionInit = this.positionDelta();
        let positionDiff= this.fontSizeMessage - positionInit;
        let self = this;
        svg.animate.tween(msg.component, 0, 500, (time)=>{
            svg.transform.translate(msg.component, self.localSize.marginLeftMessage, positionInit+(time*positionDiff));
        },
        null,
        ()=>{this.closeAllExceptCurrent()});
        //;
    }

    private animateHide(msg){
        let positionInit = this.fontSizeMessage;
        let positionDiff= positionInit - this.positionDelta();

        let self = this;
        svg.animate.tween(msg.component, 0, 500, (time)=>{
            svg.transform.translate(msg.component, self.localSize.marginLeftMessage, positionInit+(time*positionDiff));
        });
    }

    private closeAllExceptCurrent(){
        var keys = Object.keys(this.messages);
        var overlayPosition = this.fontSizeMessage * -4;
        for(var i=0; i<keys.length; i++){
            var itemId = keys[i];
            if(itemId!=this.currentMsgId){
                svg.transform.translate(this.messages[itemId].component, this.localSize.marginLeftMessage, overlayPosition);
            }
        }
    }

    private updatePosition(){
        this.checkMessagesTtl();

        if(this.ttlDisplayLeft==0){
            this.changeMessage();
            this.ttlDisplayLeft = this.ttlDisplay;
        }else{
            this.ttlDisplayLeft --;
        }

        this.refreshComponentBaseStyleClass();
    }

    private changeMessage(){
        this.resolverCursor=0;
        let nextMsg = this.grabNextMsg();
      
        if(isNotNull(nextMsg) && nextMsg.data.id != this.currentMsgId){
            this.hideMessage(this.currentMsgId);
            this.showMessage(nextMsg);
        }
        else if(isNotNull(nextMsg) && nextMsg.data.id ==this.currentMsgId && this.currentMsgNotOpen(nextMsg)){
            this.showMessage(nextMsg);
        }

        else if(isNull(nextMsg)){
            this.hideMessage(this.currentMsgId);
        }
    }


    private currentMsgNotOpen(msg){
        let nodeInfos = svg.transform.extractTranformInformation(msg.component);
        return nodeInfos.y<0;
    }


    /*****************************************************************************
    * HIDE / SHOW 
    *****************************************************************************/
    private hideMessage(id:string){
        this.messageLevel="";
        if(id == this.currentMsgId){
            this.currentMsgId=null;
        }
        if(isNotNull(id)){
            
            let msg = this.messages[id];
            if(isNotNull(msg) ){
                this.animateHide(msg);
                if(msg.timeLeft<=0){
                    this.deleteMessage(id);
                }
            }
            if(isNotNull(this.onMessageClose)){
                this.onMessageClose.emit(msg);
            }
        }
    }

    private showMessage(msg:any){
        this.currentMsgId  = msg.data.id;
        this.messagesGroup.attr("data-current-msg-id",msg.data.id);
        this.buildMessageLevel(msg);

        this.updateDisplayNbMessage();
        if(isNotNull(this.onMessageOpen)){
            this.onMessageOpen.emit(msg);
        }
        this.animateShow(msg);
    }

    private buildMessageLevel(msg){
        let styles = [];
        if(isNotNull(msg.levelType)){
            styles.push(msg.levelType.toLowerCase());
        }
        if(isNotNull(msg.level)){
            styles.push(msg.level);
        }
        this.messageLevel=styles.join(' ');
    }

    private updateDisplayNbMessage(){
        let msgCursor = 0;
        let messagesKeys   = Object.keys(this.messages);
        for(let i=0;i<messagesKeys.length; i++){
            let item = this.messages[messagesKeys[i]];
            if(isNotNull(item)){
                if(item.data.id==this.currentMsgId){
                    msgCursor = msgCursor+1;
                    break;
                }else{
                    msgCursor = msgCursor+1;
                }
            }
        }
        let cursor=[];
        cursor.push(msgCursor);
        cursor.push(this.nbMessages);

        this.layoutCompo.nbMessages.text(cursor.join('/'));
        this.updateNbMessagesPosition();
    }
    /*****************************************************************************
    * RENDERING
    *****************************************************************************/
    private renderLayout(svgComponent){
        this.messagesGroup      = svgComponent.append("g").attr("class", "messages-group");
        this.layoutCompo.layout = svgComponent.append("g").attr("class", "layout-compo");
        this.layoutCompo.defs   = svgComponent.append("defs");
        this.initLayoutComponent();
    }

    private renderingMessageComponent(data:AlertingMessage){
        let created     = org.inugami.formatters.timestampToDateTime(data.created/1000);
        let result      = this.messagesGroup.append("g").attr("class", "message")
                                                        .attr("data-messageId",data.id)
                                                        .attr("data-created",created)
                                                        .attr("data-duration",data.duration)
                                                        .attr("data-level",data.level);

        let titleTxt    = this.buildTextMessage(data.label,    data.labelBuilder,   data);
        let subTitleTxt = this.buildTextMessage(data.subLabel, data.subLabelBuilder,data);
        let tagsGroup   = result.append("g").attr("class", "tags").attr("style","font-size:85%");
        let offset      = this.fontSizeMessage*1.2;
        if(isNull(subTitleTxt) || subTitleTxt.trim().length==0){
            offset      = this.fontSizeMessage*1.4;
        }

        let title = result.append("svg:text").attr("class", "title");
            title.text(titleTxt);
            svg.transform.translate(title, 0, offset);  

        let subTitle = result.append("svg:text").attr("class", "subtitle");
            subTitle.text(subTitleTxt);
            svg.transform.translate(subTitle, 0, offset + (this.fontSizeMessage*0.70));    
            subTitle.attr("style",["font-size:",60,"%"].join(""));

        let tags         = this.renderTags(data,tagsGroup);    
        
        return {
            "component" :result,
            "title"     :title,
            "subTitle"  :subTitle,
            "tagsGroup" :tagsGroup,
            "tags"      :tags
        };
    }

    private renderTags(data,tagsGroup){
        let em =this.fontSizeMessage;
        let result       = [];
        var levelType    = this.resolveLevelType(data);
        var levelTypeTag = new AlertingTag(levelType.toLowerCase(),null,"tag-level-type", "font-size:100%",null,0.2*em);
        
        result.push(new AlertingTagLabel(levelTypeTag,tagsGroup));

        if(isNotNull(data.tags)){
            for(let item of data.tags){
                result.push(new AlertingTagLabel(item,tagsGroup));
            }
        }

        let time = org.inugami.formatters.timestampToTimeFormat(data.created/1000,"HH:mm").replace(':','h');
        result.push(new AlertingTagLabel(new AlertingTag(null,time,"alert-datetime"),tagsGroup));

        var tagMargin     = this.fontSizeMessage*0.50;
        var tagCursorPosX = 0;
        for(var i=0; i<result.length; i++){
            var tag = result[i];
            if(i!=0){
                svg.transform.translateX(tag.getComponent(),tagCursorPosX);
            }
            tagCursorPosX = tagCursorPosX + tag.getSize().width + tagMargin;

            let defName = tag.getDefName();
            if(isNotNull(defName) && isNull(this.defs[defName])){
                tag.buildDefs(this.layoutCompo.defs);
                this.defs[defName]=true;
            }
            
        }
        return result;
    }

    
    private initLayoutComponent(){
        this.layoutCompo.borderTop          = this.layoutCompo.layout.append("rect").attr("class", "layout-border-top-area");
        this.layoutCompo.msgTypeArea        = this.layoutCompo.layout.append("g").attr("class", "layout-msg-type-area");
        this.layoutCompo.msgTypeBackground  = this.layoutCompo.msgTypeArea.append("circle").attr("class", "layout-msg-type-background");
        this.layoutCompo.alertTitleTxt      = this.layoutCompo.msgTypeArea.append("svg:text").attr("class", "layout-alert-title");
        this.layoutCompo.nbMessages         = this.layoutCompo.msgTypeArea.append("svg:text").attr("class", "layout-nb-messages");
        this.layoutCompo.alertTitleTxt.text(this.alertTitle);
    }
    
    
    /*****************************************************************************
    * MANAGE MESSAGES
    *****************************************************************************/
    private addMessage(data:AlertingMessage){
        let savedMessage = this.messages[data.id];

        if(isNotNull(savedMessage)){
            this.mergeMessageData(savedMessage,data);
            this.buildTtlForMessage(data.id);
        }else{
            this.appendNewMessage(data);
        }

        this.refreshComponentBaseStyleClass();
        this.updateDisplayNbMessage();
    }

    private appendNewMessage(data:AlertingMessage){
        this.nbMessages++;

        let position  = Object.keys(this.messages).length;
        let compo     = this.renderingMessageComponent(data);
        let levelType = this.resolveLevelType(data);

        this.messages[data.id] = {
            "data"     : data,
            "levelType": levelType,
            "added"    : new Date().getTime(),
            "timeLeft" : 0,
            "component": compo.component,
            "title"    : compo.title,
            "subTitle" : compo.subTitle,
            "tags"     : compo.tags,
            "tagsGroup": compo.tagsGroup
        }

        this.buildTtlForMessage(data.id);
        this.initMessagePosition(data.id);

        if(this.nbMessages==1){
            this.changeMessage();
        }
    }

    private buildTtlForMessage(id:string){
        let savedMessage = this.messages[id];
        if(isNotNull(savedMessage)){
            let timeToShow = savedMessage.data.duration * 1000;
            savedMessage.timeLeft=new Date().getTime()+timeToShow;
        }
    }

    private mergeMessageData(msg, data){
        msg.data.level=data.level;
        msg.data.label=data.label;
        msg.data.subLabel=data.subLabel;
        msg.data.duration=data.duration;
        msg.title.text(data.label);
        msg.subTitle.text(data.subLabel);

        if(msg.data.id == this.currentMsgId){
            this.buildMessageLevel(data);
        }

        for(let tag of msg.tags){
            tag.delete();
        }

        msg.tags = this.renderTags(data,msg.tagsGroup);     
    }

    private deleteMessages(messagesUid){
        for(let uid of messagesUid){
            let msg = this.messages[uid];
            if(isNotNull(msg)){
                msg.timeLeft=0;
            }
        }
        this.updateDisplayNbMessage();
    }
    private deleteMessage(id){
        let msg = this.messages[id];
        if(isNotNull(msg)){
            for(let tag of msg.tags){
                tag.delete();
            }
            msg.component.remove(msg.tagsGroup);

            if(isNotNull(msg.title)){
                msg.component.remove(msg.title);
            }
            if(isNotNull(msg.subTitle)){
                msg.component.remove(msg.subTitle);
            }
        }

        this.messages[id] = undefined;
        delete this.messages[id];
    }

    private checkMessagesTtl(){
        let currentTime = Date.now();
        let messagesKeys   = Object.keys(this.messages);
        let nbLocalMessage = 0;
        if(messagesKeys.length>0){
            for(var i=0; i<messagesKeys.length; i++){
                let msgId = messagesKeys[i];
                let msg = this.messages[msgId];
                
                if(isNotNull(msg) && this.isTtlExpire(msg,currentTime)){
                    msg.timeLeft=0;
                    if(msgId != this.currentMsgId){
                        this.deleteMessage(msgId);
                    }
                }else{
                    nbLocalMessage++;
                }
            }
        }

        if(nbLocalMessage!=this.nbMessages){
            this.nbMessages = nbLocalMessage;
        }
    }

    private isTtlExpire(msg:any, time:number){
        return time>msg.timeLeft;
    }

    private grabNextMsg(){
        return isNull(this.currentMsgId) ? this.searchFirstMsg()
                                         : this.processSearchNextFromCurrent();
    }

    private searchFirstMsg(){
        let result = null;
        let messagesKeys   = Object.keys(this.messages);
        let size = messagesKeys.length;
        for(let i=0;i<size; i++){
            let msg = this.messages[messagesKeys[i]];
            if(isNotNull(msg) && msg.timeLeft>0){
                result = msg;
                break;
            }
        }
        return result;
    }

    private processSearchNextFromCurrent(){
        let result       = null;
        let keys         = Object.keys(this.messages);
        let size         = keys.length;
        let currentIndex = -1;
        let nextIndex    = -1;

        let messagesKeys   = Object.keys(this.messages);
        let size = messagesKeys.length;
        for(let i=0;i<size; i++){
            let msg = this.messages[messagesKeys[i]];
            if(isNotNull(msg) && msg.data.id==this.currentMsgId){
                currentIndex = i;
                break;
            }
        }
        

        if(currentIndex!=-1){
            nextIndex = currentIndex+1;
            if(nextIndex>=size){
                nextIndex = 0;
            }
        }

        let cursor = nextIndex;
        for(let i=0; i<size; i++){
            cursor+=i;
            if(nextIndex>=size){
                cursor = 0;
            }

            let msg = this.messages[keys[cursor]];
            if(isNotNull(msg) && msg.timeLeft>0){
                result = msg;
                break;  
            }
        }

        return result;
    }
    /*****************************************************************************
    * TOOLS
    *****************************************************************************/
    private resolveLevelType(data:any){
        let result = "Info";
        let currentLevel = null;

        if(isNotNull(data)){
            currentLevel = data.level;
        }
        
        if(isNotNull(currentLevel)){
            let levelLowwerCase = currentLevel.toLowerCase();

            let matchers = [
                {"matcher":"fatal", "level":"Fatal"},
                {"matcher":"error", "level":"Error"},
                {"matcher":"warn" , "level":"Warn"},
                {"matcher":"info" , "level":"Info"},
                {"matcher":"debug", "level":"Debug"},
                {"matcher":"trace", "level":"Trace"},
                {"matcher":"joke" , "level":"Joke"}
            ]
    
            for(let strategy of  matchers){
                if(levelLowwerCase.indexOf(strategy.matcher)!=-1){
                    result = strategy.level;
                    break;
                }
            }
        }
        
        return result;
    }


    private buildTextMessage(label:string, labelRenderer:any, data:AlertingMessage ){
        let result = "";
        if(isNull(labelRenderer)){
            result =label; 
        }else{
            result = labelRenderer(data);
        }
        return result;
    }
}
    