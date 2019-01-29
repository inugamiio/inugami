//##############################################################################
//
//                         INITIALIZE MOCKS DATA
//
//##############################################################################
var basicUrl = "http://inugami.io";

httpMock.addGet(httpMock.buildData("Hello the world"),basicUrl);
httpMock.addGet(httpMock.buildData("ERR-500",500,"internal error"),basicUrl,{"x-must-fail":"fail"});

httpMock.addPost(httpMock.buildData({"obj":"my-object", "time":1}),basicUrl,  null,{"time":1});
httpMock.addPost(httpMock.buildData({"obj":"my-second-object", "time":2}),basicUrl, null,{"time":2});
httpMock.addPost(httpMock.buildData("ERR-500",500,"internal error"),basicUrl, null,{"time":3});



//##############################################################################
//
//		                        HTTP API
//
//##############################################################################

// -----------------------------------------------------------------------------
//  [inugami_core_alerting_test] SHA1 hash
// -----------------------------------------------------------------------------
QUnit.test("[inugami_core_alerting_test] SHA1 hash", function( assert ) {
    var hash1 = httpMock._buildKey("GET", basicUrl, null, null);
    var hash2 = httpMock._buildKey("GET", basicUrl, null, null);
    var hash3 = httpMock._buildKey("POST", basicUrl, null, null);
    var hash4 = httpMock._buildKey("POST", basicUrl, null, {"time":1});
    var hash5 = httpMock._buildKey("POST", basicUrl, null, {"time":2});
    var hash6 = httpMock._buildKey("POST", basicUrl, {"x-header":"header"}, {"time":2});
    
    
    assert.ok(hash1 , "[SHA1 has] hash1 not null -> ok" );
    assert.ok(hash2 , "[SHA1 has] hash2 not null -> ok" );
    assert.equal(hash1,hash2, "{0} - {1} -> equals -> ok".format(hash1,hash2));
    assert.notEqual(hash1,hash3, "{0} - {1} -> not equals -> ok".format(hash1,hash3));
    assert.notEqual(hash4,hash5, "{0} - {1} -> not equals -> ok".format(hash4,hash5));
    assert.notEqual(hash5,hash6, "{0} - {1} -> not equals -> ok".format(hash5,hash6));
});

// -----------------------------------------------------------------------------
//  [inugami_core_alerting_test] http get
// -----------------------------------------------------------------------------
QUnit.test("[inugami_core_alerting_test] http get", function( assert ) {
    var httpGetResult = http.get(basicUrl);
    assert.ok(httpGetResult           ,                     "[simple case] http get -> ok" );
    assert.equal(httpGetResult.status , 200               , "[simple case] http get status 200 -> ok" );
    assert.equal(httpGetResult.data   , "Hello the world" , "[simple case] http get data -> ok" );

    var httpGet2 = http.get(basicUrl, {"x-must-fail":"fail"});
    assert.ok(httpGet2            ,                        "[error case] http get -> ok" );
    assert.equal(httpGet2.status  , 500                  , "[error case] http get status 500 -> ok" );
    assert.equal(httpGet2.data    , "ERR-500"            , "[error case] http get data -> ok" );
    assert.equal(httpGet2.message , "internal error"     , "[error case] http get message -> ok" );
});

// -----------------------------------------------------------------------------
//  [inugami_core_alerting_test] http post
// -----------------------------------------------------------------------------
QUnit.test("[inugami_core_alerting_test] http post", function( assert ) {
    var httpPost1 = http.post(basicUrl,{"time":1});
    assert.ok(httpPost1             ,               "[first case] http post -> ok" );
    assert.equal(httpPost1.status   , 200         , "[first case] http post status 200 -> ok" );
    assert.equal(httpPost1.data.obj , "my-object" , "[first case] http post data.obj (my-object) -> ok" );
   
    var httpPost2 = http.post(basicUrl,{"time":2});
    assert.ok(httpPost2             ,               "[second case] http post -> ok" );
    assert.equal(httpPost2.status   , 200         , "[second case] http post status 200 -> ok" );
    assert.equal(httpPost2.data.obj , "my-second-object" , "[second case] http post data.obj (my-second-object) -> ok" );

    var httpPost3 = http.post(basicUrl,{"time":3});
    assert.ok(httpPost3             ,               "[third case] http post -> ok" );
    assert.equal(httpPost3.status   , 500         , "[third case] http post status 500 -> ok" );
    assert.equal(httpPost3.data , "ERR-500"       , "[third case] http post data (ERR-500) -> ok" );
});


