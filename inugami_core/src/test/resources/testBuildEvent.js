
function checkEvent(){
	var event = org.inugami.builders.event({
		"name"       :"event",
		"from"       : "-10min",
		"until"      : "-5min",
		"provider"   : "graphite.provider",
		"scheduler"  : "{{EVERY_MINUTE}}",
		"mapper"     : "spiMapper",
		"processors" : [
			{"name"  : "processorA"},
			{"name"  :  "processorB"}
		],
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
		],
		"targets" : [
			{
				"name"       :"target",
				"from"       : "-30min",
				"until"      : "-2min",
				"provider"   : "graphite.provider.target",
				"parent"     : "event",
				"query"      : "foo.org.bar.service.10min",
				"mapper"	 : "targetMapperSpi",
				"processors" : [
					{"name"  : "processorC"},
					{"name"  :  "processorD"}
				],
				"alertings"  : [
					{
						"name"      : "alert-3",
						"provider"  : "default-alert-provider3",
						"message"   : "alert message3",
						"level"     : "warn",
						"condition" : "Date.now() %3 == 0"
					},
					{
						"name"     : "alert-4",
						"provider" : "default-alert-provider4",
						"function" : "foo.alert.check4"
					}
				]
			}
		]
	});	
	console.log(event);
	return event;
}
