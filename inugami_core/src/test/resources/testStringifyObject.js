function checkJsonStringify(){
	
	
	var alert = io.inugami.builders.alertError("_services.err",{"seuil":"8"});
	alert.data.service="MFO";
	alert.data.nominal="5";
	alert.data.unit="%"
	alert.data.currentValue=8.25242718446602;
	alert.duration=300;

    
  
	var result = JSON.stringify(alert);
	
	
	return result;
}



