//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// EVENTS
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
io.inugami.events = {
        _inner : {
          maxKey          : 10,
          enableCheat     : false,
          keysListener : {
            konami :  [38, 38, 40, 40, 37, 39, 37, 39, 66, 65],
            hadoken : [40,40,39,65],
          },
          inputs       :  []
        },
        type : {
            RESIZE                               : "resize",
            UPDATE_CONFIGURATION                 : "update-configuration",
            CHANGE_SCREEN_BEFORE                 : "change-screen-before",
            CHANGE_SCREEN                        : "change-screen",
            APP_COMPONENTS_EVENT                 : "app-components-event",
            APP_COMPONENTS_EVENT_CALLBACK_BEFORE : "app-components-event-callback-before",
            APP_COMPONENTS_EVENT_CALLBACK        : "app-components-event-callback",
            EVERY_SECOND                         : "every_second",
            EVERY_MINUTE                         : "every_minute",
            EVERY_PLAIN_MINUTE                   : "every_plain_minute",
            EVERY_PLAIN_HOUR                     : "every_plain_hour",
            EVERY_PLAIN_DAY                      : "every_plain_day"
        },
        buildEventFullName : function(pluginName, eventName){
          var realEvent = [];
          if(isNotNull(pluginName)){
            realEvent.push(pluginName);
          }
          realEvent.push(eventName);
          return realEvent.join('_');
        },
        fireEvent : function(eventName, data) {
            var event = new CustomEvent(eventName, { 'detail': data });
            document.dispatchEvent(event);
        },
        fireEventPlugin : function(pluginName, eventName, data, timestamp,alerts) {
          var localTimestamp = isNotNull(timestamp)?timestamp:Math.floor(Date.now() / 1000);
          var eventData = {
            "channel" : pluginName,
            "name"    : eventName,
            "time"    : localTimestamp,
            "data"    : {
              "channel" : pluginName,
              "event"   : eventName,
              "alerts"  : alerts,
              "values"  : data
            }
          };

          var event = new CustomEvent(io.inugami.events.buildEventFullName(pluginName, eventName), { 'detail': eventData });
          document.dispatchEvent(event);
        },
        addEventListenerByPlugin : function(pluginName, eventName, eventHandler){
          io.inugami.events.addEventListener(io.inugami.events.buildEventFullName(pluginName, eventName) , eventHandler);
        },
        addEventListener : function(eventName, eventHandler){
            document.addEventListener(eventName, eventHandler);
        },
        updateResize : function (){
          var data = {"height":$(window).height(),"width":$(window).width() };
          io.inugami.events.fireEvent(io.inugami.events.type.RESIZE, data);
        },
        _keyHandler : function(event){
          var key  = event.which;

          io.inugami.events._inner.inputs.push(key);
          if(io.inugami.events._inner.inputs.length > io.inugami.events._inner.maxKey){
            io.inugami.events._inner.inputs = io.inugami.events._inner.inputs.slice(1);
          }
          if(io.inugami.events._inner.enableCheat){
            console.log(io.inugami.events._inner.inputs.join(" ")) ;
          }

          var listnerName = Object.keys(io.inugami.events._inner.keysListener);
          for(var i=0; i<listnerName.length; i++){
             var name = listnerName[i];
             var listenerKeys = io.inugami.events._inner.keysListener[name];
             if(io.inugami.events._keyHandlerMatches(io.inugami.events._inner.inputs,listenerKeys)){
                io.inugami.events.fireEvent(name,null);
             }
          }
        },

        _keyHandlerMatches : function(inputs, ref){
          var result = isNotNull(inputs) && isNotNull(ref) && inputs.length>=ref.length;
          if(result){
             var inputCursor = inputs.length;

             for(var i=ref.length-1; i>=0; i--){
                inputCursor = inputCursor-1;
                result = inputs[inputCursor]==ref[i];
                if(!result){
                  break;
                }
             }
          }

          return result;
        }
}


$(window ).resize(function() {
  io.inugami.events.updateResize();
});

$(document).on('keydown', function ( e ) {
  io.inugami.events._keyHandler(e);
});

setInterval(function(){
  io.inugami.events.fireEvent(io.inugami.events.type.EVERY_SECOND,{});
  var now = new Date();
  if(now.getSeconds() == 0){
    io.inugami.events.fireEvent(io.inugami.events.type.EVERY_PLAIN_MINUTE,{});  
  }
  if(now.getSeconds() == 0 && now.getMinutes() == 0){
    io.inugami.events.fireEvent(io.inugami.events.type.EVERY_PLAIN_HOUR,{});  
  }
  if(now.getSeconds() == 0 && now.getMinutes() == 0 && now.getHours()==0){
    io.inugami.events.fireEvent(io.inugami.events.type.EVERY_PLAIN_DAY,{});  
  }
}, 1000) ;

setInterval(function(){
  io.inugami.events.fireEvent(io.inugami.events.type.EVERY_MINUTE,{});
}, 60000) ;


io.inugami.events.addEventListener("konami", function(event){
  console.log("konami !!!");
});
io.inugami.events.addEventListener("hadoken", function(event){
  console.log("haaaadookennnnnn ))))=================================");
});