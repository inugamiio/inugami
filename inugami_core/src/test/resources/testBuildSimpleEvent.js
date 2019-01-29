
function checkSimpleEvent(){
	var simpleEvent = org.inugami.builders.simpleEvent({
		"name"       :"eventName",
		"from"       : "-10min",
		"until"      : "-5min",
		"provider"   : "graphite.provider",
		"scheduler"  : "{{EVERY_MINUTE}}",
		"mapper"     : "spiMapper",
		"parent"     : "parent",
		"processors" : [
			{"name"  : "processorA"},
			{"name"  :  "processorB"}
		],
		"query"		 : "foo.org.bar.service.10min",
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
	console.log(simpleEvent);
	return simpleEvent;
}
