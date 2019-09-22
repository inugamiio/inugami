// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// CORE SERVICE
// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
org.inugami.sse = {
	httpConnector   : null,
	enableLogger 	: true,
	filterEvent 	: null,
	alertsHandler	: null,
	userToken 		: null,
	state 			: null,
	nbRetry         : 0,
	times			: {
		MIN           :   60000,
		TWO_MIN       :  120000,
		TEN_MIN       :  600000,
		HOUR          : 3600000,
		maxUnactivity :  900000,
		reboot		  : {
			hour  : 4,
			min   : 30
		}
	},
	lastIncommingData : -1,
	events 			: {
		OPEN : "sseOpen",
		ERROR : "sseError",
		UPDATE : "sseUpdate",
		ALREADY_OPEN : "sseAlreadyOpen",
		OPEN_OR_ALREADY_OPEN : "sseOpenOrAlreadyOpen",
		FORCE_REFRESH : "forceRefresh",
		ALERTS:"alerts"
	},
	_inner_data : {
		connectInProgress    : false,
		components_registred : {},
		components : [],
		/** channels : [name: {}]**/
		channels     : {},
		eventSources : null,
		logger : org.inugami.logger.factory("org.inugami.sse")
	},

	_inner_handler : {
		dispatcher : function(event) {
			org.inugami.events.fireEvent(org.inugami.sse.events.OPEN_OR_ALREADY_OPEN);
			org.inugami.sse.lastIncommingData = Date.now();

			if (org.inugami.sse.enableLogger) {
				org.inugami.sse._inner_data.logger.debug("recive event");
			}

			

			if (isNotNull(event)) {

				var json = null;
				try {
					json = JSON.parse(event.data);
				} catch (except) {
					console.warn(except);
				}

				if (org.inugami.sse.enableLogger) {
					org.inugami.sse._inner_data.logger.debug("process event : {0}", [ event.data ]);
				}

				if (isNull(json)) {
					org.inugami.sse._inner_data.logger.warn("can't parse {0}", [ event.data ]);
				} else {
					if(json.mutliEvent !=undefined && json.mutliEvent){
						for(var i=json.events.length-1; i>=0; i--){
							var currentEvent   = json.events[i];
							var isGlobale      = "globale"==currentEvent.channel;
							var isAdmin        = "administration"==currentEvent.channel;
							var isAlert        = "alert"==currentEvent.name;
							var isAlertControl = "alert-control" == currentEvent.name;
							var filtered       = isNull(org.inugami.sse.filterEvent)?true:org.inugami.sse.filterEvent(currentEvent.name);

							if(isAlertControl){
								org.inugami.events.fireEvent(currentEvent.name, currentEvent);
							}
							else if(filtered || isGlobale || isAdmin||isAlert){
								org.inugami.sse._inner_handler.dispatcherProcess(currentEvent);
							} 						
						}
					}

				}

			}
		},
		dispatcherProcess : function(event) {
			var fullEventName = (isNull(event.channel)?'':event.channel)+'_'+event.name;
			org.inugami.sse._inner_data.logger.debug("dispatche event : {0}",[fullEventName]);
			org.inugami.events.fireEvent(fullEventName, event);

			if(isNotNull(org.inugami.sse.alertsHandler) && isNotNull(event.data) &&  isNotNull(event.data.alerts) ){
				org.inugami.sse._inner_handler.cleanAlertsData(event);

				if(isNotNull(org.inugami.sse.alertsHandler)){
					org.inugami.sse.alertsHandler(event);
				}
				org.inugami.events.fireEvent(org.inugami.sse.events.ALERTS, event);
			}

			org.inugami.initialize.initContext();
			if ("refresh" === event.name && !org.inugami.values.context.URL.endsWith("/admin")) {
				org.inugami.sse.forceRefresh();
			} else {
				org.inugami.events.fireEvent(org.inugami.sse.events.UPDATE, event);
			}
			
		},

		cleanAlertsData : function (event){
			if(isNotNull(event) && isNotNull(event.data) && isNotNull(event.data.alerts)){
				for(var i=0; i<event.data.alerts.length; i++){
					if(isNotNull(event.data.alerts[i].data)){
						var type =  typeof event.data.alerts[i].data;
						if(type ==  "string"){
							var json = event.data.alerts[i].data;
							event.data.alerts[i].data = JSON.parse(json);
						}
					}
				}
			}
		},

		open : function(event) {
			org.inugami.sse.state="open";
			org.inugami.sse._inner_data.logger.info("event source opened");
			org.inugami.events.fireEvent(org.inugami.sse.events.OPEN);
			org.inugami.events.fireEvent(org.inugami.sse.events.OPEN_OR_ALREADY_OPEN);	
		},
		error : function(event) {
			org.inugami.sse.state="close";
			org.inugami.sse.closeSocket();
			console.error("SSE error : "+JSON.stringify(event));
			org.inugami.events.fireEvent(org.inugami.sse.events.ERROR, event);
			setTimeout(org.inugami.sse.reconnect._forceReconnect, 5000);
		}
	},

	/**
	 * Allow to force refresh current page
	 */
	forceRefresh : function() {
		org.inugami.sse.closeSocket();
		$(location).attr('href', window.location.href);
	},

	register : function(pluginName, filterHandler, alertsHandler){
		org.inugami.sse.connect(pluginName);

		if(isNotNull(filterHandler)){
			org.inugami.sse.filterEvent =filterHandler;
		}

		if(isNotNull(alertsHandler)){
			org.inugami.sse.alertsHandler = alertsHandler;
		}
	},


	/**
	 * Allow to connect to Server Send Event API.
	 * @param sourceUrl URL to server SSE API
	 * @param queueName name of SSE Queue, "sse" by default
	 */
	connect : function(channelName) {
		if (typeof (EventSource) !== "undefined") {
			org.inugami.sse.connectSSESocket();
			var sseChannel = isNull(channelName)?"sse":channelName;

			org.inugami.sse.registerChannelEventListener("globale");			
			org.inugami.sse.registerChannelEventListener(sseChannel);
			org.inugami.events.fireEventPlugin("globale","all-plugins-data");
		} else {
			org.inugami.sse._inner_data.logger.error("Your browser dosn't support server send event!");
		}
	},
	connectSSESocket : function(force) {
		if(org.inugami.sse.state!="open" || org.inugami.sse._inner_data.eventSources==null){
			var token     = localStorage.getItem(org.inugami.constants.token);
			var sourceUrl = CONTEXT_PATH+"rest/sse/register?token="+token+"&uuid="+org.inugami.constants.uuid;
			var socket    = null;
			try {
				var socket  = new EventSource(sourceUrl);
				socket.onmessage = org.inugami.sse._inner_handler.dispatcher;
				socket.onerror = org.inugami.sse._inner_handler.error;
	
				socket.onopen = function(event){
					org.inugami.sse._inner_handler.open(event);
				}
				org.inugami.sse._inner_data.eventSources = socket;
			} catch (e) {
				console.error(e);
			}
		}
	},

	registerChannelEventListener : function(name){
		org.inugami.sse._inner_data.eventSources.addEventListener(name, function(event) {
			org.inugami.sse._inner_handler.dispatcher(event);
		});
		org.inugami.sse._inner_data.channels[name] = {};
	},

	closeSocket : function(){
		if(isNotNull(org.inugami.sse._inner_data.eventSources)){
			try{
				org.inugami.sse._inner_data.eventSources.close();
				org.inugami.sse._inner_data.eventSources = null;
				org.inugami.constants.uuid=_buildUid();
			} catch (error) {
				console.error(error);
			}
		}
	}
	
}


