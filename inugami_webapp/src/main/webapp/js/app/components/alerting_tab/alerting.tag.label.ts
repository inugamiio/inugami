import {AlertingTag} from './../../models/alerting.message';

export class AlertingTagLabel {
    /*****************************************************************************
    * ATTRIBUTES
    *****************************************************************************/
    private alertingTag             : any;
    private svgParent               : any;
    private labelPresent            : boolean;
    private iconPresent             : boolean;
    private defName                 : string;
    private gloableStyleClass       : string;

    private innerComponents         : any = {
        container  : null,
        background : {group:null, left:null, middle:null, right:null},
        content    : {group:null, icon :null, label:null}
    };

    private size                    : any = {
        icon:null, label:null, labelGroup: null
    }

    private defaultIcon : any = {
        fatal    : '&#xf06d;',
        error    : '&#xf057;',
        warn     : '&#xf06a;',
        info     : '&#xf05a;',
        debug    : '&#xf188;',
        trace    : '&#xf21b;',
        question : '&#xf128;'
    }
    /*****************************************************************************
    * CONSTRUCTOR
    *****************************************************************************/
    constructor(alertingTag:AlertingTag, svgParent:any){
        this.alertingTag=alertingTag;
        this.svgParent=svgParent;
        if(isNotNull(this.alertingTag) && isNotNull(this.svgParent)){
            this.rendering();
            this.updateData();
        }
    }


    /*****************************************************************************
    * RENDERING
    *****************************************************************************/
    private rendering(){
        let baseCss = 'alerting-tag-label';
        this.innerComponents.container = this.svgParent.append("g");
        let styleClass = [baseCss];
     
        if(isNotNull(this.alertingTag.styleClass)){
            styleClass.push(this.alertingTag.styleClass);
            this.defName = this.alertingTag.styleClass.split(' ').join('-');
        }
        this.gloableStyleClass = styleClass.join(' ');
        let style = isNull(this.alertingTag.style)?"":this.alertingTag.style;
        this.innerComponents.container.attr("class",this.gloableStyleClass)
                                      .attr("style",style);
       
        if(isNotNull(this.alertingTag.offsetX)){
            svg.transform.translateX(this.innerComponents.container,this.alertingTag.offsetX);
        }
        if(isNotNull(this.alertingTag.offsetY)){
            svg.transform.translateY(this.innerComponents.container,this.alertingTag.offsetY);
        }
     
        this.innerComponents.background.group  = this.innerComponents.container.append("g").attr("class", baseCss+"-background");
        this.innerComponents.background.middle = this.innerComponents.background.group.append("rect").attr("class", baseCss+"-background-middle");
        this.innerComponents.background.left   = this.innerComponents.background.group.append("circle").attr("class", baseCss+"-background-left");
        this.innerComponents.background.right  = this.innerComponents.background.group.append("circle").attr("class", baseCss+"-background-right");
        
        this.innerComponents.content.group     = this.innerComponents.container.append("g").attr("class", baseCss+"-content");
        this.innerComponents.content.icon      = this.innerComponents.content.group.append("svg:text")
                                                                                   .attr("class", baseCss+"-content-icon")
                                                                                   .attr("style","font-size:85%");
        this.innerComponents.content.label     = this.innerComponents.content.group.append("svg:text").attr("class", baseCss+"-content-label");

        
    }

    /*****************************************************************************
    * UPDATE
    *****************************************************************************/
    private updateData(){
        this.iconPresent = isNotNull(this.alertingTag.icon);
        if(this.iconPresent){
            let icon = this.resolveIcon(this.alertingTag.icon);
            this.innerComponents.content.icon.html(icon);
            this.innerComponents.content.icon.attr("class", "alerting-tag-label-content-icon ");
            this.size.icon=svg.data.size(this.innerComponents.content.icon);
            svg.transform.translateX(this.innerComponents.content.label,this.size.icon.width);
        }

        this.labelPresent = isNotNull(this.alertingTag.label);
        if(this.labelPresent){
            this.innerComponents.content.label.text(this.alertingTag.label);
            this.size.label=svg.data.size(this.innerComponents.content.label);
        }else{
            this.innerComponents.content.icon.attr("style","font-size:120%");
            this.innerComponents.container.attr("class",this.gloableStyleClass+" only-icon");
        }
        this.size.labelGroup = svg.data.size(this.innerComponents.content.group);
        this.updateBackground();
    }


