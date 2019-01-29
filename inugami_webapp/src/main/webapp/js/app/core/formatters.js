// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// formatters API
// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
org.inugami.formatters = {
    selectFormatter : function(formatter, defaultFormatter){
        var result = defaultFormatter;
        if(isNotNull(formatter) && typeof formatter === "function"){
          result = formatter;
        }
        return result;
    },

    extractJiraName: function (jiraUrl) {
        var result = "";
        if(org.inugami.checks.isNotNull(jiraUrl)){
            var urlParts = jiraUrl.split("/");
            result = urlParts[urlParts.length-1];
        }
        return result;
    },
    hour : function(value, delimiter){
        var delimiterToUse = delimiter;
        if(delimiter===undefined){
            delimiterToUse = "h";
        }

        var val = "";
        var size = 4 - ( ""+value).length;
        for(var i =0;i<size;i++){
            val += "0";
        }
        val += value;

        return val.substring(0,2)+delimiterToUse+val.substring(2);
    },

    noFormatNumber: function(value){
        return value;
    },
    truncateNumberOfMaxValue: function(value,nbFloatDigit, maxValue, resultMax){
        var result = "";
        if(value<maxValue){
            result = org.inugami.formatters.truncateNumber(value,nbFloatDigit);
        }else{
            result = resultMax;
        }
        return result;
    },
    truncateNumber: function(value,nbFloatDigit){
        var nbDigit     = isNull(nbFloatDigit)?0:nbFloatDigit;
        
        var unit        = "";
        var sign        = "";
         
        if(value<0){
            value = -value;
            sign= '-';
        }
        var reduceValue = value;

        if(isNotNull(value)){
            if(value>=1000 && value<1000000){
                reduceValue= value/1000;
                unit = "K";
            }
            else if(value>=1000000 && value<1000000000){
                reduceValue= value/1000000;
                unit = "M";
            }
            else if(value>=1000000000 && value<1000000000000){
                reduceValue= value/1000000000;
                unit = "G";
            }
            else if(value>=1000000000000){
                reduceValue= value/1000000000000;
                unit = "T";
            }
        }

        return [sign,reduceValue.toFixed(nbDigit),unit].join("");
    },


    truncate : function(str, maxLength, suffix) {
        if(str.length > maxLength) {
            str = str.substring(0, maxLength + 1);
            str = str.substring(0, Math.min(str.length, str.lastIndexOf(" ")));
            str = str + suffix;
        }
        return str;
    },
    defaultTrendFormat : function(value,oldValue,maxInt){
    	var trend = [];
    	var percent =0;

    	if(isNotNull(oldValue) && oldValue!==0){
    		var diff = value - oldValue;
    		percent = Math.round((diff / oldValue)* 100);

    		if (percent > 0) {
    			trend.push("+");
    		}

    		if(isNotNull(maxInt) && percent<maxInt){
    			trend.push(percent);
    		}else{
    			trend.push(org.inugami.formatters.truncateNumber(percent));
    		}

    		trend.push("%");
    	}

  		return {
  			trend : trend.join(""),
  			trendClass : (percent > 0 ? "positif" : "negatif")
  		}
    },

    number : function(value, nbDigit, digit){
    	var result = [];

    	var str = ""+value;
    	var diff = nbDigit -str.length;
    	if(diff>0){
    		result.push(isNull(digit)?'0':digit);
    	}
    	result.push(str);

    	return result.join("");
    },

    timestampToDate : function(value){
      return org.inugami.formatters.timestampToTimeFormat(value,"YYYY-MM-DD HH:mm");
    },

    timestampToDateTime: function(value){
      return org.inugami.formatters.timestampToTimeFormat(value,"YYYY-MM-DD HH:mm:ss");
    },

    timestampToHour: function(value){
        return org.inugami.formatters.timestampToTimeFormat(value,"HH:mm");
      },

    timestampToTimeFormat: function(value, timeFormat){
      var result = "";
      if(isNotNull(value) && !isNaN(value)){
         result = moment.unix(value).format(timeFormat);
      }
      return result;
    },

    time : {
        simpleTimeMin : function(value){
           var nbDay = Math.floor(value / (3600*24));
           var deltaForHour = value-(nbDay*(3600*24));

           var nbHour      = Math.floor(deltaForHour / 3600);
           var deltaForMin = deltaForHour-(nbHour*3600);
           var nbMin       = Math.floor(deltaForMin/60);



           var result = [];
               result.push(""+(nbHour<10?"0"+nbHour:nbHour));
               result.push(""+(nbMin<10?"0"+nbMin:nbMin));
           return result.join(':');
        }
    },
    messageValue:  function(key, values){
      var result =null;
      if(isNotNull(MESSAGES)){

          if(isNotNull(MESSAGES[org.inugami.constants.context.LOCALE])){
            var bundle = MESSAGES[org.inugami.constants.context.LOCALE];
            result = bundle[key];
          }

          if(isNull(result)){
              result = MESSAGES["default"][key];
          }
      }
      if(isNotNull(result) && isNotNull(values)){
        result = org.inugami.formatters.format(result,values);
      }
      return result;
    },

    message :  function(key, values){
      var result = org.inugami.formatters.messageValue(key, values);
      if(isNull(result)){
        result = "??"+key+"??";
      }
      return result;
    },

    messages : function(baseKey, values){
        var result       = {};
        var properties   = {};

        var propsLocal   = org.inugami.formatters._extractKeysValuesFromBundle(baseKey,MESSAGES[org.inugami.constants.context.LOCALE]);
        var propsDefault = org.inugami.formatters._extractKeysValuesFromBundle(baseKey,MESSAGES["default"]);

        var keys = Object.keys(propsLocal);
        for(var i=0;i<keys.length; i++){
            var value =propsLocal[keys[i]];
            result[keys[i]]=org.inugami.formatters.format(value, values);
        }

        keys = Object.keys(propsDefault);
        for(var i=0;i<keys.length; i++){
            var value =propsDefault[keys[i]];
            result[keys[i]]=org.inugami.formatters.format(value, values);
        }

        return result;
    },
    
    _extractKeysValuesFromBundle : function(baseKey,bundle){
        var result       = {};
        var regex = new RegExp(["^",baseKey,"[.][^.]+$"].join(""));
        if(isNotNull(bundle)){
            var keys = Object.keys(bundle);
            for(var i=0; i<keys.length; i++){
                var key = keys[i];
                if(key.match(regex)){
                    result[key]=bundle[key];
                }
            }
        }
        return result;
    },
    format(message,values){
      var formatted = message;
      if(isNotNull(values)){
        for (var i = 0; i < values.length; i++) {
            var regexp = new RegExp('\\{'+i+'\\}', 'gi');
            formatted = formatted.replace(regexp, values[i]);
          }
      }
      return formatted;
    }
};
