//##############################################################################
//
//						MOCK INUGAMI COMMONS API
//
//##############################################################################


/*******************************************************************************
 *  TOOLS
 ******************************************************************************/
var checks = {
	isArray(value){
		return value== undefined || value==null? false : Object.prototype.toString.call(value) === '[object Array]';
	}
}
String.prototype.format = function() {
    var formatted = this;
    for (var i = 0; i < arguments.length; i++) {
        var regexp = new RegExp('\\{'+i+'\\}', 'gi');
        formatted = formatted.replace(regexp, arguments[i]);
    }
    return formatted;
};

/*******************************************************************************
 *  HTTP API
 ******************************************************************************/
var httpMock = {
	_innerData : {},
	verbe:{
		GET:"GET", POST:"POST",PUT:"PUT",DELETE:"DELETE"
	},
	addGet : function(data, url, header, json){
		httpMock.add(httpMock.verbe.GET,data, url, header, json);
	},
	addPost : function(data, url, header, json){
		httpMock.add(httpMock.verbe.POST,data, url, header, json);
	},
	addPut : function(data, url, header, json){
		httpMock.add(httpMock.verbe.PUT,data, url, header, json);
	},
	addDelete : function(data, url, header, json){
		httpMock.add(httpMock.verbe.DELETE,data, url, header, json);
	},
	add : function(verbe, data, url, header, json){
		var hash = httpMock._buildKey(verbe, url, header, json);
		httpMock._innerData[hash] = {
			"data":data,
			"url":url,
			"header":header,
			"json":json
		};
	},
	buildData: function(data, httpCode,message,responseAt,delais){
		var status          = httpCode==null?200:httpCode;
		var localResponseAt = responseAt==null? Date.now():responseAt;
		var localDelais     = delais==null ||isNaN(delais) ? 0 : delais;
		return {
			"status"     : status,
			"message"    : message,
			"data"       : data,
			"responseAt" : localResponseAt,
			"delais"	 : localDelais
		}
	},
	_buildKey : function(httpVerbe, url, header, json){
		var data = {"verbe":httpVerbe, "url":url};
		if(header!=undefined && header != null){
			data["header"]=header;
		}
		if(json!=undefined && json != null){
			data["json"]=json;
		}

		var jsonData = JSON.stringify(data);
		var result = SHA1(jsonData);
		console.log(JSON.stringify({"hash":result, "data":jsonData}));
		return result;
	},
	_grab: function(verbe, url, header,json){
		var hash = httpMock._buildKey(verbe, url, header, json);
		var data = httpMock._innerData[hash];
		return data==null?null: data.data;
	}
}

var http = {
	get : function(url,header){
		return httpMock._grab(httpMock.verbe.GET,url,header);
	},
	post : function(url,json,header){
		return httpMock._grab(httpMock.verbe.POST,url,header,json);
	}
}


/*******************************************************************************
 *  GRAPHITE
 ******************************************************************************/
function lastGraphiteValue(data){
	var result = null;
	if(data==null){
		return result;
	}

	if(data.length==1){
		result = mockInugami.data.graphite.extractLastTargetValue(data[0]);
	}
	else if(data.length >1){
		result={};
		for(var i=0; i<data.length; i++){
			result[data[i].target]=mockInugami.data.graphite.extractLastTargetValue(data[i]);
		}
	}

	return result;
}


//##############################################################################
//
//						MOCK INUGAMI CORE API
//
//##############################################################################
var org    = {};
    io.inugami = {};



/*******************************************************************************
 *  io.inugami.builders 
 ******************************************************************************/