    public updateBackground(){
        let height = this.computeHeight();
        let halfH = height/2;

        if(this.labelPresent){
            this.innerComponents.background.left.attr("r",halfH);
            this.innerComponents.background.right.attr("r",halfH);
            this.innerComponents.background.middle.attr("height",height)
                                           .attr("width",this.size.labelGroup.width);
        }else if(this.iconPresent){
            let maxSize = this.size.icon.height >this.size.icon.width ? this.size.icon.height :this.size.icon.width ;
            height = maxSize * 1.5;
            halfH  = height/2;
            this.innerComponents.background.left.attr("r",halfH);
        }

        let leftPos   = svg.data.size(this.innerComponents.background.left);
        let rightPos  = svg.data.size(this.innerComponents.background.right);
        let middlePos = svg.data.size(this.innerComponents.background.middle);

        if(this.labelPresent){
            let posYBackL = -(leftPos.y-this.size.labelGroup.y);
            let posYBackR = -(rightPos.y-this.size.labelGroup.y);
            let posYBackM = -(middlePos.y-this.size.labelGroup.y);

            svg.transform.translateY(this.innerComponents.background.left,isNaN(posYBackL)?0:posYBackL);
            svg.transform.translate(this.innerComponents.background.right,this.size.labelGroup.width,isNaN(posYBackR)?0:posYBackR);
            svg.transform.translate(this.innerComponents.background.middle,0,isNaN(posYBackM)?0:posYBackM);
        }
        else{
            let posX = (this.size.icon.x-leftPos.x)-(this.size.icon.width/4);
            let posY = -(leftPos.y-this.size.icon.y)-(this.size.icon.height/4);
            svg.transform.translate(this.innerComponents.background.left,
                                    isNaN(posX)?0:posX,
                                    isNaN(posY)?0:posY
                                   );
        }
    }


    /*****************************************************************************
    * COMPUTE
    *****************************************************************************/
    private computeHeight(){
        let result = 0;
        if(isNotNull(this.size.icon)){
            result = this.size.icon.height;
        }
        if(isNotNull(this.size.label) && this.size.label.height>result){
            result = this.size.label.height;
        }
        return result * 1.1;
    }

    /*****************************************************************************
    * DELETE
    *****************************************************************************/
    public delete(){
        this.innerComponents.content.group.remove(this.innerComponents.content.icon);
        this.innerComponents.content.group.remove(this.innerComponents.content.label);

        this.innerComponents.background.group.remove(this.innerComponents.background.middle);
        this.innerComponents.background.group.remove(this.innerComponents.background.left);
        this.innerComponents.background.group.remove(this.innerComponents.background.right);

        this.innerComponents.container.remove(this.innerComponents.content.group);
        this.innerComponents.container.remove(this.innerComponents.background.group);

        this.innerComponents = undefined;
    }
    /*****************************************************************************
    * TOOLS
    *****************************************************************************/
    private resolveIcon(iconName:string){
        let result = this.defaultIcon[iconName];

        if(result==null){
            result = svg.icons.search(iconName);
        }

        return isNull(result)?this.defaultIcon.question:result;
    }
    /*****************************************************************************
    * GETTER
    *****************************************************************************/
   public getSize(){
       let result = svg.data.size(this.innerComponents.container);
       if(this.iconPresent && !this.labelPresent){
            result.width = result.width*1.1;
       }
       return result;
   }

   public getComponent(){
       return this.innerComponents.container;
   }

   public getDefName(){
       return this.defName;
   }

   public buildDefs(node){
    if(isNotNull(this.defName)){
        svg.builder.linearGradient(node,this.defName);
    }
   }
}
