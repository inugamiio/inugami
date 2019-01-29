//=========================================================================
// INUGAMI API : NAMESPACES
//=========================================================================
var _inugamiApi = Java.type('org.inugami.core.context.scripts.JavaScriptApi');

var org    = {};
    org.inugami = {};

    
    
//=========================================================================
//INUGAMI API : CONFIG
//=========================================================================
org.inugami.builders = {
	alert : function(level, message, data){
		return _inugamiApi.buildAlert(level,message, data);
	},
	alertTrace : function(message, data){
		return _inugamiApi.buildAlert("trace",message, data);
	},
	alertDebug : function(message, data){
		return _inugamiApi.buildAlert("debug",message, data);
	},  
	alertInfo : function(message, data){
		return _inugamiApi.buildAlert("info",message, data);
	},
	alertWarn : function(message, data){
		return _inugamiApi.buildAlert("warn",message, data);
	},
	alertError : function(message, data){
		return _inugamiApi.buildAlert("error",message, data);
	},
	alertFatal : function(message, data){
		return _inugamiApi.buildAlert("fatal",message, data);
	},
	buildSimpleEvent : function(name,provider,query,from,until ){
		return org.inugami.builders.simpleEvent({
			"name"       : name,
			"from"       : from,
			"until"      : until,
			"provider"   : provider,
			"query"      : query
		});
	},
	buildEvent : function(name,provider,from,until){
		return org.inugami.builders.event({
			"name"       : name,
			"from"       : from,
			"until"      : until,
			"provider"   : provider
		});
	},
	simpleEvent : function(data){
		return _inugamiApi.buildSimpleEvent(data);
	},
	event : function(data){
		return _inugamiApi.buildEvent(data);
	}
};

//=========================================================================
//INUGAMI API : CONFIG
//=========================================================================
org.inugami.config = {
	/**
	* Allow to grab global property form inugami Context
	*/
	global : function(propertyName){
		return _inugamiApi.global(propertyName);
	},	
	
	/**
	* Allow to grab provider configuration form inugami Context
	*/
	providerConfig : function(providerName, configName){
		return _inugamiApi.providerConfig(providerName, configName);
	}		
}



//=========================================================================
//INUGAMI API : PROVIDER 
//=========================================================================
org.inugami.provider = {
		
		callGraphiteProvider : function(providerName, query, from, until){
			return _inugamiApi.callGraphiteProvider(providerName, query, from, until);
		},
		extractGraphiteQuery : function(event){
			return _inugamiApi.extractGraphiteQuery(event);
		}
		
}

//=========================================================================
//INUGAMI API : ALERTS
//=========================================================================
org.inugami.alerts = {
		levels : {
			FATAL    : 1000000,
		    ERROR    :  100000,
		    WARN     :   10000,
		    INFO     :    1000,
		    DEBUG    :     100,
		    TRACE    :      10,
		    OFF      :       0,
		    UNDEFINE :       1
		}
}