var mockInugami = {
	alertTypes : {
		FATAL   :{level:1000000,regex:".*fatal.*"},
		ERROR   :{level: 100000,regex:".*error.*"},
		WARN    :{level:  10000,regex:".*warn.*"},
		INFO    :{level:   1000,regex:".*info.*"},
		DEBUG   :{level:    100,regex:".*debug.*"},
		TRACE   :{level:     10,regex:".*trace.*"},
		OFF     :{level:      0,regex:"^OFF$"},
		UNDEFINE:{level:      1, name:"UNDEFINE"}
	},
	alert : function(data){
		if(data ==null){
			return null;
		}
		var levelType = mockInugami.getAlertType(data.level);
		var created   = data.created == null ? Date.now() : data.created;
		var duration  = data.duration==null?60:data.duration;
		var channel   = data.channel==null?"@all":data.channel;
		var detail    = data.data==null?{}:data.data;
		return {
			"alerteName"  : data.alerteName,
			"level"       : data.level,
			"levelType"   : levelType.name,
			"levelNumber" : levelType.level,
			"message"     : data.message,
			"subLabel"    : data.subLabel,
			"created"     : created,
			"duration"    : duration,
			"data"        : detail,
			"channel"     : channel
		};
	},
	getAlertType : function(level){
		var levelInt  = mockInugami.alertTypes.UNDEFINE.level;
		var levelName = mockInugami.alertTypes.UNDEFINE.name;

		if(level!=null){
			var keys = Object.keys(mockInugami.alertTypes);
			var type = null;
			for(var i=0;i<keys.length; i++){
				var typeName = keys[i];
				type = mockInugami.alertTypes[typeName];
				if("UNDEFINE"!=typeName){
					var regEx = new RegExp(type.regex);
					if(level.match(regEx)){
						levelInt  = type.level;
						levelName = typeName;
						break;
					}
				}
			}
		}
		return {
			"level":levelInt,
			"name" :levelName
		};
	},

	_buildAlerting(data){
		var alertings = null;
		if(data!=null && checks.isArray(data)){
			alertings = [];
			for(var i=0; i< data.length; i++){
				var alert = data[i];
				alertings.push({
					"name"      : alert.name,
					"provider"  : alert.provider,
					"level"     : alert.level,
					"message"   : alert.message,
					"function"  :  alert.function,
					"condition" :  alert.condition
				});	
			}
		}
		return alertings;
	},
	_buildProcessor(data){
		var processors = null;
		if(data!=null && checks.isArray(data)){
			processors = [];
			for(var i=0; i< data.length; i++){
				var proc = data[i];
				processors.push({
					"name"      : proc.name,
					"className" : proc.provider,
					"configs"   : proc.level,
					"manifest"  : proc.message
				});	
			}
		}
		return processors;
	},

	buildSimpleEvent : function(data){
		if(data==null){
			return null;
		}
		var alertings = mockInugami._buildAlerting(data.alertings);
		var processors = mockInugami._buildAlerting(data.processors);

		return {
			"name"       : data.name,
			"from"       : data.from,
			"until"      : data.until,
			"provider"   : data.provider,
			"mapper"     : data.mapper,
			"query"      : data.query,
			"scheduler"  : data.scheduler,
			"parent"     : data.parent,
			"alertings"  : alertings,
			"processors" : processors,
		}
	},
	event : function(data){
		if(data==null){
			return null;
		}
		var alertings  = mockInugami._buildAlerting(data.alertings);
		var processors = mockInugami._buildAlerting(data.processors);
		var targets    = null;

		if(data.targets!=null && checks.isArray(data.targets)){
			if(data.targets!=null && checks.isArray(data.targets)){
				targets = [];
				for(var i=0; i< data.targets.length; i++){
					targets.push(mockInugami.buildSimpleEvent(data.targets[i]));
				}
			}
		}

		return {
			"name"       : data.name,
			"from"       : data.from,
			"until"      : data.until,
			"provider"   : data.provider,
			"mapper"     : data.mapper,
			"scheduler"  : data.scheduler,
			"parent"     : data.parent,
			"alertings"  : alertings,
			"processors" : processors,
			"targets"    : targets
		}
	},

	buildGav : function(groupeId, artifactId, version){
		return {
			"groupId":groupeId,
			"groupId":artifactId,
			"groupId":version
		}
	}
}

/*******************************************************************************
 *  mockInugami.data
 ******************************************************************************/
