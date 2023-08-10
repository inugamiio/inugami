

var regexA = new RegExp(".*foo.*");


if("hello foobar".match(regexA)){
	
}else{
	new Error("checkRegex error", "checkRegex.js",9);
}