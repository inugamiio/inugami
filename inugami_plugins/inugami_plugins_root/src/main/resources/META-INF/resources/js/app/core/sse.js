// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// CORE SERVICE
// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
io.inugami.sse = {
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
		logger : io.inugami.logger.factory("io.inugami.sse")
	},

	_inner_handler : {
		dispatcher : function(event) {
			io.inugami.events.fireEvent(io.inugami.sse.events.OPEN_OR_ALREADY_OPEN);
			io.inugami.sse.lastIncommingData = Date.now();

			if (io.inugami.sse.enableLogger) {
				io.inugami.sse._inner_data.logger.debug("recive event");
			}

			

			if (isNotNull(event)) {

				var json = null;
				try {
					json = JSON.parse(event.data);
				} catch (except) {
					console.warn(except);
				}

				if (io.inugami.sse.enableLogger) {
					io.inugami.sse._inner_data.logger.debug("process event : {0}", [ event.data ]);
				}

				if (isNull(json)) {
					io.inugami.sse._inner_data.logger.warn("can't parse {0}", [ event.data ]);
				} else {
					if(json.mutliEvent !=undefined && json.mutliEvent){
						for(var i=json.events.length-1; i>=0; i--){
							var currentEvent   = json.events[i];
							var isGlobale      = "globale"==currentEvent.channel;
							var isAdmin        = "administration"==currentEvent.channel;
							var isAlert        = "alert"==currentEvent.name;
							var isAlertControl = "alert-control" == currentEvent.name;
							var filtered       = isNull(io.inugami.sse.filterEvent)?true:io.inugami.sse.filterEvent(currentEvent.name);

							if(isAlertControl){
								io.inugami.events.fireEvent(currentEvent.name, currentEvent);
							}
							else if(filtered || isGlobale || isAdmin||isAlert){
								io.inugami.sse._inner_handler.dispatcherProcess(currentEvent);
							} 						
						}
					}

				}

			}
		},
		dispatcherProcess : function(event) {
			var fullEventName = (isNull(event.channel)?'':event.channel)+'_'+event.name;
			io.inugami.sse._inner_data.logger.debug("dispatche event : {0}",[fullEventName]);
			io.inugami.events.fireEvent(fullEventName, event);

			if(isNotNull(io.inugami.sse.alertsHandler) && isNotNull(event.data) &&  isNotNull(event.data.alerts) ){
				io.inugami.sse._inner_handler.cleanAlertsData(event);

				if(isNotNull(io.inugami.sse.alertsHandler)){
					io.inugami.sse.alertsHandler(event);
				}
				io.inugami.events.fireEvent(io.inugami.sse.events.ALERTS, event);
			}

			io.inugami.initialize.initContext();
			if ("refresh" === event.name && !io.inugami.values.context.URL.endsWith("/admin")) {
				io.inugami.sse.forceRefresh();
			} else {
				io.inugami.events.fireEvent(io.inugami.sse.events.UPDATE, event);
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
			io.inugami.sse.state="open";
			io.inugami.sse._inner_data.logger.info("event source opened");
			io.inugami.events.fireEvent(io.inugami.sse.events.OPEN);
			io.inugami.events.fireEvent(io.inugami.sse.events.OPEN_OR_ALREADY_OPEN);	
		},
		error : function(event) {
			io.inugami.sse.state="close";
			io.inugami.sse.closeSocket();
			console.error("SSE error : "+JSON.stringify(event));
			io.inugami.events.fireEvent(io.inugami.sse.events.ERROR, event);
			setTimeout(io.inugami.sse.reconnect._forceReconnect, 5000);
		}
	},

	/**
	 * Allow to force refresh current page
	 */
	forceRefresh : function() {
		io.inugami.sse.closeSocket();
		$(location).attr('href', window.location.href);
	},

	register : function(pluginName, filterHandler, alertsHandler){
		io.inugami.sse.connect(pluginName);

		if(isNotNull(filterHandler)){
			io.inugami.sse.filterEvent =filterHandler;
		}

		if(isNotNull(alertsHandler)){
			io.inugami.sse.alertsHandler = alertsHandler;
		}
	},


	/**
	 * Allow to connect to Server Send Event API.
	 * @param sourceUrl URL to server SSE API
	 * @param queueName name of SSE Queue, "sse" by default
	 */
	connect : function(channelName) {
		if (typeof (EventSource) !== "undefined") {
			io.inugami.sse.connectSSESocket();
			var sseChannel = isNull(channelName)?"sse":channelName;

			io.inugami.sse.registerChannelEventListener("globale");			
			io.inugami.sse.registerChannelEventListener(sseChannel);
			io.inugami.events.fireEventPlugin("globale","all-plugins-data");
		} else {
			io.inugami.sse._inner_data.logger.error("Your browser dosn't support server send event!");
		}
	},
	connectSSESocket : function(force) {
		if(io.inugami.sse.state!="open" || io.inugami.sse._inner_data.eventSources==null){
			var token     = localStorage.getItem(io.inugami.constants.token);
			var sourceUrl = CONTEXT_PATH+"rest/sse/register?token="+token+"&uuid="+io.inugami.constants.uuid;
			var socket    = null;
			try {
				var socket  = new EventSource(sourceUrl);
				socket.onmessage = io.inugami.sse._inner_handler.dispatcher;
				socket.onerror = io.inugami.sse._inner_handler.error;
	
				socket.onopen = function(event){
					io.inugami.sse._inner_handler.open(event);
				}
				io.inugami.sse._inner_data.eventSources = socket;
			} catch (e) {
				console.error(e);
			}
		}
	},

	registerChannelEventListener : function(name){
		io.inugami.sse._inner_data.eventSources.addEventListener(name, function(event) {
			io.inugami.sse._inner_handler.dispatcher(event);
		});
		io.inugami.sse._inner_data.channels[name] = {};
	},

	closeSocket : function(){
		if(isNotNull(io.inugami.sse._inner_data.eventSources)){
			try{
				io.inugami.sse._inner_data.eventSources.close();
				io.inugami.sse._inner_data.eventSources = null;
				io.inugami.constants.uuid=_buildUid();
			} catch (error) {
				console.error(error);
			}
		}
	}
	
}