mockInugami.data = {
	providerFutureResult : function(data){
		if(data==null){
			return null;
		}
		var alerts = null;
		if(data.alerts!=null && checks.isArray(data.alerts)){
			alerts = [];
			for(var i=0; i< data.alerts.length; i++){
				alerts.push(mockInugami.alert(data.alerts[i]));
			}
		}

		return {
			"message"  : data.message,
			"exception": data.exception,
			"scheduler": data.scheduler,
			"data"     : data.data,
			"event"    : data.event,
			"channel"  : data.channel,
			"fieldData": data.message,
			"alerts"   : alerts
		}
	},
	graphite : {
		_datapoint : function(data){
			if(data==null){
				return null;
			}
			var timestamp = data.timestamp==null?Date.now():data.timestamp;
			return {
				"value"     : data.value,
				"timestamp" : timestamp
			}
		},
		_target : function(data){
			if(data==null){
				return null;
			}
			var datapoints = null;
			if(data.datapoints != null && checks.isArray(data.datapoints)){
				datapoints = [];
				for(var i=0; i< data.datapoints.length; i++){
					var point = mockInugami.data.graphite._datapoint(data.datapoints[i]);
					if(point!=null){
						datapoints.push(point);
					}	
				}
			}
			return {
				"target"     : data.target,
				"datapoints" : datapoints
			}
		},
		extractLastTargetValue : function(target){
			var result = null;
			if(target!=null && target.datapoints!=null){
				for(var i=target.datapoints.length-1; i>=0; i--){
					var point = target.datapoints[i];
					if(point!=null && point.value!=null){
						result = point.value;
						break;
					}
				}
			}
			return result;
		},
		simpleData : function(value){
			return mockInugami.data.graphite.simpleTarget({
						"target"     : "target",
						"datapoints" : [{"value":value}]
			});
		},
		simpleTarget : function(data){
			if(data==null){
				return null;
			}
			var target = mockInugami.data.graphite._target(data);
			var result = target==null?null: [target];

			return result;
		},
		multiTargets : function(data){
			if(data==null || !checks.isArray(data)){
				return null;
			}

			var result = [];
			for(var i=0; i< data.length; i++){
				var target = mockInugami.data.graphite._target(data[i]);
				if(target!=null){
					result.push(target);
				}
			}
			return result;
		}
	}
}

/*******************************************************************************
 *  io.inugami.builders.data
 ******************************************************************************/
io.inugami.builders = {
	alertTrace : function(message, data){
		return io.inugami.builders.alert("trace",message, data);
	},
	alertDebug : function(message, data){
		return io.inugami.builders.alert("debug",message, data);
	},  
	alertInfo : function(message, data){
		return io.inugami.builders.alert("info",message, data);
	},
	alertWarn : function(message, data){
		return io.inugami.builders.alert("warn",message, data);
	},
	alertError : function(message, data){
		return io.inugami.builders.alert("error",message, data);
	},
	alertFatal : function(message, data){
		return io.inugami.builders.alert("fatal",message, data);
	},
	alert : function(level, message, data){
		return mockInugami.alert({
			"level"   : level,
			"message" : message,
			"data"    : data
		});
	},
	buildSimpleEvent : function(name,provider,query,from,until ){
		return io.inugami.builders.simpleEvent({
			"name"       : name,
			"from"       : from,
			"until"      : until,
			"provider"   : { get : function(){return provider},
							 orElse : function(name){return name}},
			"query"      : query
		});
	},
	buildEvent : function(name,provider,from,until){
		return io.inugami.builders.event({
			"name"       : name,
			"from"       : from,
			"until"      : until,
			"provider"   : { get : function(){return provider},
							 orElse : function(name){return name}},
		});
	},
	simpleEvent : function(data){
		return mockInugami.buildSimpleEvent(data);
	},
	event : function(data){
		return mockInugami.event(data);
	}
};




/*******************************************************************************
* 
*******************************************************************************/
io.inugami.alerts = {
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
