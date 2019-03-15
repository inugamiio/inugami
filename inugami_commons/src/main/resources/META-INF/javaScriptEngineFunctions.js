var _tools          = Java.type('org.inugami.commons.engine.js.JavaScriptEngineFunctions');
var _toolsBuilders  = Java.type('org.inugami.commons.engine.js.JavaScriptEngineBuildersFunctions');
var _toolsRegex     = Java.type('org.inugami.commons.engine.js.JsRegex');
//=========================================================================
// JSON
//=========================================================================
var JSON = {
	stringify:function(data){
		return _tools.stringify(data);
	}
}
//=========================================================================
//COMPARATORS
//=========================================================================
function lt(ref, value){
	return ref > value;
}
function le(ref, value){
	return ref >= value;
}
function gt(ref, value){
	return ref < value;
}
function ge(ref, value){
	return ref <= value;
}

//=========================================================================
// CONSOLE
//=========================================================================
var console = {
	assert : function(condition) {
		if(!condition){
			throw "assertion error";
		}
	},
	clean : function() {
	},
	context : function() {
	},
	count : function() {
	},
	debug : function(data) {
		_tools.debug(data);
	},
	dir : function() {
	},
	dirxml : function() {
	},
	error : function(data) {
		_tools.error(data);
	},
	group : function() {
	},
	groupCollapsed : function() {
	},
	groupEnd : function() {
	},
	info : function(data) {
		_tools.log(data);
	},
	log : function(data) {
		_tools.log(data);
	},
	markTimeline : function() {
	},
	profile : function() {
	},
	profileEnd : function() {
	},
	table : function() {
	},
	time : function() {
	},
	timeEnd : function() {
	},
	timeStamp : function() {
	},
	timeline : function() {
	},
	timelineEnd : function() {
	},
	trace : function(data) {
		_tools.trace(data);
	},
	warn : function(data) {
		_tools.warn(data);
	}
}



//=========================================================================
// DATE
//=========================================================================

var Date = function(){
	this._innnerDate = _tools.dateNew();	
}
Date.prototype.getDate = function(){
	return this._innnerDate.get(5);
}
Date.prototype.getFullYear= function(){
	return this._innnerDate.get(1);
}
Date.prototype.getDay = function(){
	return this._innnerDate.get(5);
}


Date.prototype.getHours= function(){
	return this._innnerDate.get(11);
}
Date.prototype.getMilliseconds= function(){
	return this._innnerDate.get(14);
}
Date.prototype.getMinutes= function(){
	return this._innnerDate.get(12);
}
Date.prototype.getMonth= function(){
	return this._innnerDate.get(2);
}
Date.prototype.getSeconds= function(){
	return this._innnerDate.get(13);
}
Date.prototype.getTime= function(){
	return this._innnerDate.getTimeInMillis();
}
Date.prototype.getTimezoneOffset= function(){
	return this._innnerDate.get(15);
}
Date.prototype.getYear= function(){
	return this._innnerDate.get(1);
}
Date.prototype.getUTCDate= function(){
	return -1;
}
Date.prototype.getUTCDay= function(){
	return -1;
}
Date.prototype.getUTCFullYear= function(){
	return -1;
}
Date.prototype.getUTCHours= function(){
	return -1;
}
Date.prototype.getUTCMilliseconds= function(){
	return -1;
}
Date.prototype.getUTCMinutes= function(){
	return -1;
}
Date.prototype.getUTCMonth= function(){
	return -1;
}
Date.prototype.getUTCSeconds= function(){
	return -1;
}

Date.now = function(){
	return _tools.dateNow();
}


//=========================================================================
// HTTPS
//=========================================================================
var http = {
		get : function(url,header){
			var _header = header!==undefined?header:null;
			return _tools.httpGet(url,_header);
		},
		post : function(url,json,header){
			var _header = header!==undefined?header:null;
			return _tools.httpPost(url,json,header);			
		}
}

//=========================================================================
//REGEX
//=========================================================================
var RegExp = function(regex, options ){
	this._innnerRegEx = _toolsRegex.newRegEx(regex, options );	
}


//=========================================================================
//String	
//=========================================================================
String.prototype.match = function(strRegex){
	return _toolsRegex.match(this,strRegex);
}

String.prototype.trim = function(){
	return _toolsRegex.trim(this);
}


//=========================================================================
// Extract	
//=========================================================================
extract = function(object, path , defaultValue){
	return _tools.extract(object,path,defaultValue);
}


//=========================================================================
// BUILDERS
//=========================================================================
builders = {}

builders.timeValue = function(path, value, time){
	return _toolsBuilders.timeValue(path, value, time);
}
