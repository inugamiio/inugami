var u= {
	foo : "test",
	check :12
}

console.log("json :"+JSON.stringify(u));
var date= new Date();
console.log("now :"+Date.now());
console.log("date getDate:"+date.getDate());
console.log("date getDay:"+date.getDay());
console.log("date getFullYear:"+date.getFullYear());
console.log("date getHours:"+date.getHours());
console.log("date getMilliseconds:"+date.getMilliseconds());
console.log("date getMinutes:"+date.getMinutes());
console.log("date getMonth:"+date.getMonth());
console.log("date getSeconds:"+date.getSeconds());
console.log("date getTime:"+date.getTime());
console.log("date getTimezoneOffset:"+date.getTimezoneOffset());
console.log("date getYear:"+date.getYear());


var test="hello";
if(test!==undefined){
	console.log(test);
}