//##############################################################################
//
//	                           BUILDERS API
//
//##############################################################################

// -----------------------------------------------------------------------------
//  [inugami_core_alerting_test] org.inugami.builders.alert
// -----------------------------------------------------------------------------
QUnit.test("[inugami_core_alerting_test] org.inugami.builders.alert", function( assert ) {

    var trace = org.inugami.builders.alertTrace("trace message",{"info":"trace data info"});
    var now = Date.now();

    assert.ok(trace ,                                    "[trace] alert build  -> ok" );
    assert.equal(trace.alerteName  , null              , "[trace] trace.alerteName = null  -> ok" );
    assert.equal(trace.level       , "trace"           , "[trace] trace.level = trace  -> ok" );
    assert.equal(trace.levelType   , "TRACE"           , "[trace] trace.type = TRACE  -> ok" );
    assert.equal(trace.levelNumber , 10                , "[trace] trace.levelNumber = 10  -> ok" );
    assert.equal(trace.message     , "trace message"   , "[trace] trace.message = 'trace message'  -> ok" );
    assert.equal(trace.created     , now               , "[trace] trace.created = now  -> ok" );
    assert.equal(trace.duration    , 60                , "[trace] trace.duration = 60  -> ok" );
    assert.ok(trace.data                               , "[trace] trace.data not null  -> ok" );
    assert.equal(trace.data.info   , "trace data info" , "[trace] trace.data.info = 'trace data info'  -> ok" );

    var debug = org.inugami.builders.alertDebug("debug message",{"info":"debug data info"});
    now = Date.now();
    assert.ok(debug ,                                    "[debug] alert build  -> ok" );
    assert.equal(debug.alerteName  , null              , "[debug] debug.alerteName = null  -> ok" );
    assert.equal(debug.level       , "debug"           , "[debug] debug.level = debug  -> ok" );
    assert.equal(debug.levelType   , "DEBUG"           , "[debug] debug.type = DEBUG  -> ok" );
    assert.equal(debug.levelNumber , 100               , "[debug] debug.levelNumber = 10  -> ok" );
    assert.equal(debug.message     , "debug message"   , "[debug] debug.message = 'debug message'  -> ok" );
    assert.equal(debug.created     , now               , "[debug] debug.created = now  -> ok" );
    assert.equal(debug.duration    , 60                , "[debug] debug.duration = 60  -> ok" );
    assert.ok(debug.data                               , "[debug] debug.data not null  -> ok" );
    assert.equal(debug.data.info   , "debug data info" , "[debug] debug.data.info = 'debug data info'  -> ok" );
    
    var info = org.inugami.builders.alertInfo("info message",{"info":"info data info"});
    now = Date.now();
    assert.ok(info ,                                    "[info] alert build  -> ok" );
    assert.equal(info.alerteName  , null              , "[info] info.alerteName = null  -> ok" );
    assert.equal(info.level       , "info"            , "[info] info.level = info  -> ok" );
    assert.equal(info.levelType   , "INFO"            , "[info] info.type = INFO  -> ok" );
    assert.equal(info.levelNumber , 1000              , "[info] info.levelNumber = 10  -> ok" );
    assert.equal(info.message     , "info message"    , "[info] info.message = 'info message'  -> ok" );
    assert.equal(info.created     , now               , "[info] info.created = now  -> ok" );
    assert.equal(info.duration    , 60                , "[info] info.duration = 60  -> ok" );
    assert.ok(info.data                               , "[info] info.data not null  -> ok" );
    assert.equal(info.data.info   , "info data info"  , "[info] info.data.info = 'info data info'  -> ok" );
    
    var warn = org.inugami.builders.alertWarn("warn message",{"info":"warn data info"});
    now = Date.now();
    assert.ok(warn ,                                    "[info] alert build  -> ok" );
    assert.equal(warn.alerteName  , null              , "[info] warn.alerteName = null  -> ok" );
    assert.equal(warn.level       , "warn"            , "[info] warn.level = warn  -> ok" );
    assert.equal(warn.levelType   , "WARN"            , "[info] warn.type = WARN  -> ok" );
    assert.equal(warn.levelNumber , 10000             , "[info] warn.levelNumber = 10  -> ok" );
    assert.equal(warn.message     , "warn message"    , "[info] warn.message = 'warn message'  -> ok" );
    assert.equal(warn.created     , now               , "[info] warn.created = now  -> ok" );
    assert.equal(warn.duration    , 60                , "[info] warn.duration = 60  -> ok" );
    assert.ok(warn.data                               , "[info] warn.data not null  -> ok" );
    assert.equal(warn.data.info   , "warn data info"  , "[info] warn.data.info = 'warn data info'  -> ok" );
    
    var error = org.inugami.builders.alertError("error message",{"info":"error data info"});
    now = Date.now();
    assert.ok(error ,                                    "[error] alert build  -> ok" );
    assert.equal(error.alerteName  , null              , "[error] error.alerteName = null  -> ok" );
    assert.equal(error.level       , "error"           , "[error] error.level = error  -> ok" );
    assert.equal(error.levelType   , "ERROR"           , "[error] error.type = ERROR  -> ok" );
    assert.equal(error.levelNumber , 100000            , "[error] error.levelNumber = 10  -> ok" );
    assert.equal(error.message     , "error message"   , "[error] error.message = 'error message'  -> ok" );
    assert.equal(error.created     , now               , "[error] error.created = now  -> ok" );
    assert.equal(error.duration    , 60                , "[error] error.duration = 60  -> ok" );
    assert.ok(error.data                               , "[error] error.data not null  -> ok" );
    assert.equal(error.data.info   , "error data info" , "[error] error.data.info = 'error data info'  -> ok" );
    
    var fatal = org.inugami.builders.alertFatal("fatal message",{"info":"fatal data info"});
    now = Date.now();
    assert.ok(fatal ,                                    "[fatal] alert build  -> ok" );
    assert.equal(fatal.alerteName  , null              , "[fatal] fatal.alerteName = null  -> ok" );
    assert.equal(fatal.level       , "fatal"           , "[fatal] fatal.level = fatal  -> ok" );
    assert.equal(fatal.levelType   , "FATAL"           , "[fatal] fatal.type = FATAL  -> ok" );
    assert.equal(fatal.levelNumber , 1000000           , "[fatal] fatal.levelNumber = 10  -> ok" );
    assert.equal(fatal.message     , "fatal message"   , "[fatal] fatal.message = 'fatal message'  -> ok" );
    assert.equal(fatal.created     , now               , "[fatal] fatal.created = now  -> ok" );
    assert.equal(fatal.duration    , 60                , "[fatal] fatal.duration = 60  -> ok" );
    assert.ok(fatal.data                               , "[fatal] fatal.data not null  -> ok" );
    assert.equal(fatal.data.info   , "fatal data info" , "[fatal] fatal.data.info = 'fatal data info'  -> ok" );

});