io.inugami.events.addEventListener(io.inugami.sse.events.OPEN_OR_ALREADY_OPEN, function(event) {
	io.inugami.events.updateResize();
});

// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// RECONNECT
// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
io.inugami.sse.reconnect = {

	reboot : function(){
		var now = new Date();
		if(now.getHours() == io.inugami.sse.times.reboot.hour && now.getMinutes() == io.inugami.sse.times.reboot.min){	
			io.inugami.sse._httpConnector
						.get(CONTEXT_PATH+"rest/administration/version")
						.then(data =>{
							io.inugami.sse.forceRefresh();
			});
		}
	},
	fromLastIncommingData : function(){
		if(io.inugami.sse.lastIncommingData > 0){
			var diff = Date.now() - io.inugami.sse.lastIncommingData;
			if(diff > io.inugami.sse.times.maxUnactivity){
				io.inugami.sse.reconnect._forceReconnect();
			}
		}
	},

	fromCloseSocket : function(){
		if(isNull(io.inugami.sse._inner_data.eventSources)){
			io.inugami.sse.reconnect._forceReconnect();
		}else if(EventSource.CLOSED == io.inugami.sse._inner_data.eventSources.readyState){
			io.inugami.sse.reconnect._forceReconnect();
		}
	},

	_forceReconnect : function(){
		io.inugami.sse.closeSocket();
		io.inugami.sse.connectSSESocket(true);

		
		var channels = Object.keys(io.inugami.sse._inner_data.channels);
		for(var i=0; i<channels.length; i++){
			io.inugami.sse.registerChannelEventListener(channels[i]);
		}


		io.inugami.events.fireEventPlugin("globale","all-plugins-data");
		
	}
}




io.inugami.events.addEventListener(io.inugami.events.type.EVERY_PLAIN_MINUTE, function(event) {
	if (isNotNull(io.inugami.sse._inner_data.eventSources)) {
		io.inugami.sse.reconnect.reboot();
		io.inugami.sse.reconnect.fromLastIncommingData();
		io.inugami.sse.reconnect.fromCloseSocket();
	}
});

window.addEventListener("beforeunload", function(e){
	io.inugami.sse.closeSocket();
 });

