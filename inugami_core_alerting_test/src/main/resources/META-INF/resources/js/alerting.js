
/**
 * Simple alerting example
 */
function simpleAlerting(gav, event, data){
    var value = lastGraphiteValue(data);
    var result = null;

    if(value!=null){
        if(value>10){
            result = org.inugami.builders.alertError("error message");
        }else if(value>5){
            result = org.inugami.builders.alertWarn("warn message");
        }
    }

    if(result!=null){
        result.data.service="my-service";
        result.data.nominal="2";
        result.data.unit="%"
        result.data.currentValue=value;
        result.duration=300;      

        console.log("[my-service]"+ JSON.stringify(result));    
    }
    
    return result;
}