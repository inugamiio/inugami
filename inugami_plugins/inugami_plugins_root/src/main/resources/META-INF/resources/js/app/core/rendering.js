// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// RENDERING API
// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
org.inugami.rendering={
    createNode : function(nodeName, styleClass, id){
        org.inugami.asserts.notNull(nodeName,"can't create node, node name is null!");

        var result = $('<'+nodeName+'></'+nodeName+'>');

        if(org.inugami.checks.notEmpty(id)){
            result.attr("id",id);
        }

        if(org.inugami.checks.notEmpty(styleClass)){
            result.attr("class",styleClass);
        }

        return result;
    },


    styleIfNotNull : function (node, styleClass){
        return org.inugami.checks.isNotNull(node)?styleClass:"";
    },

    styleIfNull : function (node, styleClass){
        return org.inugami.checks.isNull(node)?styleClass:"";
    },

    clearDiv : function(){
        var result = $("<div></div>");
            result.attr("class","clean");
        return result;
    },

    alertsStyles : function(styleClass, alerts){
        var styles = [];
        var maxAlert = null;
        if(isNotNull(alerts)){
            for(var i=0; i<alerts.length; i++){
                if(isNull(maxAlert) || alerts[i].levelNumber > maxAlert.levelNumber){
                    maxAlert = alerts[i];
                }
            }
        }
        styles.push("alerting");

        if(isNotNull(styleClass)){
            styles.push(styleClass);
        }
        if(isNull(maxAlert)){
            styles.push("no-alert");
        }else{
            styles.push(maxAlert.level);
        }
        return styles.join(" ");
    }

};

// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// RENDERING API SVG
// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
svg = {};


// *****************************************************************************
// SVG DATA
// *****************************************************************************
svg.data= {
    point : function(x,y){
        return {"x":x, "y":y};
    },
    size : function(element){
        var node = null;
        if(isNotNull(element) && isNotNull(element.node)){
            node = element.node();
        }

        return isNull(node)?null:svg.components.size(element);
    }
};

// *****************************************************************************
// SVG TRANSFORM
// *****************************************************************************
svg.transform = {
    clean  : function(node){
        node.remove("transform");
    },

    scale  : function(node, scaleX, scaleY){
        if(isNotNull(node)){
            var positions = svg.transform.extractTranformInformation(node);
                positions.scaleX=isNull(scaleX)?0:scaleX;
                positions.scaleY=isNull(scaleY)?0:scaleY;
            
                svg.transform._genericTransform(node,positions);
        }
    },
    scaleX : function(node, scaleX){
        if(isNotNull(node)){
            var positions = svg.transform.extractTranformInformation(node);
                positions.scaleX=isNull(scaleX)?0:scaleX;

            svg.transform._genericTransform(node,positions);
        }
    },

    scaleY : function(node, scaleY){
        if(isNotNull(node)){
            var positions = svg.transform.extractTranformInformation(node);
                positions.scaleY=isNull(scaleY)?0:scaleY;

            svg.transform._genericTransform(node,positions);
        }
    },

    matrix : function(node, scaleX,scaleY, posX, posY){
        if(isNotNull(node)){
            var data = [isNull(scaleX)?0:scaleX,
                        0,
                        0,
                        isNull(scaleY)?0:scaleY,
                        isNull(posX)?0:posX,
                        isNull(posY)?0:posY];
            node.attr("transform", "matrix("+data.join(',')+")");
        }
    },

    translateX : function(node, posX){
        if(isNotNull(node)){
            var positions = svg.transform.extractTranformInformation(node);
                positions.x=isNull(posX)?0:posX;

            svg.transform._genericTransform(node,positions);
        }
    },

    translateY : function(node, posY){
        if(isNotNull(node)){
            var positions = svg.transform.extractTranformInformation(node);
                positions.y=isNull(posY)?0:posY;

            svg.transform._genericTransform(node,positions);
        }
    },

    translate : function(node, posX, posY){
        if(isNotNull(node)){
            var positions = svg.transform.extractTranformInformation(node);
                positions.x=isNull(posX)?0:posX;    
                positions.y=isNull(posY)?0:posY;

            svg.transform._genericTransform(node,positions);
        }
    },

    extractTranformInformation : function (node){
        org.inugami.asserts.notNull(node);
        var result = {
            x:null,
            y:null,
            scaleX:null,
            scaleY:null
        };


        var attrTransfo = node.attr("transform");

        if(isNotNull(attrTransfo)){

            var regex = new RegExp('(?:([^(]+)[(])([^)]+)(?:[)])');
            var group = regex.exec(attrTransfo);

            switch (group[1]) {
                case "translate" : 
                    var data = group[2].split(',');
                    result.x=Number(data[0]);
                    result.y=Number(data[1]);
                    break;

                case "scale" : 
                    var data = group[2].split(',');
                    result.scaleX=Number(data[0]);
                    result.scaleY=Number(data[1]);
                    break;

                case "matrix" : 
                    var data = group[2].split(',');
                    result.scaleX=Number(data[0]);
                    result.scaleY=Number(data[3]);
                    result.x=Number(data[4]);
                    result.y=Number(data[5]);
                    break;
            }
        }


        return result;
    },
    

    _noScale : function(data){
        org.inugami.asserts.notNull(data);
        return isNull(data.scaleX) &&  isNull(data.scaleY);
    },
    _noTranslate : function(data){
        org.inugami.asserts.notNull(data);
        return isNull(data.x) &&  isNull(data.y);
    },

    _genericTransform : function(node, transfo){
        org.inugami.asserts.notNull(node);
        org.inugami.asserts.notNull(transfo);

        if(svg.transform._noScale(transfo)){
            node.attr("transform", "translate("+[isNull(transfo.x)?0:transfo.x,
                                                 isNull(transfo.y)?0:transfo.y]
                                                .join(',')+")");
        }
        else if(svg.transform._noTranslate(transfo)){
            node.attr("transform", "scale("+[isNull(transfo.scaleX)?0:transfo.scaleX,
                                             isNull(transfo.scaleY)?0:transfo.scaleY]
                                            .join(',')+")");
        }
        else{
            svg.transform.matrix(node,
                                 isNull(transfo.scaleX)?0:transfo.scaleX,
                                 isNull(transfo.scaleY)?0:transfo.scaleY,
                                 isNull(transfo.x)?0:transfo.x,
                                 isNull(transfo.y)?0:transfo.y);
        }
    }
};