// -----------------------------------------------------------------------------
//  [inugami_core_alerting_test] org.inugami.builders.alert
// -----------------------------------------------------------------------------
QUnit.test("[inugami_core_alerting_test] org.inugami.builders.simpleEvent", function( assert ) {
    var simpleEvent = org.inugami.builders.buildSimpleEvent("simpleEvent");

    assert.ok(simpleEvent ,                               "[case-1] simpleEvent isn't null  -> ok" );
    assert.equal(simpleEvent.name      , "simpleEvent"  , "[case-1] simpleEvent.name = simpleEvent  -> ok" );
    assert.equal(simpleEvent.from      , null           , "[case-1] simpleEvent.from = null  -> ok" );
    assert.equal(simpleEvent.until     , null           , "[case-1] simpleEvent.until = null  -> ok" );
    assert.equal(simpleEvent.provider.get()  , null           , "[case-1] simpleEvent.provider = null  -> ok" );
    assert.equal(simpleEvent.mapper    , null           , "[case-1] simpleEvent.mapper = null  -> ok" );
    assert.equal(simpleEvent.parent    , null           , "[case-1] simpleEvent.parent = null  -> ok" );
    assert.equal(simpleEvent.scheduler , null           , "[case-1] simpleEvent.scheduler = null  -> ok" );
    assert.equal(simpleEvent.query     , null           , "[case-1] simpleEvent.query = null  -> ok" );
    assert.equal(simpleEvent.alertings , null           , "[case-1] simpleEvent.alertings = null  -> ok" );
    assert.equal(simpleEvent.processors, null           , "[case-1] simpleEvent.processors = null  -> ok" );

    var query =  "org.foo.bar.service.10min";
    simpleEvent = org.inugami.builders.buildSimpleEvent("simpleEvent", "my-provider", query,"-20min", "-10min");
    assert.ok(simpleEvent ,                               "[case-2] simpleEvent isn't null  -> ok" );
    assert.equal(simpleEvent.name      , "simpleEvent"  , "[case-2] simpleEvent.name = simpleEvent  -> ok" );
    assert.equal(simpleEvent.from      , "-20min"       , "[case-2] simpleEvent.from = null  -> ok" );
    assert.equal(simpleEvent.until     , "-10min"       , "[case-2] simpleEvent.until = null  -> ok" );
    assert.equal(simpleEvent.provider.get()  , "my-provider"  , "[case-2] simpleEvent.provider = null  -> ok" );
    assert.equal(simpleEvent.mapper    , null           , "[case-2] simpleEvent.mapper = null  -> ok" );
    assert.equal(simpleEvent.parent    , null           , "[case-2] simpleEvent.parent = null  -> ok" );
    assert.equal(simpleEvent.scheduler , null           , "[case-2] simpleEvent.scheduler = null  -> ok" );
    assert.equal(simpleEvent.query     , query          , "[case-2] simpleEvent.query = null  -> ok" );
    assert.equal(simpleEvent.alertings , null           , "[case-2] simpleEvent.alertings = null  -> ok" );
    assert.equal(simpleEvent.processors, null           , "[case-2] simpleEvent.processors = null  -> ok" );


    simpleEvent = org.inugami.builders.simpleEvent({
        "name"       :"eventName",
		"from"       : "-10min",
		"until"      : "-5min",
		"provider"   : "graphite.provider",
		"scheduler"  : "{{EVERY_MINUTE}}",
		"mapper"     : "spiMapper",
		"parent"     : "parent",
		"processors" : [
			{"name"  : "processorA"},
			{"name"  : "processorB"}
		],
		"query"		 : query,
		"alertings"  : [
			{
				"name"      : "alert-1",
				"provider"  : "default-alert-provider",
				"message"   : "alert message",
				"level"     : "warn",
				"condition" : "Date.now() %2 == 0"
			},
			{
				"name"     : "alert-2",
				"provider" : "default-alert-provider",
				"function" : "foo.alert.check"
			}
		]
    });
    assert.ok(simpleEvent ,                                       "[case-3] simpleEvent isn't null  -> ok" );
    assert.equal(simpleEvent.name      , "eventName"            , "[case-3] simpleEvent.name = simpleEvent  -> ok" );
    assert.equal(simpleEvent.from      , "-10min"               , "[case-3] simpleEvent.from = null  -> ok" );
    assert.equal(simpleEvent.until     , "-5min"                , "[case-3] simpleEvent.until = null  -> ok" );
    assert.equal(simpleEvent.provider  , "graphite.provider"    , "[case-3] simpleEvent.provider = null  -> ok" );
    assert.equal(simpleEvent.mapper    , "spiMapper"            , "[case-3] simpleEvent.mapper = null  -> ok" );
    assert.equal(simpleEvent.parent    , "parent"               , "[case-3] simpleEvent.parent = null  -> ok" );
    assert.equal(simpleEvent.scheduler , "{{EVERY_MINUTE}}"     , "[case-3] simpleEvent.scheduler = null  -> ok" );
    assert.equal(simpleEvent.query     , query                  , "[case-3] simpleEvent.query = null  -> ok" );

    assert.equal(simpleEvent.alertings.length       , 2                       , "[case-3] alertings = null  -> ok" );
    assert.equal(simpleEvent.alertings[0].name      , "alert-1"               , "[case-3] alertings[0].name = 'alert-1'  -> ok" );
    assert.equal(simpleEvent.alertings[0].provider  , "default-alert-provider", "[case-3] alertings[0].provider = 'default-alert-provide'  -> ok" );
    assert.equal(simpleEvent.alertings[0].message   , "alert message"         , "[case-3] alertings[0].message = 'alert message'  -> ok" );
    assert.equal(simpleEvent.alertings[0].level     , "warn"                  , "[case-3] alertings[0].level = 'warn'  -> ok" );
    assert.equal(simpleEvent.alertings[0].condition , "Date.now() %2 == 0"    , "[case-3] alertings[0].condition = 'Date.now() %2 == 0'  -> ok" );
    assert.equal(simpleEvent.alertings[0].function , null                     , "[case-3] alertings[0].function = null  -> ok" );

    assert.equal(simpleEvent.alertings[1].name      , "alert-2"               , "[case-3] alertings[1].name = 'alert-2'  -> ok" );
    assert.equal(simpleEvent.alertings[1].provider  , "default-alert-provider", "[case-3] alertings[1].provider = 'default-alert-provide'  -> ok" );
    assert.equal(simpleEvent.alertings[1].message   , null                    , "[case-3] alertings[1].message = null  -> ok" );
    assert.equal(simpleEvent.alertings[1].level     , null                    , "[case-3] alertings[1].level = null  -> ok" );
    assert.equal(simpleEvent.alertings[1].condition , null                    , "[case-3] alertings[1].condition = null  -> ok" );
    assert.equal(simpleEvent.alertings[1].function , "foo.alert.check"        , "[case-3] alertings[1].function = 'foo.alert.check'  -> ok" );


    assert.equal(simpleEvent.processors.length, 2                             , "[case-3] simpleEvent.processors = null  -> ok" );
    assert.equal(simpleEvent.processors[0].name         , "processorA"        , "[case-3] processors[0].name = 'processorA'  -> ok" );
    assert.equal(simpleEvent.processors[0].className    , null                , "[case-3] processors[0].className = null  -> ok" );
    assert.equal(simpleEvent.processors[0].configs      , null                , "[case-3] processors[0].configs = null  -> ok" );
    assert.equal(simpleEvent.processors[0].manifest     , null                , "[case-3] processors[0].manifest = null  -> ok" );

    assert.equal(simpleEvent.processors[1].name         , "processorB"        , "[case-3] processors[0].name = 'processorB'  -> ok" );
    assert.equal(simpleEvent.processors[1].className    , null                , "[case-3] processors[0].className = null  -> ok" );
    assert.equal(simpleEvent.processors[1].configs      , null                , "[case-3] processors[0].configs = null  -> ok" );
    assert.equal(simpleEvent.processors[1].manifest     , null                , "[case-3] processors[0].manifest = null  -> ok" );
});


