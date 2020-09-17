//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//INIT CORE
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
io.inugami.initialize = {
    initContext : function(){
        io.inugami.initialize._initContext(io.inugami.values.context);  
    },
    _initContext : function(value){
        
        value.URL  = window.location.href;
        var urlParts = /^(http(?:s){0,1})(?:[:][/]{1})(?:(?:[/])([^/]+))((?:[/][^/?#]+)*(?:[/]){0,1})(?:([?#])(.*)){0,1}$/.exec(value.URL);
        value.PROTOCOLE=urlParts[1];
        
        var lastIndexOfCtx = urlParts[3].lastIndexOf("/");
        value.CONTEXT=urlParts[3].substring(0,lastIndexOfCtx);
        value.PAGE=urlParts[3].substring(lastIndexOfCtx,urlParts[3].length);
        if(urlParts.length>3){
            value.OPTION = urlParts[5];
            value.OPTION_TYPE = urlParts[4];
        }
    }
};

new function(){
    io.inugami.initialize._initContext(io.inugami.constants.context);
}();