org.inugami.events.addEventListener(org.inugami.sse.events.OPEN_OR_ALREADY_OPEN, function(event) {
	org.inugami.events.updateResize();
});

// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// RECONNECT
// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
org.inugami.sse.reconnect = {

	reboot : function(){
		var now = new Date();
		if(now.getHours() == org.inugami.sse.times.reboot.hour && now.getMinutes() == org.inugami.sse.times.reboot.min){	
			org.inugami.sse._httpConnector
						.get(CONTEXT_PATH+"rest/administration/version")
						.then(data =>{
							org.inugami.sse.forceRefresh();
			});
		}
	},
	fromLastIncommingData : function(){
		if(org.inugami.sse.lastIncommingData > 0){
			var diff = Date.now() - org.inugami.sse.lastIncommingData;
			if(diff > org.inugami.sse.times.maxUnactivity){
				org.inugami.sse.reconnect._forceReconnect();
			}
		}
	},

	fromCloseSocket : function(){
		if(isNull(org.inugami.sse._inner_data.eventSources)){
			org.inugami.sse.reconnect._forceReconnect();
		}else if(EventSource.CLOSED == org.inugami.sse._inner_data.eventSources.readyState){
			org.inugami.sse.reconnect._forceReconnect();
		}
	},

	_forceReconnect : function(){
		org.inugami.sse.closeSocket();
		org.inugami.sse.connectSSESocket(true);

		
		var channels = Object.keys(org.inugami.sse._inner_data.channels);
		for(var i=0; i<channels.length; i++){
			org.inugami.sse.registerChannelEventListener(channels[i]);
		}


		org.inugami.events.fireEventPlugin("globale","all-plugins-data");
		
	}
}




org.inugami.events.addEventListener(org.inugami.events.type.EVERY_PLAIN_MINUTE, function(event) {
	if (isNotNull(org.inugami.sse._inner_data.eventSources)) {
		org.inugami.sse.reconnect.reboot();
		org.inugami.sse.reconnect.fromLastIncommingData();
		org.inugami.sse.reconnect.fromCloseSocket();
	}
});

window.addEventListener("beforeunload", function(e){
	org.inugami.sse.closeSocket();
 });

