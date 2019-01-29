var data = {
		"foo":{
			"bar" : "Hello the world"
		},
		"values" : [
			{
				"style":"joe"
			},
			{
				"style":"foobar"
			}
		]
};
var extracted00 =  extract(data, "foo.baaaaar");
console.log("extracted 0 : "+extracted00);

var extracted0 =  extract(data, "foo.baaaaar", "empty");
console.log("extracted 0 : "+extracted0);


var extracted1 =  extract(data, "foo.bar");
console.log("extracted 1 : "+extracted1);

var extracted2 =  extract(data, "values.[1].style");
console.log("extracted 2 : "+extracted2);




console.log("http result = "+result);
var graphiteData = result.data[0];
console.log("graphite target = "+graphiteData.target);

console.log("-----------------------");
console.log(JSON.stringify(result.data[0]));
console.log("-----------------------");
console.log("length :"+result.data[0].datapoints.length);
for(var i=0; i<result.data[0].datapoints.length; i++){
	console.log(">> "+result.data[0].datapoints[i][0]);
}
console.log("-----------------------");

console.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
console.log("> Check Builders");
console.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
console.log(builders.timeValue("foo.bar.org", 15.25, Date.now()/1000));
console.log(builders.timeValue("foo.bar.org.joe", 21.1124));