// *****************************************************************************
// SVG BUILDER
// *****************************************************************************
svg.builder = {
    svgContainer : function(node,height,width,styleClass){
        var result = d3.select(node)
                       .append('svg')
                       .attr('width',  width)
                       .attr('height', height);

        if(isNotNull(styleClass)){
            result.attr('class',styleClass);
        }
        return result;
    },

    /**
     * @param x : number : X point position
     * @param y : number : Y point position
     * @return point string representation
     */
    point : function(x,y){
        return [x==null?0:x,y==null?0:y].join(',');
    },


    /**
     * @param points    : string[] : points arrays as string literal
     * @param closePath : boolean  : if true path will be close
     * @return path string representation
     */
    path : function(points,closePath){
        var result = ["m"].concat(points);
        if(closePath){
            result.push("z");
        }

        return result.join(" ");
    },

    fontSize : function(node, fontSize){
        node.attr("style",["font-size:",fontSize,"px"].join(""));
    }, 

    linearGradient : function(node, name){
        var localName = name.split('-').join('_');
        var result = node.append("linearGradient")
                         .attr("id",localName)
                         .attr("x1","0%")
                         .attr("y1","0%")
                         .attr("x2","0%")
                         .attr("y2","100%");

        result.append("stop")
              .attr("offset","0%")
              .attr("class",localName+"_begin");

        result.append("stop")
              .attr("offset","100%")
              .attr("class",localName+"_end");

        return result;              
    }
};
// *****************************************************************************
// SVG ANIMATE
// *****************************************************************************
svg.animate = {
    tween : function(node,delay,duration, handler,interpolateFunction, onDone){
         var currentNode = isNotNull(node)?node:d3.select('body');
        
         currentNode.transition()
                    .delay(delay)
                    .duration(duration)
                    .tween(org.inugami.services.generateId("svg.animate.tween"), function() {
            var customInterpolate=isNotNull(interpolateFunction);
            return function(time) {
                var t = customInterpolate?interpolateFunction(time):time;
                handler(t);
                if(time==1){
                    if(isNotNull(onDone)){
                        onDone();
                    }
                }
            };
         });
    },

    tweenValue : function(node,delay,duration, previousValue, currentValue,handler, onDone){
        var hasOnDone = isNotNull(onDone);
        if(previousValue==currentValue){
            if(hasOnDone){
                handler(currentValue);
                if(isNotNull(onDone)){
                    onDone(currentValue);
                }
            }
        }else{
            var value = previousValue;
            var diff= currentValue-previousValue;

            svg.animate.tween(node,delay,duration,function(time){
                var localValue =  value + (diff* time);
                handler(localValue);
                if(time==1){
                    if(isNotNull(onDone)){
                        onDone(localValue);
                    }
                }
            });
        }
    }
};

// *****************************************************************************
// SVG COMPONENTS
// *****************************************************************************
svg.components= {
    searchParent : function(node, nodeName){
        var result = [];
        if(node==null || "BODY" == node.nodeName){
            // done
        }
        else if(nodeName == node.nodeName ){
            result.push(node);
            result = result.concat(svg.components.searchParent(node.parentElement,nodeName));
        }else{
            result = result.concat(svg.components.searchParent(node.parentElement,nodeName));
        }

        return result;
    },

    size : function(component){
        var info = component.node().getBoundingClientRect();
        return {
            "bottom" : info.bottom,
            "height" : info.height,
            "left"   : info.left,
            "right"  : info.right,
            "top"    : info.top,
            "width"  : info.width,
            "x"      : info.x,
            "y"      : info.y
        }
    },

    translate : function(node,position){
        var result = ["translate("];
            result.push(position.x);
            result.push(',');
            result.push(position.y);
            result.push(')');
        node.attr("transform",result.join(""));
    },

    attribute : function(node, attributeName){
        var result = null;

        var attributes = svg.components.attributes(node);
        if(isNotNull(attributes)){
            var attr = attributes[attributeName];
            if(isNotNull(attr)){
                result = attr.value;
            }
        }
        return result;
    },

    attributes : function(node){
        var result = null;

        var group=null;
        if(isNotNull(node)&& isNotNull(node._groups)){
            if(Array.isArray(node._groups)){
                group = node._groups[0];
            }
        }

        if(isNotNull(group) && Array.isArray(group)){
            var firstNode = group[0];
            if(isNotNull(firstNode)){
                result =  firstNode.attributes;
            }
        }
        return result;
    }



};

// *****************************************************************************
// SVG COMPONENTS RENDER
// *****************************************************************************
svg.components.render= {
    tip : function(nativeElement,positionOffset){
        var result = null;
        if(isNotNull(nativeElement)){
            var nativeNode = d3.select(nativeElement);
            var mainContainer = nativeNode.append("div").attr("class", "tip");
            var group = mainContainer.append("div");
            var value = group.append("div").attr("class", "value");
            
            var offsetX = isNull(positionOffset)?0:positionOffset.x;
            var offsetY = isNull(positionOffset)?0:positionOffset.y;

            result ={
                'mainContainer' : mainContainer,
                'group'         : group,
                'value'         : value,
                'position'      : function(){
                    var posX = ['left:', org.inugami.values.mouse.x+offsetX, 'px;'].join("");
                    var posY = ['top:', org.inugami.values.mouse.y+offsetY, 'px;'].join("");
                    return posX+';'+posY;
                }
            }
        }

        return result;
    }
};

// *****************************************************************************
// SVG MATH
// *****************************************************************************
svg.math = {
    convertToRadian : function(angle){
        return angle * org.inugami.constants.math.TWO_PI_RATIO;
    },

    /**
     * Allow to rotate a point by radian angle
     * @param x     : number : X position
     * @param y     : number : Y position
     * @param angle : number : angle in degre
     */
    rotate : function(x,y,angle) {
        var radian = svg.math.convertToRadian(angle);
        var cos = Math.cos(radian);
        var sin = Math.sin(radian);

        return {
            "x": (x*cos - y*sin),
            "y": (x*sin + y*cos)
        };
    },

    /**
     * Allow to rotate a point by radian angle
     * @param x     : number : X position
     * @param y     : number : Y position
     * @param angle : number : angle in degre
     */
    rotateByRef : function(xRef,yRef, x,y,angle) {
        var zX = x - xRef;   
        var zY = y - yRef;
        var rotate = svg.math.rotate(zX,zY,angle);
        return {
            "x":rotate.x + xRef, 
            "y":rotate.y + yRef
        }
    },


    /**
     * Allow to compute relational dimention
     * @param parents     : array<node> : components parents
     * @param heightRatio : number      : current component height ratio
     * @param widthRatio  : number      : current component width ratio
     * @param fontRatio   : number      : current font ratio
     */
    computeDimension : function(parents,heightRatio, widthRatio, fontRatio){
        var resultHeightRatio = heightRatio;
        var resultWidthRatio  = widthRatio;
        var resultFontRatio   = fontRatio;

        if(isNotNull(parents)){
            resultHeightRatio = 1;
            resultWidthRatio  = 1;
            resultFontRatio   = 1;
            var parentHeight  = 1;
            var parentWidth   = 1;
            var parentFont   = 1;


            if(parents.length>0){
                for(let i=parents.length-1; i >= 0; i--){
                   var localHeight = parents[i].getAttribute("height");
                   var localWidth  = parents[i].getAttribute("width");
                   var localFont  = parents[i].getAttribute("fontratio");
        
                   if(isNotNull(localHeight)){
                     parentHeight = parentHeight * localHeight;
                   }
                   if(isNotNull(localWidth)){
                     parentWidth = parentWidth * localWidth;
                   }
                   if(isNotNull(localFont)){
                    parentFont = parentFont * localFont;
                  }
                   
                }
            }
        
            if(isNotNull(heightRatio)){
               resultHeightRatio = parentHeight*heightRatio;
            }else{
               resultHeightRatio = parentHeight;
            }
        
            if(isNotNull(widthRatio)){
               resultWidthRatio = parentWidth*widthRatio;
            }else{
               resultWidthRatio = parentWidth;
            }

            if(isNotNull(fontRatio)){
                resultFontRatio = parentFont*fontRatio;
             }else{
                resultFontRatio = parentFont;
             }
        }
        
        return {
            "height" : resultHeightRatio,
            "width"  : resultWidthRatio,
            "font"   : resultFontRatio
        }
    },

    computeFontSize : function(fontRatio, height, width, ratioByHeight, ratioByWidth ){
        var result = org.inugami.values.size.font;

        if(isNotNull(ratioByHeight) || isNotNull(ratioByWidth)){
            if(ratioByHeight){
                result = height*fontRatio;
            }else{
                result = width*fontRatio;
            }
        }else{
            var fullSize = height*width;
            result = fullSize*fontRatio;
        }
        return result;
    }

};


