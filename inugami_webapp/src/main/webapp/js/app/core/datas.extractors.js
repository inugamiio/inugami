// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// data EXTRACTORS
// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
org.inugami.data = {};
org.inugami.data.validators={
    graphite:{
        simpleValue : function(target){
            var result = false;
            if(isNotNull(target) && !Array.isArray(target)){
               result =    isNotNull(target.target)
                        && isNotNull(target.datapoints)
                        && Array.isArray(target.datapoints)
            }
            return result;
        },

        isTimeValue : function(target){
            return isNotNull(target) && isNotNull(target.path) && isNotNull(target.value);
        }
    }
}
org.inugami.data.extractors = {
      _inner : {
          LOGGER : org.inugami.logger.factory("org.inugami.data.extractors")
      },
      graphite : {
          /**
          @see GraphiteDatapoint typescript class (js/app/models/graphite.datapoint.ts)
          @return  will return value struture like:
           <code>
           {
              timestamp : 1485291600,
              value : any
            }
           </code>
          **/
          simpleValue : function(target){
              return org.inugami.data.extractors.graphite.simpleValueFiltred(target, function(result){
                return isNotNull(result) && isNotNull(result.value);
              });
          }, 

          simpleValueFiltred : function(target, filter){
            var result = null;
            if(org.inugami.data.validators.graphite.simpleValue(target)){
                var size =target.datapoints.length;
                for(var index = size-1; index>=0; index--){
                    result = target.datapoints[index];
                    if(filter(result)){
                      break;
                    }
                }

            }else if(org.inugami.data.validators.graphite.isTimeValue(target)){
                result = {
                    "target":target.path,
                    "value":target.value,
                    "timestamp":target.time
                };
            }
            else{
               org.inugami.data.extractors._inner.LOGGER.error("error on validating graphite data : {0}",[target]);
            }
            return result;
          },

          sortTargets : function(data){
            var result = [];
            var map = {}
           
            if(isNotNull(data)){
                var size =data.length;
                for(var i=0; i<size; i++){
                    if(isNotNull(data[i])){
                        var key = isNull(data[i].target)?'unkown_'+i:data[i].target;
                        if(isNotNull(map[key])){
                            key = key+"_"+i;
                        }
                        
                        map[key] = data[i];
                    }
                }
            }

            var keys = Object.keys(map).sort();
            var keysSize = keys.length;
            for(i=0 ;i<keysSize; i++){
                result.push(map[keys[i]]);
            }
            
            return result;
          },

          cleanDatapoints : function(datapoints){
            var result = null;
            if(isNotNull(datapoints) && Array.isArray(datapoints)){
                result = [];
                var size = datapoints.length;
                for(var i=0; i<size; i++){
                    var data = datapoints[i];
                    if(isNotNull(data) && isNotNull(data.value)){
                        result.push(data);
                    }
                }
            }
            return result;
          }
      },


      style : {
        decomposeStyleClass: function(value, separator){
            var result = "";
            if(isNotNull(value)){ 
                if(isNotNull(separator)){
                    result = value.split(separator).join(" ");
                }else{
                    result = value;
                }
            }
            return result;
        }
      },

      calc :{
          simpleDiff : function(currentValue, previousValue){
            return previousValue - currentValue;
          }
      },

      alertMax : function(data){
        var result = null;
        if(isNotNull(data) && isNotNull(data.alerts)){
            var maxLevel = -1;
            for(var i=0; i< data.alerts.length; i++){
                var alert = data.alerts[i];
                if(isNull(result) || alert.levelNumber>maxLevel){
                    maxLevel = alert.levelNumber;
                    result   = alert;
                }
            }
        }
        return result;
      }
};

org.inugami.data.aggregators = {
    sum : function(values){
        var result = null;
        if(isNotNull(values) && values.length>0){
            result = 0.0;
            for(var i=values.length-1;i>=0;i--){
                result += values[i];
            }
        }
        return result;
    },
    
    avg : function(values){
        var result = null;
        var sum = org.inugami.data.aggregators.sum(values);
        if(sum!=null){
            result = sum / values.length;
        }
        return result;
    },

    min : function(values){
        return org.inugami.data.aggregators.percentil(values,0);
    },
    max : function(values){
        return org.inugami.data.aggregators.percentil(values,1);
    },
    p50 : function(values){
        return org.inugami.data.aggregators.percentil(values,0.5);
    },
    p70 : function(values){
        return org.inugami.data.aggregators.percentil(values,0.7);
    },
    p90 : function(values){
        return org.inugami.data.aggregators.percentil(values,0.9);
    },
    p95 : function(values){
        return org.inugami.data.aggregators.percentil(values,0.95);
    },
    p99 : function(values){
        return org.inugami.data.aggregators.percentil(values,0.99);
    },

    percentil :function(values, percentil){
        var result  = null;
        var ordered = null;
        var size =-1;
        if(isNotNull(values)){
            size = values.length;
        }

        if(size>0){
            ordered = values.sort();
        }

        if(isNotNull(ordered)){
            var index = Math.floor(size * percentil);
            if(index>=size){
                index = size-1;
            }
            result = ordered[index];
        }

        return result;
    }
}