// -----------------------------------------------------------------------------
//  [inugami_core_alerting_test] org.inugami.builders.alert
// -----------------------------------------------------------------------------
QUnit.test("[inugami_core_alerting_test] org.inugami.builders.event", function( assert ) {
    var query =  "org.foo.bar.service.10min";
    var event = org.inugami.builders.buildEvent("event");

    assert.ok(event ,                               "[case-1] event isn't null  -> ok" );
    assert.equal(event.name      , "event"        , "[case-1] event.name = event  -> ok" );
    assert.equal(event.from      , null           , "[case-1] event.from = null  -> ok" );
    assert.equal(event.until     , null           , "[case-1] event.until = null  -> ok" );
    assert.equal(event.provider.get()  , null           , "[case-1] event.provider = null  -> ok" );
    assert.equal(event.mapper    , null           , "[case-1] event.mapper = null  -> ok" );
    assert.equal(event.parent    , null           , "[case-1] event.parent = null  -> ok" );
    assert.equal(event.scheduler , null           , "[case-1] event.scheduler = null  -> ok" );
    assert.equal(event.alertings , null           , "[case-1] event.alertings = null  -> ok" );
    assert.equal(event.processors, null           , "[case-1] event.processors = null  -> ok" );
    assert.equal(event.targets   , null           , "[case-1] event.targets = null  -> ok" );

    event = org.inugami.builders.buildEvent("event", "my-provider", "-20min", "-10min");
    assert.equal(event.name      , "event"        , "[case-2] event.name = event  -> ok" );
    assert.equal(event.from      , "-20min"       , "[case-2] event.from = '-20min'  -> ok" );
    assert.equal(event.until     , "-10min"       , "[case-2] event.until = '-10min'  -> ok" );
    assert.equal(event.provider.get()  , "my-provider"  , "[case-2] event.provider = 'my-provider'  -> ok" );
    assert.equal(event.mapper    , null           , "[case-2] event.mapper = null  -> ok" );
    assert.equal(event.parent    , null           , "[case-2] event.parent = null  -> ok" );
    assert.equal(event.scheduler , null           , "[case-2] event.scheduler = null  -> ok" );
    assert.equal(event.alertings , null           , "[case-2] event.alertings = null  -> ok" );
    assert.equal(event.processors, null           , "[case-2] event.processors = null  -> ok" );
    assert.equal(event.targets   , null           , "[case-2] event.targets = null  -> ok" );


    event = org.inugami.builders.event({
		"name"       :"event",
		"from"       : "-10min",
		"until"      : "-5min",
		"provider"   : "graphite.provider",
		"scheduler"  : "{{EVERY_MINUTE}}",
		"mapper"     : "spiMapper",
		"processors" : [
			{"name"  : "processorA"}
		],
		"alertings"  : [
			{
				"name"      : "alert-1",
				"provider"  : "default-alert-provider",
				"message"   : "alert message",
				"level"     : "warn",
				"condition" : "Date.now() %2 == 0"
			}
		],
		"targets" : [
			{
				"name"       :"target",
				"from"       : "-30min",
				"until"      : "-2min",
				"provider"   : "graphite.provider.target",
				"parent"     : "event",
				"query"      : query,
				"mapper"	 : "targetMapperSpi",
				"processors" : [
					{"name"  : "processorC"}
				],
				"alertings"  : [
					{
						"name"     : "alert-4",
						"provider" : "default-alert-provider4",
						"function" : "foo.alert.check4"
					}
				]
			}
		]
    });	
    
    assert.equal(event.name      , "event"              , "[case-3] event.name = event  -> ok" );
    assert.equal(event.from      , "-10min"             , "[case-3] event.from = '-10min'  -> ok" );
    assert.equal(event.until     , "-5min"              , "[case-3] event.until = '-5min'  -> ok" );
    assert.equal(event.provider  , "graphite.provider"  , "[case-3] event.provider = 'graphite.provider'  -> ok" );
    assert.equal(event.mapper    , "spiMapper"          , "[case-3] event.mapper = 'spiMapper'  -> ok" );
    assert.equal(event.parent    , null                 , "[case-3] event.parent = null  -> ok" );
    assert.equal(event.scheduler , "{{EVERY_MINUTE}}"   , "[case-3] event.scheduler = '{{EVERY_MINUTE}}'  -> ok" );
    
    assert.equal(event.alertings.length , 1             , "[case-3] event.alertings = null  -> ok" );
    var alert = event.alertings[0];
    assert.equal(alert.name      , "alert-1"               , "[case-3] alertings[0].name = 'alert-1'  -> ok" );
    assert.equal(alert.provider  , "default-alert-provider", "[case-3] alertings[0].provider = 'default-alert-provide'  -> ok" );
    assert.equal(alert.message   , "alert message"         , "[case-3] alertings[0].message = 'alert message'  -> ok" );
    assert.equal(alert.level     , "warn"                  , "[case-3] alertings[0].level = 'warn'  -> ok" );
    assert.equal(alert.condition , "Date.now() %2 == 0"    , "[case-3] alertings[0].condition = 'Date.now() %2 == 0'  -> ok" );
    assert.equal(alert.function , null                     , "[case-3] alertings[0].function = null  -> ok" );


    assert.equal(event.processors.length, 1             , "[case-3] event.processors = null  -> ok" );
    var proc = event.processors[0];
    assert.equal(proc.name         , "processorA"        , "[case-3] processors[0].name = 'processorA'  -> ok" );
    assert.equal(proc.className    , null                , "[case-3] processors[0].className = null  -> ok" );
    assert.equal(proc.configs      , null                , "[case-3] processors[0].configs = null  -> ok" );
    assert.equal(proc.manifest     , null                , "[case-3] processors[0].manifest = null  -> ok" );


    assert.equal(event.targets.length   , 1             , "[case-3] event.targets = null  -> ok" );
    var target = event.targets[0];
    assert.equal(target.name      , "target"                    , "[case-3] target.name = event  -> ok" );
    assert.equal(target.from      , "-30min"                    , "[case-3] target.from = '-10min'  -> ok" );
    assert.equal(target.until     , "-2min"                     , "[case-3] target.until = '-5min'  -> ok" );
    assert.equal(target.provider  , "graphite.provider.target"  , "[case-3] target.provider = 'graphite.provider'  -> ok" );
    assert.equal(target.mapper    , "targetMapperSpi"           , "[case-3] target.mapper = 'spiMapper'  -> ok" );
    assert.equal(target.parent    , "event"                     , "[case-3] target.parent ='event'  -> ok" );
    assert.equal(target.scheduler , null                        , "[case-3] target.scheduler = null -> ok" );
    assert.equal(target.query    , query                        , "[case-3] target.query ='org.foo.bar.service.10min'  -> ok" );
});

