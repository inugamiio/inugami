(function(){"use strict";var e={function:!0,object:!0},R=e[typeof window]&&window||this,i=e[typeof exports]&&exports,t=e[typeof module]&&module&&!module.nodeType&&module,n=i&&t&&"object"==typeof global&&global;!n||n.global!==n&&n.window!==n&&n.self!==n||(R=n);var o=Math.pow(2,53)-1,T=/\bOpera/,r=Object.prototype,a=r.hasOwnProperty,F=r.toString;function l(e){return(e=String(e)).charAt(0).toUpperCase()+e.slice(1)}function G(e){return e=K(e),/^(?:webOS|i(?:OS|P))/.test(e)?e:l(e)}function $(e,t){for(var i in e)a.call(e,i)&&t(e[i],i,e)}function X(e){return null==e?l(e):F.call(e).slice(8,-1)}function j(e){return String(e).replace(/([ -])(?!$)/g,"$1?")}function N(i,n){var r=null;return function(e,t){var i=-1,n=e?e.length:0;if("number"==typeof n&&-1<n&&n<=o)for(;++i<n;)t(e[i],i,e);else $(e,t)}(i,function(e,t){r=n(r,e,t,i)}),r}function K(e){return String(e).replace(/^ +| +$/g,"")}var s=function e(n){var t=R,i=n&&"object"==typeof n&&"String"!=X(n);i&&(t=n,n=null);var r=t.navigator||{},o=r.userAgent||"";n||(n=o);var a,l,s=i?!!r.likeChrome:/\bChrome\b/.test(n)&&!/internal|\n/i.test(F.toString()),b="Object",c=i?b:"ScriptBridgingProxyObject",p=i?b:"Environment",u=i&&t.java?"JavaPackage":X(t.java),d=i?b:"RuntimeObject",f=/\bJava/.test(u)&&t.java,S=f&&X(t.environment)==p,x=f?"a":"α",h=f?"b":"β",m=t.document||{},g=t.operamini||t.opera,O=T.test(O=i&&g?g["[[Class]]"]:X(g))?O:g=null,y=n,M=[],v=null,P=n==o,w=P&&g&&"function"==typeof g.version&&g.version(),E=N([{label:"EdgeHTML",pattern:"Edge"},"Trident",{label:"WebKit",pattern:"AppleWebKit"},"iCab","Presto","NetFront","Tasman","KHTML","Gecko"],function(e,t){return e||RegExp("\\b"+(t.pattern||j(t))+"\\b","i").exec(n)&&(t.label||t)}),k=N(["Adobe AIR","Arora","Avant Browser","Breach","Camino","Electron","Epiphany","Fennec","Flock","Galeon","GreenBrowser","iCab","Iceweasel","K-Meleon","Konqueror","Lunascape","Maxthon",{label:"Microsoft Edge",pattern:"Edge"},"Midori","Nook Browser","PaleMoon","PhantomJS","Raven","Rekonq","RockMelt",{label:"Samsung Internet",pattern:"SamsungBrowser"},"SeaMonkey",{label:"Silk",pattern:"(?:Cloud9|Silk-Accelerated)"},"Sleipnir","SlimBrowser",{label:"SRWare Iron",pattern:"Iron"},"Sunrise","Swiftfox","Waterfox","WebPositive","Opera Mini",{label:"Opera Mini",pattern:"OPiOS"},"Opera",{label:"Opera",pattern:"OPR"},"Chrome",{label:"Chrome Mobile",pattern:"(?:CriOS|CrMo)"},{label:"Firefox",pattern:"(?:Firefox|Minefield)"},{label:"Firefox for iOS",pattern:"FxiOS"},{label:"IE",pattern:"IEMobile"},{label:"IE",pattern:"MSIE"},"Safari"],function(e,t){return e||RegExp("\\b"+(t.pattern||j(t))+"\\b","i").exec(n)&&(t.label||t)}),C=I([{label:"BlackBerry",pattern:"BB10"},"BlackBerry",{label:"Galaxy S",pattern:"GT-I9000"},{label:"Galaxy S2",pattern:"GT-I9100"},{label:"Galaxy S3",pattern:"GT-I9300"},{label:"Galaxy S4",pattern:"GT-I9500"},{label:"Galaxy S5",pattern:"SM-G900"},{label:"Galaxy S6",pattern:"SM-G920"},{label:"Galaxy S6 Edge",pattern:"SM-G925"},{label:"Galaxy S7",pattern:"SM-G930"},{label:"Galaxy S7 Edge",pattern:"SM-G935"},"Google TV","Lumia","iPad","iPod","iPhone","Kindle",{label:"Kindle Fire",pattern:"(?:Cloud9|Silk-Accelerated)"},"Nexus","Nook","PlayBook","PlayStation Vita","PlayStation","TouchPad","Transformer",{label:"Wii U",pattern:"WiiU"},"Wii","Xbox One",{label:"Xbox 360",pattern:"Xbox"},"Xoom"]),W=N({Apple:{iPad:1,iPhone:1,iPod:1},Archos:{},Amazon:{Kindle:1,"Kindle Fire":1},Asus:{Transformer:1},"Barnes & Noble":{Nook:1},BlackBerry:{PlayBook:1},Google:{"Google TV":1,Nexus:1},HP:{TouchPad:1},HTC:{},LG:{},Microsoft:{Xbox:1,"Xbox One":1},Motorola:{Xoom:1},Nintendo:{"Wii U":1,Wii:1},Nokia:{Lumia:1},Samsung:{"Galaxy S":1,"Galaxy S2":1,"Galaxy S3":1,"Galaxy S4":1},Sony:{PlayStation:1,"PlayStation Vita":1}},function(e,t,i){return e||(t[C]||t[/^[a-z]+(?: +[a-z]+\b)*/i.exec(C)]||RegExp("\\b"+j(i)+"(?:\\b|\\w*\\d)","i").exec(n))&&i}),B=N(["Windows Phone","Android","CentOS",{label:"Chrome OS",pattern:"CrOS"},"Debian","Fedora","FreeBSD","Gentoo","Haiku","Kubuntu","Linux Mint","OpenBSD","Red Hat","SuSE","Ubuntu","Xubuntu","Cygwin","Symbian OS","hpwOS","webOS ","webOS","Tablet OS","Tizen","Linux","Mac OS X","Macintosh","Mac","Windows 98;","Windows "],function(e,t){var i=t.pattern||j(t);return!e&&(e=RegExp("\\b"+i+"(?:/[\\d.]+|[ \\w.]*)","i").exec(n))&&(e=function(e,t,i){var n={"10.0":"10",6.4:"10 Technical Preview",6.3:"8.1",6.2:"8",6.1:"Server 2008 R2 / 7","6.0":"Server 2008 / Vista",5.2:"Server 2003 / XP 64-bit",5.1:"XP",5.01:"2000 SP1","5.0":"2000","4.0":"NT","4.90":"ME"};return t&&i&&/^Win/i.test(e)&&!/^Windows Phone /i.test(e)&&(n=n[/[\d.]+$/.exec(e)])&&(e="Windows "+n),e=String(e),t&&i&&(e=e.replace(RegExp(t,"i"),i)),e=G(e.replace(/ ce$/i," CE").replace(/\bhpw/i,"web").replace(/\bMacintosh\b/,"Mac OS").replace(/_PowerPC\b/i," OS").replace(/\b(OS X) [^ \d]+/i,"$1").replace(/\bMac (OS X)\b/,"$1").replace(/\/(\d)/," $1").replace(/_/g,".").replace(/(?: BePC|[ .]*fc[ \d.]+)$/i,"").replace(/\bx86\.64\b/gi,"x86_64").replace(/\b(Windows Phone) OS\b/,"$1").replace(/\b(Chrome OS \w+) [\d.]+\b/,"$1").split(" on ")[0])}(e,i,t.label||t)),e});function I(e){return N(e,function(e,t){var i=t.pattern||j(t);return!e&&(e=RegExp("\\b"+i+" *\\d+[.\\w_]*","i").exec(n)||RegExp("\\b"+i+" *\\w+-[\\w]*","i").exec(n)||RegExp("\\b"+i+"(?:; *(?:[a-z]+[_-])?[a-z]+\\d+|[^ ();-]*)","i").exec(n))&&((e=String(t.label&&!RegExp(i,"i").test(t.label)?t.label:e).split("/"))[1]&&!/[\d.]+/.test(e[0])&&(e[0]+=" "+e[1]),t=t.label||t,e=G(e[0].replace(RegExp(i,"i"),t).replace(RegExp("; *(?:"+t+"[_-])?","i")," ").replace(RegExp("("+t+")[-_.]?(\\w)","i"),"$1 $2"))),e})}if(E&&(E=[E]),W&&!C&&(C=I([W])),(a=/\bGoogle TV\b/.exec(C))&&(C=a[0]),/\bSimulator\b/i.test(n)&&(C=(C?C+" ":"")+"Simulator"),"Opera Mini"==k&&/\bOPiOS\b/.test(n)&&M.push("running in Turbo/Uncompressed mode"),"IE"==k&&/\blike iPhone OS\b/.test(n)?(W=(a=e(n.replace(/like iPhone OS/,""))).manufacturer,C=a.product):/^iP/.test(C)?(k||(k="Safari"),B="iOS"+((a=/ OS ([\d_]+)/i.exec(n))?" "+a[1].replace(/_/g,"."):"")):"Konqueror"!=k||/buntu/i.test(B)?W&&"Google"!=W&&(/Chrome/.test(k)&&!/\bMobile Safari\b/i.test(n)||/\bVita\b/.test(C))||/\bAndroid\b/.test(B)&&/^Chrome/.test(k)&&/\bVersion\//i.test(n)?(k="Android Browser",B=/\bAndroid\b/.test(B)?B:"Android"):"Silk"==k?(/\bMobi/i.test(n)||(B="Android",M.unshift("desktop mode")),/Accelerated *= *true/i.test(n)&&M.unshift("accelerated")):"PaleMoon"==k&&(a=/\bFirefox\/([\d.]+)\b/.exec(n))?M.push("identifying as Firefox "+a[1]):"Firefox"==k&&(a=/\b(Mobile|Tablet|TV)\b/i.exec(n))?(B||(B="Firefox OS"),C||(C=a[1])):!k||(a=!/\bMinefield\b/i.test(n)&&/\b(?:Firefox|Safari)\b/.exec(k))?(k&&!C&&/[\/,]|^[^(]+?\)/.test(n.slice(n.indexOf(a+"/")+8))&&(k=null),(a=C||W||B)&&(C||W||/\b(?:Android|Symbian OS|Tablet OS|webOS)\b/.test(B))&&(k=/[a-z]+(?: Hat)?/i.exec(/\bAndroid\b/.test(B)?B:a)+" Browser")):"Electron"==k&&(a=(/\bChrome\/([\d.]+)\b/.exec(n)||0)[1])&&M.push("Chromium "+a):B="Kubuntu",w||(w=N(["(?:Cloud9|CriOS|CrMo|Edge|FxiOS|IEMobile|Iron|Opera ?Mini|OPiOS|OPR|Raven|SamsungBrowser|Silk(?!/[\\d.]+$))","Version",j(k),"(?:Firefox|Minefield|NetFront)"],function(e,t){return e||(RegExp(t+"(?:-[\\d.]+/|(?: for [\\w-]+)?[ /-])([\\d.]+[^ ();/_-]*)","i").exec(n)||0)[1]||null})),(a=("iCab"==E&&3<parseFloat(w)?"WebKit":/\bOpera\b/.test(k)&&(/\bOPR\b/.test(n)?"Blink":"Presto"))||/\b(?:Midori|Nook|Safari)\b/i.test(n)&&!/^(?:Trident|EdgeHTML)$/.test(E)&&"WebKit"||!E&&/\bMSIE\b/i.test(n)&&("Mac OS"==B?"Tasman":"Trident")||"WebKit"==E&&/\bPlayStation\b(?! Vita\b)/i.test(k)&&"NetFront")&&(E=[a]),"IE"==k&&(a=(/; *(?:XBLWP|ZuneWP)(\d+)/i.exec(n)||0)[1])?(k+=" Mobile",B="Windows Phone "+(/\+$/.test(a)?a:a+".x"),M.unshift("desktop mode")):/\bWPDesktop\b/i.test(n)?(k="IE Mobile",B="Windows Phone 8.x",M.unshift("desktop mode"),w||(w=(/\brv:([\d.]+)/.exec(n)||0)[1])):"IE"!=k&&"Trident"==E&&(a=/\brv:([\d.]+)/.exec(n))&&(k&&M.push("identifying as "+k+(w?" "+w:"")),k="IE",w=a[1]),P){if(function(e,t){var i=null!=e?typeof e[t]:"number";return!(/^(?:boolean|number|string|undefined)$/.test(i)||"object"==i&&!e[t])}(t,"global"))if(f&&(y=(a=f.lang.System).getProperty("os.arch"),B=B||a.getProperty("os.name")+" "+a.getProperty("os.version")),S){try{w=t.require("ringo/engine").version.join("."),k="RingoJS"}catch(e){(a=t.system)&&a.global.system==t.system&&(k="Narwhal",B||(B=a[0].os||null))}k||(k="Rhino")}else"object"==typeof t.process&&!t.process.browser&&(a=t.process)&&("object"==typeof a.versions&&("string"==typeof a.versions.electron?(M.push("Node "+a.versions.node),k="Electron",w=a.versions.electron):"string"==typeof a.versions.nw&&(M.push("Chromium "+w,"Node "+a.versions.node),k="NW.js",w=a.versions.nw)),k||(k="Node.js",y=a.arch,B=a.platform,w=(w=/[\d.]+/.exec(a.version))?w[0]:null));else X(a=t.runtime)==c?(k="Adobe AIR",B=a.flash.system.Capabilities.os):X(a=t.phantom)==d?(k="PhantomJS",w=(a=a.version||null)&&a.major+"."+a.minor+"."+a.patch):"number"==typeof m.documentMode&&(a=/\bTrident\/(\d+)/i.exec(n))?(w=[w,m.documentMode],(a=+a[1]+4)!=w[1]&&(M.push("IE "+w[1]+" mode"),E&&(E[1]=""),w[1]=a),w="IE"==k?String(w[1].toFixed(1)):w[0]):"number"==typeof m.documentMode&&/^(?:Chrome|Firefox)\b/.test(k)&&(M.push("masking as "+k+" "+w),k="IE",w="11.0",E=["Trident"],B="Windows");B=B&&G(B)}if(w&&(a=/(?:[ab]|dp|pre|[ab]\d+pre)(?:\d+\+?)?$/i.exec(w)||/(?:alpha|beta)(?: ?\d)?/i.exec(n+";"+(P&&r.appMinorVersion))||/\bMinefield\b/i.test(n)&&"a")&&(v=/b/i.test(a)?"beta":"alpha",w=w.replace(RegExp(a+"\\+?$"),"")+("beta"==v?h:x)+(/\d+\+?/.exec(a)||"")),"Fennec"==k||"Firefox"==k&&/\b(?:Android|Firefox OS)\b/.test(B))k="Firefox Mobile";else if("Maxthon"==k&&w)w=w.replace(/\.[\d.]+/,".x");else if(/\bXbox\b/i.test(C))"Xbox 360"==C&&(B=null),"Xbox 360"==C&&/\bIEMobile\b/.test(n)&&M.unshift("mobile mode");else if(!/^(?:Chrome|IE|Opera)$/.test(k)&&(!k||C||/Browser|Mobi/.test(k))||"Windows CE"!=B&&!/Mobi/i.test(n))if("IE"==k&&P)try{null===t.external&&M.unshift("platform preview")}catch(e){M.unshift("embedded")}else(/\bBlackBerry\b/.test(C)||/\bBB10\b/.test(n))&&(a=(RegExp(C.replace(/ +/g," *")+"/([.\\d]+)","i").exec(n)||0)[1]||w)?(B=((a=[a,/BB10/.test(n)])[1]?(C=null,W="BlackBerry"):"Device Software")+" "+a[0],w=null):this!=$&&"Wii"!=C&&(P&&g||/Opera/.test(k)&&/\b(?:MSIE|Firefox)\b/i.test(n)||"Firefox"==k&&/\bOS X (?:\d+\.){2,}/.test(B)||"IE"==k&&(B&&!/^Win/.test(B)&&5.5<w||/\bWindows XP\b/.test(B)&&8<w||8==w&&!/\bTrident\b/.test(n)))&&!T.test(a=e.call($,n.replace(T,"")+";"))&&a.name&&(a="ing as "+a.name+((a=a.version)?" "+a:""),T.test(k)?(/\bIE\b/.test(a)&&"Mac OS"==B&&(B=null),a="identify"+a):(a="mask"+a,k=O?G(O.replace(/([a-z])([A-Z])/g,"$1 $2")):"Opera",/\bIE\b/.test(a)&&(B=null),P||(w=null)),E=["Presto"],M.push(a));else k+=" Mobile";(a=(/\bAppleWebKit\/([\d.]+\+?)/i.exec(n)||0)[1])&&(a=[parseFloat(a.replace(/\.(\d)$/,".0$1")),a],"Safari"==k&&"+"==a[1].slice(-1)?(k="WebKit Nightly",v="alpha",w=a[1].slice(0,-1)):w!=a[1]&&w!=(a[2]=(/\bSafari\/([\d.]+\+?)/i.exec(n)||0)[1])||(w=null),a[1]=(/\bChrome\/([\d.]+)/i.exec(n)||0)[1],537.36==a[0]&&537.36==a[2]&&28<=parseFloat(a[1])&&"WebKit"==E&&(E=["Blink"]),a=P&&(s||a[1])?(E&&(E[1]="like Chrome"),a[1]||((a=a[0])<530?1:a<532?2:a<532.05?3:a<533?4:a<534.03?5:a<534.07?6:a<534.1?7:a<534.13?8:a<534.16?9:a<534.24?10:a<534.3?11:a<535.01?12:a<535.02?"13+":a<535.07?15:a<535.11?16:a<535.19?17:a<536.05?18:a<536.1?19:a<537.01?20:a<537.11?"21+":a<537.13?23:a<537.18?24:a<537.24?25:a<537.36?26:"Blink"!=E?"27":"28")):(E&&(E[1]="like Safari"),(a=a[0])<400?1:a<500?2:a<526?3:a<533?4:a<534?"4+":a<535?5:a<537?6:a<538?7:a<601?8:"8"),E&&(E[1]+=" "+(a+="number"==typeof a?".x":/[.+]/.test(a)?"":"+")),"Safari"==k&&(!w||45<parseInt(w))&&(w=a)),"Opera"==k&&(a=/\bzbov|zvav$/.exec(B))?(k+=" ",M.unshift("desktop mode"),"zvav"==a?(k+="Mini",w=null):k+="Mobile",B=B.replace(RegExp(" *"+a+"$"),"")):"Safari"==k&&/\bChrome\b/.exec(E&&E[1])&&(M.unshift("desktop mode"),k="Chrome Mobile",w=null,B=/\bOS X\b/.test(B)?(W="Apple","iOS 4.3+"):null),w&&0==w.indexOf(a=/[\d.]+$/.exec(B))&&-1<n.indexOf("/"+a+"-")&&(B=K(B.replace(a,""))),E&&!/\b(?:Avant|Nook)\b/.test(k)&&(/Browser|Lunascape|Maxthon/.test(k)||"Safari"!=k&&/^iOS/.test(B)&&/\bSafari\b/.test(E[1])||/^(?:Adobe|Arora|Breach|Midori|Opera|Phantom|Rekonq|Rock|Samsung Internet|Sleipnir|Web)/.test(k)&&E[1])&&(a=E[E.length-1])&&M.push(a),M.length&&(M=["("+M.join("; ")+")"]),W&&C&&C.indexOf(W)<0&&M.push("on "+W),C&&M.push((/^on /.test(M[M.length-1])?"":"on ")+C),B&&(a=/ ([\d.+]+)$/.exec(B),l=a&&"/"==B.charAt(B.length-a[0].length-1),B={architecture:32,family:a&&!l?B.replace(a[0],""):B,version:a?a[1]:null,toString:function(){var e=this.version;return this.family+(e&&!l?" "+e:"")+(64==this.architecture?" 64-bit":"")}}),(a=/\b(?:AMD|IA|Win|WOW|x86_|x)64\b/i.exec(y))&&!/\bi686\b/i.test(y)?(B&&(B.architecture=64,B.family=B.family.replace(RegExp(" *"+a),"")),k&&(/\bWOW64\b/i.test(n)||P&&/\w(?:86|32)$/.test(r.cpuClass||r.platform)&&!/\bWin64; x64\b/i.test(n))&&M.unshift("32-bit")):B&&/^OS X/.test(B.family)&&"Chrome"==k&&39<=parseFloat(w)&&(B.architecture=64),n||(n=null);var A={};return A.description=n,A.layout=E&&E[0],A.manufacturer=W,A.name=k,A.prerelease=v,A.product=C,A.ua=n,A.version=k&&w,A.os=B||{architecture:null,family:null,version:null,toString:function(){return"null"}},A.parse=e,A.toString=function(){return this.description||""},A.version&&M.unshift(w),A.name&&M.unshift(k),B&&k&&(B!=String(B).split(" ")[0]||B!=k.split(" ")[0]&&!C)&&M.push(C?"("+B+")":"on "+B),M.length&&(A.description=M.join(" ")),A}();"function"==typeof define&&"object"==typeof define.amd&&define.amd?(R.platform=s,define(function(){return s})):i&&t?$(s,function(e,t){i[t]=e}):R.platform=s}).call(this);