svg.icons = {
    search : function(value){
        var result = "";
        if(isNotNull(value)){
            result = svg.icons.icons[value];
            if(isNull(result)){
                var valueTrim = value.trim(value);
                var keys = Object.keys(svg.icons.icons);

                for(var i=0; i<keys.length; i++){
                    var key = keys[i];
                    var regex = new RegExp('^('+key+'\\s+)|(.*\\s+'+key+'\\s+.*)|(.*\\s+'+key+')$');

                    if(valueTrim.match(regex)){
                        result = svg.icons.icons[key];
                        break;
                    }
                }
                
            }
        }
        return result;
    },
    icons :  {
        "fa-500px": "&#xf26e",
        "fa-accessible-icon": "&#xf368",
        "fa-accusoft": "&#xf369",
        "fa-address-book": "&#xf2b9",
        "fa-address-card": "&#xf2bb",
        "fa-adjust": "&#xf042",
        "fa-adn": "&#xf170",
        "fa-adversal": "&#xf36a",
        "fa-affiliatetheme": "&#xf36b",
        "fa-algolia": "&#xf36c",
        "fa-align-center": "&#xf037",
        "fa-align-justify": "&#xf039",
        "fa-align-left": "&#xf036",
        "fa-align-right": "&#xf038",
        "fa-amazon": "&#xf270",
        "fa-amazon-pay": "&#xf42c",
        "fa-ambulance": "&#xf0f9",
        "fa-american-sign-language-interpreting": "&#xf2a3",
        "fa-amilia": "&#xf36d",
        "fa-anchor": "&#xf13d",
        "fa-android": "&#xf17b",
        "fa-angellist": "&#xf209",
        "fa-angle-double-down": "&#xf103",
        "fa-angle-double-left": "&#xf100",
        "fa-angle-double-right": "&#xf101",
        "fa-angle-double-up": "&#xf102",
        "fa-angle-down": "&#xf107",
        "fa-angle-left": "&#xf104",
        "fa-angle-right": "&#xf105",
        "fa-angle-up": "&#xf106",
        "fa-angrycreative": "&#xf36e",
        "fa-angular": "&#xf420",
        "fa-app-store": "&#xf36f",
        "fa-app-store-ios": "&#xf370",
        "fa-apper": "&#xf371",
        "fa-apple": "&#xf179",
        "fa-apple-pay": "&#xf415",
        "fa-archive": "&#xf187",
        "fa-arrow-alt-circle-down": "&#xf358",
        "fa-arrow-alt-circle-left": "&#xf359",
        "fa-arrow-alt-circle-right": "&#xf35a",
        "fa-arrow-alt-circle-up": "&#xf35b",
        "fa-arrow-circle-down": "&#xf0ab",
        "fa-arrow-circle-left": "&#xf0a8",
        "fa-arrow-circle-right": "&#xf0a9",
        "fa-arrow-circle-up": "&#xf0aa",
        "fa-arrow-down": "&#xf063",
        "fa-arrow-left": "&#xf060",
        "fa-arrow-right": "&#xf061",
        "fa-arrow-up": "&#xf062",
        "fa-arrows-alt": "&#xf0b2",
        "fa-arrows-alt-h": "&#xf337",
        "fa-arrows-alt-v": "&#xf338",
        "fa-assistive-listening-systems": "&#xf2a2",
        "fa-asterisk": "&#xf069",
        "fa-asymmetrik": "&#xf372",
        "fa-at": "&#xf1fa",
        "fa-audible": "&#xf373",
        "fa-audio-description": "&#xf29e",
        "fa-autoprefixer": "&#xf41c",
        "fa-avianex": "&#xf374",
        "fa-aviato": "&#xf421",
        "fa-aws": "&#xf375",
        "fa-backward": "&#xf04a",
        "fa-balance-scale": "&#xf24e",
        "fa-ban": "&#xf05e",
        "fa-band-aid": "&#xf462",
        "fa-bandcamp": "&#xf2d5",
        "fa-barcode": "&#xf02a",
        "fa-bars": "&#xf0c9",
        "fa-baseball-ball": "&#xf433",
        "fa-basketball-ball": "&#xf434",
        "fa-bath": "&#xf2cd",
        "fa-battery-empty": "&#xf244",
        "fa-battery-full": "&#xf240",
        "fa-battery-half": "&#xf242",
        "fa-battery-quarter": "&#xf243",
        "fa-battery-three-quarters": "&#xf241",
        "fa-bed": "&#xf236",
        "fa-beer": "&#xf0fc",
        "fa-behance": "&#xf1b4",
        "fa-behance-square": "&#xf1b5",
        "fa-bell": "&#xf0f3",
        "fa-bell-slash": "&#xf1f6",
        "fa-bicycle": "&#xf206",
        "fa-bimobject": "&#xf378",
        "fa-binoculars": "&#xf1e5",
        "fa-birthday-cake": "&#xf1fd",
        "fa-bitbucket": "&#xf171",
        "fa-bitcoin": "&#xf379",
        "fa-bity": "&#xf37a",
        "fa-black-tie": "&#xf27e",
        "fa-blackberry": "&#xf37b",
        "fa-blind": "&#xf29d",
        "fa-blogger": "&#xf37c",
        "fa-blogger-b": "&#xf37d",
        "fa-bluetooth": "&#xf293",
        "fa-bluetooth-b": "&#xf294",
        "fa-bold": "&#xf032",
        "fa-bolt": "&#xf0e7",
        "fa-bomb": "&#xf1e2",
        "fa-book": "&#xf02d",
        "fa-bookmark": "&#xf02e",
        "fa-bowling-ball": "&#xf436",
        "fa-box": "&#xf466",
        "fa-boxes": "&#xf468",
        "fa-braille": "&#xf2a1",
        "fa-briefcase": "&#xf0b1",
        "fa-btc": "&#xf15a",
        "fa-bug": "&#xf188",
        "fa-building": "&#xf1ad",
        "fa-bullhorn": "&#xf0a1",
        "fa-bullseye": "&#xf140",
        "fa-buromobelexperte": "&#xf37f",
        "fa-bus": "&#xf207",
        "fa-buysellads": "&#xf20d",
        "fa-calculator": "&#xf1ec",
        "fa-calendar": "&#xf133",
        "fa-calendar-alt": "&#xf073",
        "fa-calendar-check": "&#xf274",
        "fa-calendar-minus": "&#xf272",
        "fa-calendar-plus": "&#xf271",
        "fa-calendar-times": "&#xf273",
        "fa-camera": "&#xf030",
        "fa-camera-retro": "&#xf083",
        "fa-car": "&#xf1b9",
        "fa-caret-down": "&#xf0d7",
        "fa-caret-left": "&#xf0d9",
        "fa-caret-right": "&#xf0da",
        "fa-caret-square-down": "&#xf150",
        "fa-caret-square-left": "&#xf191",
        "fa-caret-square-right": "&#xf152",
        "fa-caret-square-up": "&#xf151",
        "fa-caret-up": "&#xf0d8",
        "fa-cart-arrow-down": "&#xf218",
        "fa-cart-plus": "&#xf217",
        "fa-cc-amazon-pay": "&#xf42d",
        "fa-cc-amex": "&#xf1f3",
        "fa-cc-apple-pay": "&#xf416",
        "fa-cc-diners-club": "&#xf24c",
        "fa-cc-discover": "&#xf1f2",
        "fa-cc-jcb": "&#xf24b",
        "fa-cc-mastercard": "&#xf1f1",
        "fa-cc-paypal": "&#xf1f4",
        "fa-cc-stripe": "&#xf1f5",
        "fa-cc-visa": "&#xf1f0",
        "fa-centercode": "&#xf380",
        "fa-certificate": "&#xf0a3",
        "fa-chart-area": "&#xf1fe",
        "fa-chart-bar": "&#xf080",
        "fa-chart-line": "&#xf201",
        "fa-chart-pie": "&#xf200",
        "fa-check": "&#xf00c",
        "fa-check-circle": "&#xf058",
        "fa-check-square": "&#xf14a",
        "fa-chess": "&#xf439",
        "fa-chess-bishop": "&#xf43a",
        "fa-chess-board": "&#xf43c",
        "fa-chess-king": "&#xf43f",
        "fa-chess-knight": "&#xf441",
        "fa-chess-pawn": "&#xf443",
        "fa-chess-queen": "&#xf445",
        "fa-chess-rook": "&#xf447",
        "fa-chevron-circle-down": "&#xf13a",
        "fa-chevron-circle-left": "&#xf137",
        "fa-chevron-circle-right": "&#xf138",
        "fa-chevron-circle-up": "&#xf139",
        "fa-chevron-down": "&#xf078",
        "fa-chevron-left": "&#xf053",
        "fa-chevron-right": "&#xf054",
        "fa-chevron-up": "&#xf077",
        "fa-child": "&#xf1ae",
        "fa-chrome": "&#xf268",
        "fa-circle": "&#xf111",
        "fa-circle-notch": "&#xf1ce",
        "fa-clipboard": "&#xf328",
        "fa-clipboard-check": "&#xf46c",
        "fa-clipboard-list": "&#xf46d",
        "fa-clock": "&#xf017",
        "fa-clone": "&#xf24d",
        "fa-closed-captioning": "&#xf20a",
        "fa-cloud": "&#xf0c2",
        "fa-cloud-download-alt": "&#xf381",
        "fa-cloud-upload-alt": "&#xf382",
        "fa-cloudscale": "&#xf383",
        "fa-cloudsmith": "&#xf384",
        "fa-cloudversify": "&#xf385",
        "fa-code": "&#xf121",
        "fa-code-branch": "&#xf126",
        "fa-codepen": "&#xf1cb",
        "fa-codiepie": "&#xf284",
        "fa-coffee": "&#xf0f4",
        "fa-cog": "&#xf013",
        "fa-cogs": "&#xf085",
        "fa-columns": "&#xf0db",
        "fa-comment": "&#xf075",
        "fa-comment-alt": "&#xf27a",
        "fa-comments": "&#xf086",
        "fa-compass": "&#xf14e",
        "fa-compress": "&#xf066",
        "fa-connectdevelop": "&#xf20e",
        "fa-contao": "&#xf26d",
        "fa-copy": "&#xf0c5",
        "fa-copyright": "&#xf1f9",
        "fa-cpanel": "&#xf388",
        "fa-creative-commons": "&#xf25e",
        "fa-credit-card": "&#xf09d",
        "fa-crop": "&#xf125",
        "fa-crosshairs": "&#xf05b",
        "fa-css3": "&#xf13c",
        "fa-css3-alt": "&#xf38b",
        "fa-cube": "&#xf1b2",
        "fa-cubes": "&#xf1b3",
        "fa-cut": "&#xf0c4",
        "fa-cuttlefish": "&#xf38c",
        "fa-d-and-d": "&#xf38d",
        "fa-dashcube": "&#xf210",
        "fa-database": "&#xf1c0",
        "fa-deaf": "&#xf2a4",
        "fa-delicious": "&#xf1a5",
        "fa-deploydog": "&#xf38e",
        "fa-deskpro": "&#xf38f",
        "fa-desktop": "&#xf108",
        "fa-deviantart": "&#xf1bd",
        "fa-digg": "&#xf1a6",
        "fa-digital-ocean": "&#xf391",
        "fa-discord": "&#xf392",
        "fa-discourse": "&#xf393",
        "fa-dna": "&#xf471",
        "fa-dochub": "&#xf394",
        "fa-docker": "&#xf395",
        "fa-dollar-sign": "&#xf155",
        "fa-dolly": "&#xf472",
        "fa-dolly-flatbed": "&#xf474",
        "fa-dot-circle": "&#xf192",
        "fa-download": "&#xf019",
        "fa-draft2digital": "&#xf396",
        "fa-dribbble": "&#xf17d",
        "fa-dribbble-square": "&#xf397",
        "fa-dropbox": "&#xf16b",
        "fa-drupal": "&#xf1a9",
        "fa-dyalog": "&#xf399",
        "fa-earlybirds": "&#xf39a",
        "fa-edge": "&#xf282",
        "fa-edit": "&#xf044",
        "fa-eject": "&#xf052",
        "fa-elementor": "&#xf430",
        "fa-ellipsis-h": "&#xf141",
        "fa-ellipsis-v": "&#xf142",
        "fa-ember": "&#xf423",
        "fa-empire": "&#xf1d1",
        "fa-envelope": "&#xf0e0",
        "fa-envelope-open": "&#xf2b6",
        "fa-envelope-square": "&#xf199",
        "fa-envira": "&#xf299",
        "fa-eraser": "&#xf12d",
        "fa-erlang": "&#xf39d",
        "fa-ethereum": "&#xf42e",
        "fa-etsy": "&#xf2d7",
        "fa-euro-sign": "&#xf153",
        "fa-exchange-alt": "&#xf362",
        "fa-exclamation": "&#xf12a",
        "fa-exclamation-circle": "&#xf06a",
        "fa-exclamation-triangle": "&#xf071",
        "fa-expand": "&#xf065",
        "fa-expand-arrows-alt": "&#xf31e",
        "fa-expeditedssl": "&#xf23e",
        "fa-external-link-alt": "&#xf35d",
        "fa-external-link-square-alt": "&#xf360",
        "fa-eye": "&#xf06e",
        "fa-eye-dropper": "&#xf1fb",
        "fa-eye-slash": "&#xf070",
        "fa-facebook": "&#xf09a",
        "fa-facebook-f": "&#xf39e",
        "fa-facebook-messenger": "&#xf39f",
        "fa-facebook-square": "&#xf082",
        "fa-fast-backward": "&#xf049",
        "fa-fast-forward": "&#xf050",
        "fa-fax": "&#xf1ac",
        "fa-female": "&#xf182",
        "fa-fighter-jet": "&#xf0fb",
        "fa-file": "&#xf15b",
        "fa-file-alt": "&#xf15c",
        "fa-file-archive": "&#xf1c6",
        "fa-file-audio": "&#xf1c7",
        "fa-file-code": "&#xf1c9",
        "fa-file-excel": "&#xf1c3",
        "fa-file-image": "&#xf1c5",
        "fa-file-pdf": "&#xf1c1",
        "fa-file-powerpoint": "&#xf1c4",
        "fa-file-video": "&#xf1c8",
        "fa-file-word": "&#xf1c2",
        "fa-film": "&#xf008",
        "fa-filter": "&#xf0b0",
        "fa-fire": "&#xf06d",
        "fa-fire-extinguisher": "&#xf134",
        "fa-firefox": "&#xf269",
        "fa-first-aid": "&#xf479",
        "fa-first-order": "&#xf2b0",
        "fa-firstdraft": "&#xf3a1",
        "fa-flag": "&#xf024",
        "fa-flag-checkered": "&#xf11e",
        "fa-flask": "&#xf0c3",
        "fa-flickr": "&#xf16e",
        "fa-flipboard": "&#xf44d",
        "fa-fly": "&#xf417",
        "fa-folder": "&#xf07b",
        "fa-folder-open": "&#xf07c",
        "fa-font": "&#xf031",
        "fa-font-awesome": "&#xf2b4",
        "fa-font-awesome-alt": "&#xf35c",
        "fa-font-awesome-flag": "&#xf425",
        "fa-fonticons": "&#xf280",
        "fa-fonticons-fi": "&#xf3a2",
        "fa-football-ball": "&#xf44e",
        "fa-fort-awesome": "&#xf286",
        "fa-fort-awesome-alt": "&#xf3a3",
        "fa-forumbee": "&#xf211",
        "fa-forward": "&#xf04e",
        "fa-foursquare": "&#xf180",
        "fa-free-code-camp": "&#xf2c5",
        "fa-freebsd": "&#xf3a4",
        "fa-frown": "&#xf119",
        "fa-futbol": "&#xf1e3",
        "fa-gamepad": "&#xf11b",
        "fa-gavel": "&#xf0e3",
        "fa-gem": "&#xf3a5",
        "fa-genderless": "&#xf22d",
        "fa-get-pocket": "&#xf265",
        "fa-gg": "&#xf260",
        "fa-gg-circle": "&#xf261",
        "fa-gift": "&#xf06b",
        "fa-git": "&#xf1d3",
        "fa-git-square": "&#xf1d2",
        "fa-github": "&#xf09b",
        "fa-github-alt": "&#xf113",
        "fa-github-square": "&#xf092",
        "fa-gitkraken": "&#xf3a6",
        "fa-gitlab": "&#xf296",
        "fa-gitter": "&#xf426",
        "fa-glass-martini": "&#xf000",
        "fa-glide": "&#xf2a5",
        "fa-glide-g": "&#xf2a6",
        "fa-globe": "&#xf0ac",
        "fa-gofore": "&#xf3a7",
        "fa-golf-ball": "&#xf450",
        "fa-goodreads": "&#xf3a8",
        "fa-goodreads-g": "&#xf3a9",
        "fa-google": "&#xf1a0",
        "fa-google-drive": "&#xf3aa",
        "fa-google-play": "&#xf3ab",
        "fa-google-plus": "&#xf2b3",
        "fa-google-plus-g": "&#xf0d5",
        "fa-google-plus-square": "&#xf0d4",
        "fa-google-wallet": "&#xf1ee",
        "fa-graduation-cap": "&#xf19d",
        "fa-gratipay": "&#xf184",
        "fa-grav": "&#xf2d6",
        "fa-gripfire": "&#xf3ac",
        "fa-grunt": "&#xf3ad",
        "fa-gulp": "&#xf3ae",
        "fa-h-square": "&#xf0fd",
        "fa-hacker-news": "&#xf1d4",
        "fa-hacker-news-square": "&#xf3af",
        "fa-hand-lizard": "&#xf258",
        "fa-hand-paper": "&#xf256",
        "fa-hand-peace": "&#xf25b",
        "fa-hand-point-down": "&#xf0a7",
        "fa-hand-point-left": "&#xf0a5",
        "fa-hand-point-right": "&#xf0a4",
        "fa-hand-point-up": "&#xf0a6",
        "fa-hand-pointer": "&#xf25a",
        "fa-hand-rock": "&#xf255",
        "fa-hand-scissors": "&#xf257",
        "fa-hand-spock": "&#xf259",
        "fa-handshake": "&#xf2b5",
        "fa-hashtag": "&#xf292",
        "fa-hdd": "&#xf0a0",
        "fa-heading": "&#xf1dc",
        "fa-headphones": "&#xf025",
        "fa-heart": "&#xf004",
        "fa-heartbeat": "&#xf21e",
        "fa-hips": "&#xf452",
        "fa-hire-a-helper": "&#xf3b0",
        "fa-history": "&#xf1da",
        "fa-hockey-puck": "&#xf453",
        "fa-home": "&#xf015",
        "fa-hooli": "&#xf427",
        "fa-hospital": "&#xf0f8",
        "fa-hospital-symbol": "&#xf47e",
        "fa-hotjar": "&#xf3b1",
        "fa-hourglass": "&#xf254",
        "fa-hourglass-end": "&#xf253",
        "fa-hourglass-half": "&#xf252",
        "fa-hourglass-start": "&#xf251",
        "fa-houzz": "&#xf27c",
        "fa-html5": "&#xf13b",
        "fa-hubspot": "&#xf3b2",
        "fa-i-cursor": "&#xf246",
        "fa-id-badge": "&#xf2c1",
        "fa-id-card": "&#xf2c2",
        "fa-image": "&#xf03e",
        "fa-images": "&#xf302",
        "fa-imdb": "&#xf2d8",
        "fa-inbox": "&#xf01c",
        "fa-indent": "&#xf03c",
        "fa-industry": "&#xf275",
        "fa-info": "&#xf129",
        "fa-info-circle": "&#xf05a",
        "fa-instagram": "&#xf16d",
        "fa-internet-explorer": "&#xf26b",
        "fa-ioxhost": "&#xf208",
        "fa-italic": "&#xf033",
        "fa-itunes": "&#xf3b4",
        "fa-itunes-note": "&#xf3b5",
        "fa-jenkins": "&#xf3b6",
        "fa-joget": "&#xf3b7",
        "fa-joomla": "&#xf1aa",
        "fa-js": "&#xf3b8",
        "fa-js-square": "&#xf3b9",
        "fa-jsfiddle": "&#xf1cc",
        "fa-key": "&#xf084",
        "fa-keyboard": "&#xf11c",
        "fa-keycdn": "&#xf3ba",
        "fa-kickstarter": "&#xf3bb",
        "fa-kickstarter-k": "&#xf3bc",
        "fa-korvue": "&#xf42f",
        "fa-language": "&#xf1ab",
        "fa-laptop": "&#xf109",
        "fa-laravel": "&#xf3bd",
        "fa-lastfm": "&#xf202",
        "fa-lastfm-square": "&#xf203",
        "fa-leaf": "&#xf06c",
        "fa-leanpub": "&#xf212",
        "fa-lemon": "&#xf094",
        "fa-less": "&#xf41d",
        "fa-level-down-alt": "&#xf3be",
        "fa-level-up-alt": "&#xf3bf",
        "fa-life-ring": "&#xf1cd",
        "fa-lightbulb": "&#xf0eb",
        "fa-line": "&#xf3c0",
        "fa-link": "&#xf0c1",
        "fa-linkedin": "&#xf08c",
        "fa-linkedin-in": "&#xf0e1",
        "fa-linode": "&#xf2b8",
        "fa-linux": "&#xf17c",
        "fa-lira-sign": "&#xf195",
        "fa-list": "&#xf03a",
        "fa-list-alt": "&#xf022",
        "fa-list-ol": "&#xf0cb",
        "fa-list-ul": "&#xf0ca",
        "fa-location-arrow": "&#xf124",
        "fa-lock": "&#xf023",
        "fa-lock-open": "&#xf3c1",
        "fa-long-arrow-alt-down": "&#xf309",
        "fa-long-arrow-alt-left": "&#xf30a",
        "fa-long-arrow-alt-right": "&#xf30b",
        "fa-long-arrow-alt-up": "&#xf30c",
        "fa-low-vision": "&#xf2a8",
        "fa-lyft": "&#xf3c3",
        "fa-magento": "&#xf3c4",
        "fa-magic": "&#xf0d0",
        "fa-magnet": "&#xf076",
        "fa-male": "&#xf183",
        "fa-map": "&#xf279",
        "fa-map-marker": "&#xf041",
        "fa-map-marker-alt": "&#xf3c5",
        "fa-map-pin": "&#xf276",
        "fa-map-signs": "&#xf277",
        "fa-mars": "&#xf222",
        "fa-mars-double": "&#xf227",
        "fa-mars-stroke": "&#xf229",
        "fa-mars-stroke-h": "&#xf22b",
        "fa-mars-stroke-v": "&#xf22a",
        "fa-maxcdn": "&#xf136",
        "fa-medapps": "&#xf3c6",
        "fa-medium": "&#xf23a",
        "fa-medium-m": "&#xf3c7",
        "fa-medkit": "&#xf0fa",
        "fa-medrt": "&#xf3c8",
        "fa-meetup": "&#xf2e0",
        "fa-meh": "&#xf11a",
        "fa-mercury": "&#xf223",
        "fa-microchip": "&#xf2db",
        "fa-microphone": "&#xf130",
        "fa-microphone-slash": "&#xf131",
        "fa-microsoft": "&#xf3ca",
        "fa-minus": "&#xf068",
        "fa-minus-circle": "&#xf056",
        "fa-minus-square": "&#xf146",
        "fa-mix": "&#xf3cb",
        "fa-mixcloud": "&#xf289",
        "fa-mizuni": "&#xf3cc",
        "fa-mobile": "&#xf10b",
        "fa-mobile-alt": "&#xf3cd",
        "fa-modx": "&#xf285",
        "fa-monero": "&#xf3d0",
        "fa-money-bill-alt": "&#xf3d1",
        "fa-moon": "&#xf186",
        "fa-motorcycle": "&#xf21c",
        "fa-mouse-pointer": "&#xf245",
        "fa-music": "&#xf001",
        "fa-napster": "&#xf3d2",
        "fa-neuter": "&#xf22c",
        "fa-newspaper": "&#xf1ea",
        "fa-nintendo-switch": "&#xf418",
        "fa-node": "&#xf419",
        "fa-node-js": "&#xf3d3",
        "fa-npm": "&#xf3d4",
        "fa-ns8": "&#xf3d5",
        "fa-nutritionix": "&#xf3d6",
        "fa-object-group": "&#xf247",
        "fa-object-ungroup": "&#xf248",
        "fa-odnoklassniki": "&#xf263",
        "fa-odnoklassniki-square": "&#xf264",
        "fa-opencart": "&#xf23d",
        "fa-openid": "&#xf19b",
        "fa-opera": "&#xf26a",
        "fa-optin-monster": "&#xf23c",
        "fa-osi": "&#xf41a",
        "fa-outdent": "&#xf03b",
        "fa-page4": "&#xf3d7",
        "fa-pagelines": "&#xf18c",
        "fa-paint-brush": "&#xf1fc",
        "fa-palfed": "&#xf3d8",
        "fa-pallet": "&#xf482",
        "fa-paper-plane": "&#xf1d8",
        "fa-paperclip": "&#xf0c6",
        "fa-paragraph": "&#xf1dd",
        "fa-paste": "&#xf0ea",
        "fa-patreon": "&#xf3d9",
        "fa-pause": "&#xf04c",
        "fa-pause-circle": "&#xf28b",
        "fa-paw": "&#xf1b0",
        "fa-paypal": "&#xf1ed",
        "fa-pen-square": "&#xf14b",
        "fa-pencil-alt": "&#xf303",
        "fa-percent": "&#xf295",
        "fa-periscope": "&#xf3da",
        "fa-phabricator": "&#xf3db",
        "fa-phoenix-framework": "&#xf3dc",
        "fa-phone": "&#xf095",
        "fa-phone-square": "&#xf098",
        "fa-phone-volume": "&#xf2a0",
        "fa-php": "&#xf457",
        "fa-pied-piper": "&#xf2ae",
        "fa-pied-piper-alt": "&#xf1a8",
        "fa-pied-piper-pp": "&#xf1a7",
        "fa-pills": "&#xf484",
        "fa-pinterest": "&#xf0d2",
        "fa-pinterest-p": "&#xf231",
        "fa-pinterest-square": "&#xf0d3",
        "fa-plane": "&#xf072",
        "fa-play": "&#xf04b",
        "fa-play-circle": "&#xf144",
        "fa-playstation": "&#xf3df",
        "fa-plug": "&#xf1e6",
        "fa-plus": "&#xf067",
        "fa-plus-circle": "&#xf055",
        "fa-plus-square": "&#xf0fe",
        "fa-podcast": "&#xf2ce",
        "fa-pound-sign": "&#xf154",
        "fa-power-off": "&#xf011",
        "fa-print": "&#xf02f",
        "fa-product-hunt": "&#xf288",
        "fa-pushed": "&#xf3e1",
        "fa-puzzle-piece": "&#xf12e",
        "fa-python": "&#xf3e2",
        "fa-qq": "&#xf1d6",
        "fa-qrcode": "&#xf029",
        "fa-question": "&#xf128",
        "fa-question-circle": "&#xf059",
        "fa-quidditch": "&#xf458",
        "fa-quinscape": "&#xf459",
        "fa-quora": "&#xf2c4",
        "fa-quote-left": "&#xf10d",
        "fa-quote-right": "&#xf10e",
        "fa-random": "&#xf074",
        "fa-ravelry": "&#xf2d9",
        "fa-react": "&#xf41b",
        "fa-rebel": "&#xf1d0",
        "fa-recycle": "&#xf1b8",
        "fa-red-river": "&#xf3e3",
        "fa-reddit": "&#xf1a1",
        "fa-reddit-alien": "&#xf281",
        "fa-reddit-square": "&#xf1a2",
        "fa-redo": "&#xf01e",
        "fa-redo-alt": "&#xf2f9",
        "fa-registered": "&#xf25d",
        "fa-rendact": "&#xf3e4",
        "fa-renren": "&#xf18b",
        "fa-reply": "&#xf3e5",
        "fa-reply-all": "&#xf122",
        "fa-replyd": "&#xf3e6",
        "fa-resolving": "&#xf3e7",
        "fa-retweet": "&#xf079",
        "fa-road": "&#xf018",
        "fa-rocket": "&#xf135",
        "fa-rocketchat": "&#xf3e8",
        "fa-rockrms": "&#xf3e9",
        "fa-rss": "&#xf09e",
        "fa-rss-square": "&#xf143",
        "fa-ruble-sign": "&#xf158",
        "fa-rupee-sign": "&#xf156",
        "fa-safari": "&#xf267",
        "fa-sass": "&#xf41e",
        "fa-save": "&#xf0c7",
        "fa-schlix": "&#xf3ea",
        "fa-scribd": "&#xf28a",
        "fa-search": "&#xf002",
        "fa-search-minus": "&#xf010",
        "fa-search-plus": "&#xf00e",
        "fa-searchengin": "&#xf3eb",
        "fa-sellcast": "&#xf2da",
        "fa-sellsy": "&#xf213",
        "fa-server": "&#xf233",
        "fa-servicestack": "&#xf3ec",
        "fa-share": "&#xf064",
        "fa-share-alt": "&#xf1e0",
        "fa-share-alt-square": "&#xf1e1",
        "fa-share-square": "&#xf14d",
        "fa-shekel-sign": "&#xf20b",
        "fa-shield-alt": "&#xf3ed",
        "fa-ship": "&#xf21a",
        "fa-shipping-fast": "&#xf48b",
        "fa-shirtsinbulk": "&#xf214",
        "fa-shopping-bag": "&#xf290",
        "fa-shopping-basket": "&#xf291",
        "fa-shopping-cart": "&#xf07a",
        "fa-shower": "&#xf2cc",
        "fa-sign-in-alt": "&#xf2f6",
        "fa-sign-language": "&#xf2a7",
        "fa-sign-out-alt": "&#xf2f5",
        "fa-signal": "&#xf012",
        "fa-simplybuilt": "&#xf215",
        "fa-sistrix": "&#xf3ee",
        "fa-sitemap": "&#xf0e8",
        "fa-skyatlas": "&#xf216",
        "fa-skype": "&#xf17e",
        "fa-slack": "&#xf198",
        "fa-slack-hash": "&#xf3ef",
        "fa-sliders-h": "&#xf1de",
        "fa-slideshare": "&#xf1e7",
        "fa-smile": "&#xf118",
        "fa-snapchat": "&#xf2ab",
        "fa-snapchat-ghost": "&#xf2ac",
        "fa-snapchat-square": "&#xf2ad",
        "fa-snowflake": "&#xf2dc",
        "fa-sort": "&#xf0dc",
        "fa-sort-alpha-down": "&#xf15d",
        "fa-sort-alpha-up": "&#xf15e",
        "fa-sort-amount-down": "&#xf160",
        "fa-sort-amount-up": "&#xf161",
        "fa-sort-down": "&#xf0dd",
        "fa-sort-numeric-down": "&#xf162",
        "fa-sort-numeric-up": "&#xf163",
        "fa-sort-up": "&#xf0de",
        "fa-soundcloud": "&#xf1be",
        "fa-space-shuttle": "&#xf197",
        "fa-speakap": "&#xf3f3",
        "fa-spinner": "&#xf110",
        "fa-spotify": "&#xf1bc",
        "fa-square": "&#xf0c8",
        "fa-square-full": "&#xf45c",
        "fa-stack-exchange": "&#xf18d",
        "fa-stack-overflow": "&#xf16c",
        "fa-star": "&#xf005",
        "fa-star-half": "&#xf089",
        "fa-staylinked": "&#xf3f5",
        "fa-steam": "&#xf1b6",
        "fa-steam-square": "&#xf1b7",
        "fa-steam-symbol": "&#xf3f6",
        "fa-step-backward": "&#xf048",
        "fa-step-forward": "&#xf051",
        "fa-stethoscope": "&#xf0f1",
        "fa-sticker-mule": "&#xf3f7",
        "fa-sticky-note": "&#xf249",
        "fa-stop": "&#xf04d",
        "fa-stop-circle": "&#xf28d",
        "fa-stopwatch": "&#xf2f2",
        "fa-strava": "&#xf428",
        "fa-street-view": "&#xf21d",
        "fa-strikethrough": "&#xf0cc",
        "fa-stripe": "&#xf429",
        "fa-stripe-s": "&#xf42a",
        "fa-studiovinari": "&#xf3f8",
        "fa-stumbleupon": "&#xf1a4",
        "fa-stumbleupon-circle": "&#xf1a3",
        "fa-subscript": "&#xf12c",
        "fa-subway": "&#xf239",
        "fa-suitcase": "&#xf0f2",
        "fa-sun": "&#xf185",
        "fa-superpowers": "&#xf2dd",
        "fa-superscript": "&#xf12b",
        "fa-supple": "&#xf3f9",
        "fa-sync": "&#xf021",
        "fa-sync-alt": "&#xf2f1",
        "fa-syringe": "&#xf48e",
        "fa-table": "&#xf0ce",
        "fa-table-tennis": "&#xf45d",
        "fa-tablet": "&#xf10a",
        "fa-tablet-alt": "&#xf3fa",
        "fa-tachometer-alt": "&#xf3fd",
        "fa-tag": "&#xf02b",
        "fa-tags": "&#xf02c",
        "fa-tasks": "&#xf0ae",
        "fa-taxi": "&#xf1ba",
        "fa-telegram": "&#xf2c6",
        "fa-telegram-plane": "&#xf3fe",
        "fa-tencent-weibo": "&#xf1d5",
        "fa-terminal": "&#xf120",
        "fa-text-height": "&#xf034",
        "fa-text-width": "&#xf035",
        "fa-th": "&#xf00a",
        "fa-th-large": "&#xf009",
        "fa-th-list": "&#xf00b",
        "fa-themeisle": "&#xf2b2",
        "fa-thermometer": "&#xf491",
        "fa-thermometer-empty": "&#xf2cb",
        "fa-thermometer-full": "&#xf2c7",
        "fa-thermometer-half": "&#xf2c9",
        "fa-thermometer-quarter": "&#xf2ca",
        "fa-thermometer-three-quarters": "&#xf2c8",
        "fa-thumbs-down": "&#xf165",
        "fa-thumbs-up": "&#xf164",
        "fa-thumbtack": "&#xf08d",
        "fa-ticket-alt": "&#xf3ff",
        "fa-times": "&#xf00d",
        "fa-times-circle": "&#xf057",
        "fa-tint": "&#xf043",
        "fa-toggle-off": "&#xf204",
        "fa-toggle-on": "&#xf205",
        "fa-trademark": "&#xf25c",
        "fa-train": "&#xf238",
        "fa-transgender": "&#xf224",
        "fa-transgender-alt": "&#xf225",
        "fa-trash": "&#xf1f8",
        "fa-trash-alt": "&#xf2ed",
        "fa-tree": "&#xf1bb",
        "fa-trello": "&#xf181",
        "fa-tripadvisor": "&#xf262",
        "fa-trophy": "&#xf091",
        "fa-truck": "&#xf0d1",
        "fa-tty": "&#xf1e4",
        "fa-tumblr": "&#xf173",
        "fa-tumblr-square": "&#xf174",
        "fa-tv": "&#xf26c",
        "fa-twitch": "&#xf1e8",
        "fa-twitter": "&#xf099",
        "fa-twitter-square": "&#xf081",
        "fa-typo3": "&#xf42b",
        "fa-uber": "&#xf402",
        "fa-uikit": "&#xf403",
        "fa-umbrella": "&#xf0e9",
        "fa-underline": "&#xf0cd",
        "fa-undo": "&#xf0e2",
        "fa-undo-alt": "&#xf2ea",
        "fa-uniregistry": "&#xf404",
        "fa-universal-access": "&#xf29a",
        "fa-university": "&#xf19c",
        "fa-unlink": "&#xf127",
        "fa-unlock": "&#xf09c",
        "fa-unlock-alt": "&#xf13e",
        "fa-untappd": "&#xf405",
        "fa-upload": "&#xf093",
        "fa-usb": "&#xf287",
        "fa-user": "&#xf007",
        "fa-user-circle": "&#xf2bd",
        "fa-user-md": "&#xf0f0",
        "fa-user-plus": "&#xf234",
        "fa-user-secret": "&#xf21b",
        "fa-user-times": "&#xf235",
        "fa-users": "&#xf0c0",
        "fa-ussunnah": "&#xf407",
        "fa-utensil-spoon": "&#xf2e5",
        "fa-utensils": "&#xf2e7",
        "fa-vaadin": "&#xf408",
        "fa-venus": "&#xf221",
        "fa-venus-double": "&#xf226",
        "fa-venus-mars": "&#xf228",
        "fa-viacoin": "&#xf237",
        "fa-viadeo": "&#xf2a9",
        "fa-viadeo-square": "&#xf2aa",
        "fa-viber": "&#xf409",
        "fa-video": "&#xf03d",
        "fa-vimeo": "&#xf40a",
        "fa-vimeo-square": "&#xf194",
        "fa-vimeo-v": "&#xf27d",
        "fa-vine": "&#xf1ca",
        "fa-vk": "&#xf189",
        "fa-vnv": "&#xf40b",
        "fa-volleyball-ball": "&#xf45f",
        "fa-volume-down": "&#xf027",
        "fa-volume-off": "&#xf026",
        "fa-volume-up": "&#xf028",
        "fa-vuejs": "&#xf41f",
        "fa-warehouse": "&#xf494",
        "fa-weibo": "&#xf18a",
        "fa-weight": "&#xf496",
        "fa-weixin": "&#xf1d7",
        "fa-whatsapp": "&#xf232",
        "fa-whatsapp-square": "&#xf40c",
        "fa-wheelchair": "&#xf193",
        "fa-whmcs": "&#xf40d",
        "fa-wifi": "&#xf1eb",
        "fa-wikipedia-w": "&#xf266",
        "fa-window-close": "&#xf410",
        "fa-window-maximize": "&#xf2d0",
        "fa-window-minimize": "&#xf2d1",
        "fa-window-restore": "&#xf2d2",
        "fa-windows": "&#xf17a",
        "fa-won-sign": "&#xf159",
        "fa-wordpress": "&#xf19a",
        "fa-wordpress-simple": "&#xf411",
        "fa-wpbeginner": "&#xf297",
        "fa-wpexplorer": "&#xf2de",
        "fa-wpforms": "&#xf298",
        "fa-wrench": "&#xf0ad",
        "fa-xbox": "&#xf412",
        "fa-xing": "&#xf168",
        "fa-xing-square": "&#xf169",
        "fa-y-combinator": "&#xf23b",
        "fa-yahoo": "&#xf19e",
        "fa-yandex": "&#xf413",
        "fa-yandex-international": "&#xf414",
        "fa-yelp": "&#xf1e9",
        "fa-yen-sign": "&#xf157",
        "fa-yoast": "&#xf2b1",
        "fa-youtube": "&#xf167",
        "fa-youtube-square": "&#xf431"
    }
};
