// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// VALUES
// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
org.inugami.values = {
    screen : {
        height : $(window).height(),
        width : $(window).width()
    },
    mouse : {
        x : 0,
        y : 0
    },
    size : {
        font : 12
    },
    context: {
        URL : window.location.href,
        URL_FULL:$(location)[0].href,
        PROTOCOLE: null,
        CONTEXT : null,
        PAGE :null,
        OPTION:null,
        OPTION_TYPE:null
    }
};


$(document).mousemove(function(event) {
    org.inugami.values.mouse.x=event.pageX;
    org.inugami.values.mouse.y=event.pageY;
});