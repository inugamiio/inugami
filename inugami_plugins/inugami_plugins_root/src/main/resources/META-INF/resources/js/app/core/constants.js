// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// CONSTANTS
// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
function _S4() {
    return (((1+Math.random())*0x10000)|0).toString(16).substring(1); 
}
function _buildUid(){
    return (_S4() + _S4() + "-" + _S4() + "-4" + _S4().substr(0,3) + "-" + _S4() + "-" + _S4() + _S4() + _S4()).toLowerCase();
}



org.inugami.constants = {
    mimetype : {
        JSON : "application/json"
    },
    headers:{
        DEFAULT_JSON                   : { 'Content-Type': 'application/json' },
        DEVICE_IDENTIFIER              : "x-device-identifier",
        CORRELATION_ID                 : "x-correlation-id",
        DEVICE_TYPE                    : "x-device-type",
        DEVICE_CLASS                   : "x-device-class",
        DEVICE_OS_VERSION              : "x-device-os-version",
        DEVICE_VERSION                 : "x-device-version",
        DEVICE_NETWORK_TYPE            : "x-device-network-type",
        DEVICE_NETWORK_SPEED_DOWN      : "x-device-network-speed-down",
        DEVICE_NETWORK_SPEED_LATENCY   : "x-device-network-speed-latency"
    },
    codes : {
        types : ['actionscript3', 'as3','applescript','bash', 'shell', 'sh','c#',
                 'c-sharp', 'csharp','coldfusion','cf','cpp', 'cc', 'c++', 'c',
                 'h', 'hpp', 'h++','css','delphi', 'pascal', 'pas','diff', 'patch',
                 'erl', 'erlang','groovy','haxe', 'hx','js', 'jscript', 'javascript', 'json',
                 'java','jfx', 'javafx','perl', 'Perl', 'pl','php','text', 'plain',
                 'powershell', 'ps', 'posh','py', 'python','ruby', 'rails', 'ror', 'rb',
                 'sass', 'scss','scala','sql','tap', 'Tap', 'TAP','ts', 'typescript',
                 'vb', 'vbnet','xml', 'xhtml', 'xslt', 'html', 'plist']
    },
    lorem : {
        data :['a', 'ac', 'accumsan', 'adipiscing', 'Aenean', 'Aliquam', 'aliquam', 'amet', 'ante', 'arcu', 'at', 'condimentum',
               'congue', 'consectetur', 'consequat', 'convallis', 'Cras', 'cursus', 'diam', 'dictum', 'dolor', 'Donec', 'dui',
               'efficitur', 'egestas', 'eleifend', 'elit', 'enim', 'erat', 'eros', 'et', 'Etiam', 'ex', 'facilisis', 'faucibus',
               'felis', 'feugiat', 'finibus', 'Fusce', 'gravida', 'hendrerit', 'id', 'imperdiet', 'In', 'in', 'Integer', 'ipsum',
               'justo', 'lacinia', 'lacus', 'laoreet', 'lectus', 'leo', 'libero', 'lobortis', 'Lorem', 'lorem', 'magna', 'massa',
               'mauris', 'Mauris', 'maximus', 'molestie', 'nec', 'neque', 'nibh', 'nisi', 'nisl', 'non', 'Nullam', 'Nunc', 'nunc',
               'odio', 'orci', 'ornare', 'pellentesque', 'pharetra', 'placerat', 'Praesent', 'Proin', 'pulvinar', 'quis', 'rutrum',
               'sagittis', 'sapien', 'Sed', 'sit', 'sodales', 'sollicitudin', 'Suspendisse', 'tempor', 'tincidunt', 'tortor',
               'tristique', 'turpis', 'ullamcorper', 'ultricies', 'urna', 'ut', 'varius', 'vehicula', 'vel', 'velit', 'venenatis',
               'Vestibulum', 'vestibulum', 'vitae', 'Vivamus', 'viverra', 'volutpat', 'vulputate'],
        size : 0
    },
    context: {
        URL : window.location.href,
        PROTOCOLE: null,
        CONTEXT : null,
        PAGE :null,
        OPTION:null,
        OPTION_TYPE:null,
        LOCALE: window.navigator.userLanguage || window.navigator.language
        
    },
    math :{
        TWO_PI       : 2 * Math.PI,
        TWO_PI_RATIO : (2 * Math.PI)/360
    },
    token : "inugami_token",
    uuid:_buildUid(),
    deviceIdentifier : (function(){
        var result =  localStorage.getItem("x-device-identifier");
        if(result == null || result == undefined){
            result = _buildUid();
            localStorage.setItem("x-device-identifier",result);
        }
        return result;
    })(),
    deviceClass: (function(){
        var isMobile = {
            Android: function() {
                return navigator.userAgent.match(/Android/i);
            },
            BlackBerry: function() {
                return navigator.userAgent.match(/BlackBerry/i);
            },
            iOS: function() {
                return navigator.userAgent.match(/iPhone|iPad|iPod/i);
            },
            Opera: function() {
                return navigator.userAgent.match(/Opera Mini/i);
            },
            Windows: function() {
                return navigator.userAgent.match(/IEMobile/i) || navigator.userAgent.match(/WPDesktop/i);
            },
            any: function() {
                return (isMobile.Android() || isMobile.BlackBerry() || isMobile.iOS() || isMobile.Opera() || isMobile.Windows());
            }
        };
        
        var result = [isMobile.any()?"MOBILE":"WEB",window.screen.width+'x'+window.screen.height];
        return result.join('_');
    })(),
    deviceType: (function(){
        var result = [platform.name,platform.version,platform.product,platform.layout];
        return result.join('_');
    })(),
    deviceOsVersion:platform.os,
    deviceVersion:APPLICATION_VERSION,
    deviceNetworkType: (function(){
        var result = [];
        if(isNotNull(navigator.connection)){
            result.push(isNull(navigator.connection.type)?"unknown":navigator.connection.type);
            result.push(navigator.connection.effectiveType);
        }
        return result.join('_');
    })(),
    deviceNetworkSpeedDown: (function(){
        return isNotNull(navigator.connection)?navigator.connection.downlink: 0;
    })(),
    deviceNetworkSpeedLatency: (function(){
        return isNotNull(navigator.connection)?navigator.connection.rtt:0;
    })(),
}
org.inugami.constants.lorem.size= org.inugami.constants.lorem.data.length;


