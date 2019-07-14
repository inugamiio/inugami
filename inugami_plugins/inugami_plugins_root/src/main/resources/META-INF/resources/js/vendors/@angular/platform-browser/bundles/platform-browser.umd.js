!function(t,e){"object"==typeof exports&&"undefined"!=typeof module?e(exports,require("@angular/common"),require("@angular/core")):"function"==typeof define&&define.amd?define("@angular/platform-browser",["exports","@angular/common","@angular/core"],e):e(((t=t||self).ng=t.ng||{},t.ng.platformBrowser={}),t.ng.common,t.ng.core)}(this,function(t,r,f){"use strict";var o=function(t,e){return(o=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(t,e){t.__proto__=e}||function(t,e){for(var n in e)e.hasOwnProperty(n)&&(t[n]=e[n])})(t,e)};function e(t,e){function n(){this.constructor=t}o(t,e),t.prototype=null===e?Object.create(e):(n.prototype=e.prototype,new n)}var n=function(){return(n=Object.assign||function(t){for(var e,n=1,o=arguments.length;n<o;n++)for(var r in e=arguments[n])Object.prototype.hasOwnProperty.call(e,r)&&(t[r]=e[r]);return t}).apply(this,arguments)};function i(t,e,n,o){var r,i=arguments.length,a=i<3?e:null===o?o=Object.getOwnPropertyDescriptor(e,n):o;if("object"==typeof Reflect&&"function"==typeof Reflect.decorate)a=Reflect.decorate(t,e,n,o);else for(var s=t.length-1;0<=s;s--)(r=t[s])&&(a=(i<3?r(a):3<i?r(e,n,a):r(e,n))||a);return 3<i&&a&&Object.defineProperty(e,n,a),a}function a(n,o){return function(t,e){o(t,e,n)}}function s(t,e){if("object"==typeof Reflect&&"function"==typeof Reflect.metadata)return Reflect.metadata(t,e)}function u(t,e){var n="function"==typeof Symbol&&t[Symbol.iterator];if(!n)return t;var o,r,i=n.call(t),a=[];try{for(;(void 0===e||0<e--)&&!(o=i.next()).done;)a.push(o.value)}catch(t){r={error:t}}finally{try{o&&!o.done&&(n=i.return)&&n.call(i)}finally{if(r)throw r.error}}return a}var p=null;function c(){return p}function l(t){p=p||t}var d=(Object.defineProperty(y.prototype,"attrToPropMap",{get:function(){return this._attrToPropMap},set:function(t){this._attrToPropMap=t},enumerable:!0,configurable:!0}),y);function y(){this.resourceLoaderType=null}var h,m=(e(g,h=d),g.prototype.getDistributedNodes=function(t){return t.getDistributedNodes()},g.prototype.resolveAndSetHref=function(t,e,n){t.href=null==n?e:e+"/../"+n},g.prototype.supportsDOMEvents=function(){return!0},g.prototype.supportsNativeShadowDOM=function(){return"function"==typeof document.body.createShadowRoot},g.prototype.getAnimationPrefix=function(){return this._animationPrefix?this._animationPrefix:""},g.prototype.getTransitionEnd=function(){return this._transitionEnd?this._transitionEnd:""},g.prototype.supportsAnimation=function(){return null!=this._animationPrefix&&null!=this._transitionEnd},g);function g(){var e=h.call(this)||this;e._animationPrefix=null,e._transitionEnd=null;try{var n=e.createElement("div",document);if(null!=e.getStyle(n,"animationName"))e._animationPrefix="";else for(var t=["Webkit","Moz","O","ms"],o=0;o<t.length;o++)if(null!=e.getStyle(n,t[o]+"AnimationName")){e._animationPrefix="-"+t[o].toLowerCase()+"-";break}var r={WebkitTransition:"webkitTransitionEnd",MozTransition:"transitionend",OTransition:"oTransitionEnd otransitionend",transition:"transitionend"};Object.keys(r).forEach(function(t){null!=e.getStyle(n,t)&&(e._transitionEnd=r[t])})}catch(t){e._animationPrefix=null,e._transitionEnd=null}return e}var v,w={class:"className",innerHtml:"innerHTML",readonly:"readOnly",tabindex:"tabIndex"},b={"\b":"Backspace","\t":"Tab","":"Delete","":"Escape",Del:"Delete",Esc:"Escape",Left:"ArrowLeft",Right:"ArrowRight",Up:"ArrowUp",Down:"ArrowDown",Menu:"ContextMenu",Scroll:"ScrollLock",Win:"OS"},_={A:"1",B:"2",C:"3",D:"4",E:"5",F:"6",G:"7",H:"8",I:"9",J:"*",K:"+",M:"-",N:".",O:"/","`":"0","":"NumLock"},E=function(){if(f.ɵglobal.Node)return f.ɵglobal.Node.prototype.contains||function(t){return!!(16&this.compareDocumentPosition(t))}}(),S=(e(T,v=m),T.prototype.parse=function(t){throw new Error("parse not implemented")},T.makeCurrent=function(){l(new T)},T.prototype.hasProperty=function(t,e){return e in t},T.prototype.setProperty=function(t,e,n){t[e]=n},T.prototype.getProperty=function(t,e){return t[e]},T.prototype.invoke=function(t,e,n){var o;(o=t)[e].apply(o,function(){for(var t=[],e=0;e<arguments.length;e++)t=t.concat(u(arguments[e]));return t}(n))},T.prototype.logError=function(t){window.console&&(console.error?console.error(t):console.log(t))},T.prototype.log=function(t){window.console&&window.console.log&&window.console.log(t)},T.prototype.logGroup=function(t){window.console&&window.console.group&&window.console.group(t)},T.prototype.logGroupEnd=function(){window.console&&window.console.groupEnd&&window.console.groupEnd()},Object.defineProperty(T.prototype,"attrToPropMap",{get:function(){return w},enumerable:!0,configurable:!0}),T.prototype.contains=function(t,e){return E.call(t,e)},T.prototype.querySelector=function(t,e){return t.querySelector(e)},T.prototype.querySelectorAll=function(t,e){return t.querySelectorAll(e)},T.prototype.on=function(t,e,n){t.addEventListener(e,n,!1)},T.prototype.onAndCancel=function(t,e,n){return t.addEventListener(e,n,!1),function(){t.removeEventListener(e,n,!1)}},T.prototype.dispatchEvent=function(t,e){t.dispatchEvent(e)},T.prototype.createMouseEvent=function(t){var e=this.getDefaultDocument().createEvent("MouseEvent");return e.initEvent(t,!0,!0),e},T.prototype.createEvent=function(t){var e=this.getDefaultDocument().createEvent("Event");return e.initEvent(t,!0,!0),e},T.prototype.preventDefault=function(t){t.preventDefault(),t.returnValue=!1},T.prototype.isPrevented=function(t){return t.defaultPrevented||null!=t.returnValue&&!t.returnValue},T.prototype.getInnerHTML=function(t){return t.innerHTML},T.prototype.getTemplateContent=function(t){return"content"in t&&this.isTemplateElement(t)?t.content:null},T.prototype.getOuterHTML=function(t){return t.outerHTML},T.prototype.nodeName=function(t){return t.nodeName},T.prototype.nodeValue=function(t){return t.nodeValue},T.prototype.type=function(t){return t.type},T.prototype.content=function(t){return this.hasProperty(t,"content")?t.content:t},T.prototype.firstChild=function(t){return t.firstChild},T.prototype.nextSibling=function(t){return t.nextSibling},T.prototype.parentElement=function(t){return t.parentNode},T.prototype.childNodes=function(t){return t.childNodes},T.prototype.childNodesAsList=function(t){for(var e=t.childNodes,n=new Array(e.length),o=0;o<e.length;o++)n[o]=e[o];return n},T.prototype.clearNodes=function(t){for(;t.firstChild;)t.removeChild(t.firstChild)},T.prototype.appendChild=function(t,e){t.appendChild(e)},T.prototype.removeChild=function(t,e){t.removeChild(e)},T.prototype.replaceChild=function(t,e,n){t.replaceChild(e,n)},T.prototype.remove=function(t){return t.parentNode&&t.parentNode.removeChild(t),t},T.prototype.insertBefore=function(t,e,n){t.insertBefore(n,e)},T.prototype.insertAllBefore=function(e,n,t){t.forEach(function(t){return e.insertBefore(t,n)})},T.prototype.insertAfter=function(t,e,n){t.insertBefore(n,e.nextSibling)},T.prototype.setInnerHTML=function(t,e){t.innerHTML=e},T.prototype.getText=function(t){return t.textContent},T.prototype.setText=function(t,e){t.textContent=e},T.prototype.getValue=function(t){return t.value},T.prototype.setValue=function(t,e){t.value=e},T.prototype.getChecked=function(t){return t.checked},T.prototype.setChecked=function(t,e){t.checked=e},T.prototype.createComment=function(t){return this.getDefaultDocument().createComment(t)},T.prototype.createTemplate=function(t){var e=this.getDefaultDocument().createElement("template");return e.innerHTML=t,e},T.prototype.createElement=function(t,e){return(e=e||this.getDefaultDocument()).createElement(t)},T.prototype.createElementNS=function(t,e,n){return(n=n||this.getDefaultDocument()).createElementNS(t,e)},T.prototype.createTextNode=function(t,e){return(e=e||this.getDefaultDocument()).createTextNode(t)},T.prototype.createScriptTag=function(t,e,n){var o=(n=n||this.getDefaultDocument()).createElement("SCRIPT");return o.setAttribute(t,e),o},T.prototype.createStyleElement=function(t,e){var n=(e=e||this.getDefaultDocument()).createElement("style");return this.appendChild(n,this.createTextNode(t,e)),n},T.prototype.createShadowRoot=function(t){return t.createShadowRoot()},T.prototype.getShadowRoot=function(t){return t.shadowRoot},T.prototype.getHost=function(t){return t.host},T.prototype.clone=function(t){return t.cloneNode(!0)},T.prototype.getElementsByClassName=function(t,e){return t.getElementsByClassName(e)},T.prototype.getElementsByTagName=function(t,e){return t.getElementsByTagName(e)},T.prototype.classList=function(t){return Array.prototype.slice.call(t.classList,0)},T.prototype.addClass=function(t,e){t.classList.add(e)},T.prototype.removeClass=function(t,e){t.classList.remove(e)},T.prototype.hasClass=function(t,e){return t.classList.contains(e)},T.prototype.setStyle=function(t,e,n){t.style[e]=n},T.prototype.removeStyle=function(t,e){t.style[e]=""},T.prototype.getStyle=function(t,e){return t.style[e]},T.prototype.hasStyle=function(t,e,n){var o=this.getStyle(t,e)||"";return n?o==n:0<o.length},T.prototype.tagName=function(t){return t.tagName},T.prototype.attributeMap=function(t){for(var e=new Map,n=t.attributes,o=0;o<n.length;o++){var r=n.item(o);e.set(r.name,r.value)}return e},T.prototype.hasAttribute=function(t,e){return t.hasAttribute(e)},T.prototype.hasAttributeNS=function(t,e,n){return t.hasAttributeNS(e,n)},T.prototype.getAttribute=function(t,e){return t.getAttribute(e)},T.prototype.getAttributeNS=function(t,e,n){return t.getAttributeNS(e,n)},T.prototype.setAttribute=function(t,e,n){t.setAttribute(e,n)},T.prototype.setAttributeNS=function(t,e,n,o){t.setAttributeNS(e,n,o)},T.prototype.removeAttribute=function(t,e){t.removeAttribute(e)},T.prototype.removeAttributeNS=function(t,e,n){t.removeAttributeNS(e,n)},T.prototype.templateAwareRoot=function(t){return this.isTemplateElement(t)?this.content(t):t},T.prototype.createHtmlDocument=function(){return document.implementation.createHTMLDocument("fakeTitle")},T.prototype.getDefaultDocument=function(){return document},T.prototype.getBoundingClientRect=function(t){try{return t.getBoundingClientRect()}catch(t){return{top:0,bottom:0,left:0,right:0,width:0,height:0}}},T.prototype.getTitle=function(t){return t.title},T.prototype.setTitle=function(t,e){t.title=e||""},T.prototype.elementMatches=function(t,e){return!!this.isElementNode(t)&&(t.matches&&t.matches(e)||t.msMatchesSelector&&t.msMatchesSelector(e)||t.webkitMatchesSelector&&t.webkitMatchesSelector(e))},T.prototype.isTemplateElement=function(t){return this.isElementNode(t)&&"TEMPLATE"===t.nodeName},T.prototype.isTextNode=function(t){return t.nodeType===Node.TEXT_NODE},T.prototype.isCommentNode=function(t){return t.nodeType===Node.COMMENT_NODE},T.prototype.isElementNode=function(t){return t.nodeType===Node.ELEMENT_NODE},T.prototype.hasShadowRoot=function(t){return null!=t.shadowRoot&&t instanceof HTMLElement},T.prototype.isShadowRoot=function(t){return t instanceof DocumentFragment},T.prototype.importIntoDoc=function(t){return document.importNode(this.templateAwareRoot(t),!0)},T.prototype.adoptNode=function(t){return document.adoptNode(t)},T.prototype.getHref=function(t){return t.getAttribute("href")},T.prototype.getEventKey=function(t){var e=t.key;if(null==e){if(null==(e=t.keyIdentifier))return"Unidentified";e.startsWith("U+")&&(e=String.fromCharCode(parseInt(e.substring(2),16)),3===t.location&&_.hasOwnProperty(e)&&(e=_[e]))}return b[e]||e},T.prototype.getGlobalEventTarget=function(t,e){return"window"===e?window:"document"===e?t:"body"===e?t.body:null},T.prototype.getHistory=function(){return window.history},T.prototype.getLocation=function(){return window.location},T.prototype.getBaseHref=function(t){var e=(A=A||document.querySelector("base"))?A.getAttribute("href"):null;return null==e?null:function(t){return(N=N||document.createElement("a")).setAttribute("href",t),"/"===N.pathname.charAt(0)?N.pathname:"/"+N.pathname}(e)},T.prototype.resetBaseElement=function(){A=null},T.prototype.getUserAgent=function(){return window.navigator.userAgent},T.prototype.setData=function(t,e,n){this.setAttribute(t,"data-"+e,n)},T.prototype.getData=function(t,e){return this.getAttribute(t,"data-"+e)},T.prototype.getComputedStyle=function(t){return getComputedStyle(t)},T.prototype.supportsWebAnimation=function(){return"function"==typeof Element.prototype.animate},T.prototype.performanceNow=function(){return window.performance&&window.performance.now?window.performance.now():(new Date).getTime()},T.prototype.supportsCookies=function(){return!0},T.prototype.getCookie=function(t){return r.ɵparseCookieValue(document.cookie,t)},T.prototype.setCookie=function(t,e){document.cookie=encodeURIComponent(t)+"="+encodeURIComponent(e)},T);function T(){return null!==v&&v.apply(this,arguments)||this}var N,A=null;function C(){return!!window.history.pushState}var O,I=(e(R,O=r.PlatformLocation),R.prototype._init=function(){this.location=c().getLocation(),this._history=c().getHistory()},R.prototype.getBaseHrefFromDOM=function(){return c().getBaseHref(this._doc)},R.prototype.onPopState=function(t){c().getGlobalEventTarget(this._doc,"window").addEventListener("popstate",t,!1)},R.prototype.onHashChange=function(t){c().getGlobalEventTarget(this._doc,"window").addEventListener("hashchange",t,!1)},Object.defineProperty(R.prototype,"href",{get:function(){return this.location.href},enumerable:!0,configurable:!0}),Object.defineProperty(R.prototype,"protocol",{get:function(){return this.location.protocol},enumerable:!0,configurable:!0}),Object.defineProperty(R.prototype,"hostname",{get:function(){return this.location.hostname},enumerable:!0,configurable:!0}),Object.defineProperty(R.prototype,"port",{get:function(){return this.location.port},enumerable:!0,configurable:!0}),Object.defineProperty(R.prototype,"pathname",{get:function(){return this.location.pathname},set:function(t){this.location.pathname=t},enumerable:!0,configurable:!0}),Object.defineProperty(R.prototype,"search",{get:function(){return this.location.search},enumerable:!0,configurable:!0}),Object.defineProperty(R.prototype,"hash",{get:function(){return this.location.hash},enumerable:!0,configurable:!0}),R.prototype.pushState=function(t,e,n){C()?this._history.pushState(t,e,n):this.location.hash=n},R.prototype.replaceState=function(t,e,n){C()?this._history.replaceState(t,e,n):this.location.hash=n},R.prototype.forward=function(){this._history.forward()},R.prototype.back=function(){this._history.back()},R.prototype.getState=function(){return this._history.state},R=i([f.Injectable(),a(0,f.Inject(r.DOCUMENT)),s("design:paramtypes",[Object])],R));function R(t){var e=O.call(this)||this;return e._doc=t,e._init(),e}var D=new f.InjectionToken("TRANSITION_ID");function M(n,t,e){return function(){e.get(f.ApplicationInitStatus).donePromise.then(function(){var e=c();Array.prototype.slice.apply(e.querySelectorAll(t,"style[ng-transition]")).filter(function(t){return e.getAttribute(t,"ng-transition")===n}).forEach(function(t){return e.remove(t)})})}}var P=[{provide:f.APP_INITIALIZER,useFactory:M,deps:[D,r.DOCUMENT,f.Injector],multi:!0}],L=(k.init=function(){f.setTestabilityGetter(new k)},k.prototype.addToWindow=function(o){f.ɵglobal.getAngularTestability=function(t,e){void 0===e&&(e=!0);var n=o.findTestabilityInTree(t,e);if(null==n)throw new Error("Could not find testability for element.");return n},f.ɵglobal.getAllAngularTestabilities=function(){return o.getAllTestabilities()},f.ɵglobal.getAllAngularRootElements=function(){return o.getAllRootElements()},f.ɵglobal.frameworkStabilizers||(f.ɵglobal.frameworkStabilizers=[]),f.ɵglobal.frameworkStabilizers.push(function(e){function n(t){r=r||t,0==--o&&e(r)}var t=f.ɵglobal.getAllAngularTestabilities(),o=t.length,r=!1;t.forEach(function(t){t.whenStable(n)})})},k.prototype.findTestabilityInTree=function(t,e,n){if(null==e)return null;var o=t.getTestability(e);return null!=o?o:n?c().isShadowRoot(e)?this.findTestabilityInTree(t,c().getHost(e),!0):this.findTestabilityInTree(t,c().parentElement(e),!0):null},k);function k(){}function j(t,e){"undefined"!=typeof COMPILED&&COMPILED||((f.ɵglobal.ng=f.ɵglobal.ng||{})[t]=e)}var x={ApplicationRef:f.ApplicationRef,NgZone:f.NgZone};function H(t){return f.getDebugNode(t)}function B(t){return j("probe",H),j("coreTokens",n({},x,function(t){return t.reduce(function(t,e){return t[e.name]=e.token,t},{})}(t||[]))),function(){return H}}var U=[{provide:f.APP_INITIALIZER,useFactory:B,deps:[[f.NgProbeToken,new f.Optional]],multi:!0}],V=U,z=new f.InjectionToken("EventManagerPlugins"),F=(Z.prototype.addEventListener=function(t,e,n){return this._findPluginFor(e).addEventListener(t,e,n)},Z.prototype.addGlobalEventListener=function(t,e,n){return this._findPluginFor(e).addGlobalEventListener(t,e,n)},Z.prototype.getZone=function(){return this._zone},Z.prototype._findPluginFor=function(t){var e=this._eventNameToPlugin.get(t);if(e)return e;for(var n=this._plugins,o=0;o<n.length;o++){var r=n[o];if(r.supports(t))return this._eventNameToPlugin.set(t,r),r}throw new Error("No event manager plugin found for event "+t)},Z=i([f.Injectable(),a(0,f.Inject(z)),s("design:paramtypes",[Array,f.NgZone])],Z));function Z(t,e){var n=this;this._zone=e,this._eventNameToPlugin=new Map,t.forEach(function(t){return t.manager=n}),this._plugins=t.slice().reverse()}var G=(K.prototype.addGlobalEventListener=function(t,e,n){var o=c().getGlobalEventTarget(this._doc,t);if(!o)throw new Error("Unsupported event target "+o+" for event "+e);return this.addEventListener(o,e,n)},K);function K(t){this._doc=t}var q=(W.prototype.addStyles=function(t){var e=this,n=new Set;t.forEach(function(t){e._stylesSet.has(t)||(e._stylesSet.add(t),n.add(t))}),this.onStylesAdded(n)},W.prototype.onStylesAdded=function(t){},W.prototype.getAllStyles=function(){return Array.from(this._stylesSet)},W=i([f.Injectable()],W));function W(){this._stylesSet=new Set}var J,X=(e(Y,J=q),Y.prototype._addStylesToHost=function(t,n){var o=this;t.forEach(function(t){var e=o._doc.createElement("style");e.textContent=t,o._styleNodes.add(n.appendChild(e))})},Y.prototype.addHost=function(t){this._addStylesToHost(this._stylesSet,t),this._hostNodes.add(t)},Y.prototype.removeHost=function(t){this._hostNodes.delete(t)},Y.prototype.onStylesAdded=function(e){var n=this;this._hostNodes.forEach(function(t){return n._addStylesToHost(e,t)})},Y.prototype.ngOnDestroy=function(){this._styleNodes.forEach(function(t){return c().remove(t)})},Y=i([f.Injectable(),a(0,f.Inject(r.DOCUMENT)),s("design:paramtypes",[Object])],Y));function Y(t){var e=J.call(this)||this;return e._doc=t,e._hostNodes=new Set,e._styleNodes=new Set,e._hostNodes.add(t.head),e}var Q={svg:"http://www.w3.org/2000/svg",xhtml:"http://www.w3.org/1999/xhtml",xlink:"http://www.w3.org/1999/xlink",xml:"http://www.w3.org/XML/1998/namespace",xmlns:"http://www.w3.org/2000/xmlns/"},$=/%COMP%/g,tt="%COMP%",et="_nghost-"+tt,nt="_ngcontent-"+tt;function ot(t){return nt.replace($,t)}function rt(t){return et.replace($,t)}function it(t,e,n){for(var o=0;o<e.length;o++){var r=e[o];Array.isArray(r)?it(t,r,n):(r=r.replace($,t),n.push(r))}return n}function at(e){return function(t){!1===e(t)&&(t.preventDefault(),t.returnValue=!1)}}var st=(ut.prototype.createRenderer=function(t,e){if(!t||!e)return this.defaultRenderer;switch(e.encapsulation){case f.ViewEncapsulation.Emulated:var n=this.rendererByCompId.get(e.id);return n||(n=new yt(this.eventManager,this.sharedStylesHost,e,this.appId),this.rendererByCompId.set(e.id,n)),n.applyToHost(t),n;case f.ViewEncapsulation.Native:case f.ViewEncapsulation.ShadowDom:return new gt(this.eventManager,this.sharedStylesHost,t,e);default:if(!this.rendererByCompId.has(e.id)){var o=it(e.id,e.styles,[]);this.sharedStylesHost.addStyles(o),this.rendererByCompId.set(e.id,this.defaultRenderer)}return this.defaultRenderer}},ut.prototype.begin=function(){},ut.prototype.end=function(){},ut=i([f.Injectable(),a(2,f.Inject(f.APP_ID)),s("design:paramtypes",[F,X,String])],ut));function ut(t,e,n){this.eventManager=t,this.sharedStylesHost=e,this.appId=n,this.rendererByCompId=new Map,this.defaultRenderer=new pt(t)}var pt=(ct.prototype.destroy=function(){},ct.prototype.createElement=function(t,e){return e?document.createElementNS(Q[e]||e,t):document.createElement(t)},ct.prototype.createComment=function(t){return document.createComment(t)},ct.prototype.createText=function(t){return document.createTextNode(t)},ct.prototype.appendChild=function(t,e){t.appendChild(e)},ct.prototype.insertBefore=function(t,e,n){t&&t.insertBefore(e,n)},ct.prototype.removeChild=function(t,e){t&&t.removeChild(e)},ct.prototype.selectRootElement=function(t,e){var n="string"==typeof t?document.querySelector(t):t;if(!n)throw new Error('The selector "'+t+'" did not match any elements');return e||(n.textContent=""),n},ct.prototype.parentNode=function(t){return t.parentNode},ct.prototype.nextSibling=function(t){return t.nextSibling},ct.prototype.setAttribute=function(t,e,n,o){if(o){e=o+":"+e;var r=Q[o];r?t.setAttributeNS(r,e,n):t.setAttribute(e,n)}else t.setAttribute(e,n)},ct.prototype.removeAttribute=function(t,e,n){if(n){var o=Q[n];o?t.removeAttributeNS(o,e):t.removeAttribute(n+":"+e)}else t.removeAttribute(e)},ct.prototype.addClass=function(t,e){t.classList.add(e)},ct.prototype.removeClass=function(t,e){t.classList.remove(e)},ct.prototype.setStyle=function(t,e,n,o){o&f.RendererStyleFlags2.DashCase?t.style.setProperty(e,n,o&f.RendererStyleFlags2.Important?"important":""):t.style[e]=n},ct.prototype.removeStyle=function(t,e,n){n&f.RendererStyleFlags2.DashCase?t.style.removeProperty(e):t.style[e]=""},ct.prototype.setProperty=function(t,e,n){ft(e,"property"),t[e]=n},ct.prototype.setValue=function(t,e){t.nodeValue=e},ct.prototype.listen=function(t,e,n){return ft(e,"listener"),"string"==typeof t?this.eventManager.addGlobalEventListener(t,e,at(n)):this.eventManager.addEventListener(t,e,at(n))},ct);function ct(t){this.eventManager=t,this.data=Object.create(null)}var lt="@".charCodeAt(0);function ft(t,e){if(t.charCodeAt(0)===lt)throw new Error("Found the synthetic "+e+" "+t+'. Please include either "BrowserAnimationsModule" or "NoopAnimationsModule" in your application.')}var dt,yt=(e(ht,dt=pt),ht.prototype.applyToHost=function(t){dt.prototype.setAttribute.call(this,t,this.hostAttr,"")},ht.prototype.createElement=function(t,e){var n=dt.prototype.createElement.call(this,t,e);return dt.prototype.setAttribute.call(this,n,this.contentAttr,""),n},ht);function ht(t,e,n,o){var r=dt.call(this,t)||this,i=it(o+"-"+(r.component=n).id,n.styles,[]);return e.addStyles(i),r.contentAttr=ot(o+"-"+n.id),r.hostAttr=rt(o+"-"+n.id),r}var mt,gt=(e(vt,mt=pt),vt.prototype.nodeOrShadowRoot=function(t){return t===this.hostEl?this.shadowRoot:t},vt.prototype.destroy=function(){this.sharedStylesHost.removeHost(this.shadowRoot)},vt.prototype.appendChild=function(t,e){return mt.prototype.appendChild.call(this,this.nodeOrShadowRoot(t),e)},vt.prototype.insertBefore=function(t,e,n){return mt.prototype.insertBefore.call(this,this.nodeOrShadowRoot(t),e,n)},vt.prototype.removeChild=function(t,e){return mt.prototype.removeChild.call(this,this.nodeOrShadowRoot(t),e)},vt.prototype.parentNode=function(t){return this.nodeOrShadowRoot(mt.prototype.parentNode.call(this,this.nodeOrShadowRoot(t)))},vt);function vt(t,e,n,o){var r=mt.call(this,t)||this;r.sharedStylesHost=e,r.hostEl=n,(r.component=o).encapsulation===f.ViewEncapsulation.ShadowDom?r.shadowRoot=n.attachShadow({mode:"open"}):r.shadowRoot=n.createShadowRoot(),r.sharedStylesHost.addHost(r.shadowRoot);for(var i=it(o.id,o.styles,[]),a=0;a<i.length;a++){var s=document.createElement("style");s.textContent=i[a],r.shadowRoot.appendChild(s)}return r}function wt(t){return!!It&&It.hasOwnProperty(t)}function bt(t){var e=Nt[t.type];if(e){var n=this[e];if(n){var o=[t];if(1===n.length)return(a=n[0]).zone!==Zone.current?a.zone.run(a.handler,this,o):a.handler.apply(this,o);for(var r=n.slice(),i=0;i<r.length&&!0!==t[Ct];i++){var a;(a=r[i]).zone!==Zone.current?a.zone.run(a.handler,this,o):a.handler.apply(this,o)}}}}var _t,Et="undefined"!=typeof Zone&&Zone.__symbol__||function(t){return"__zone_symbol__"+t},St=Et("addEventListener"),Tt=Et("removeEventListener"),Nt={},At="removeEventListener",Ct="__zone_symbol__propagationStopped",Ot="__zone_symbol__stopImmediatePropagation",It=function(){var t="undefined"!=typeof Zone&&Zone[Et("BLACK_LISTED_EVENTS")];if(t){var e={};return t.forEach(function(t){e[t]=t}),e}}(),Rt=(e(Dt,_t=G),Dt.prototype.patchEvent=function(){if("undefined"!=typeof Event&&Event&&Event.prototype&&!Event.prototype[Ot]){var t=Event.prototype[Ot]=Event.prototype.stopImmediatePropagation;Event.prototype.stopImmediatePropagation=function(){this&&(this[Ct]=!0),t&&t.apply(this,arguments)}}},Dt.prototype.supports=function(t){return!0},Dt.prototype.addEventListener=function(t,e,n){var o=this,r=t[St],i=n;if(!r||f.NgZone.isInAngularZone()&&!wt(e))t.addEventListener(e,i,!1);else{var a=Nt[e];a=a||(Nt[e]=Et("ANGULAR"+e+"FALSE"));var s=t[a],u=s&&0<s.length;s=s||(t[a]=[]);var p=wt(e)?Zone.root:Zone.current;if(0===s.length)s.push({zone:p,handler:i});else{for(var c=!1,l=0;l<s.length;l++)if(s[l].handler===i){c=!0;break}c||s.push({zone:p,handler:i})}u||t[St](e,bt,!1)}return function(){return o.removeEventListener(t,e,i)}},Dt.prototype.removeEventListener=function(t,e,n){var o=t[Tt];if(!o)return t[At].apply(t,[e,n,!1]);var r=Nt[e],i=r&&t[r];if(!i)return t[At].apply(t,[e,n,!1]);for(var a=!1,s=0;s<i.length;s++)if(i[s].handler===n){a=!0,i.splice(s,1);break}a?0===i.length&&o.apply(t,[e,bt,!1]):t[At].apply(t,[e,n,!1])},Dt=i([f.Injectable(),a(0,f.Inject(r.DOCUMENT)),a(2,f.Optional()),a(2,f.Inject(f.PLATFORM_ID)),s("design:paramtypes",[Object,f.NgZone,Object])],Dt));function Dt(t,e,n){var o=_t.call(this,t)||this;return o.ngZone=e,n&&r.isPlatformServer(n)||o.patchEvent(),o}var Mt={pan:!0,panstart:!0,panmove:!0,panend:!0,pancancel:!0,panleft:!0,panright:!0,panup:!0,pandown:!0,pinch:!0,pinchstart:!0,pinchmove:!0,pinchend:!0,pinchcancel:!0,pinchin:!0,pinchout:!0,press:!0,pressup:!0,rotate:!0,rotatestart:!0,rotatemove:!0,rotateend:!0,rotatecancel:!0,swipe:!0,swipeleft:!0,swiperight:!0,swipeup:!0,swipedown:!0,tap:!0},Pt=new f.InjectionToken("HammerGestureConfig"),Lt=new f.InjectionToken("HammerLoader"),kt=(jt.prototype.buildHammer=function(t){var e=new Hammer(t,this.options);for(var n in e.get("pinch").set({enable:!0}),e.get("rotate").set({enable:!0}),this.overrides)e.get(n).set(this.overrides[n]);return e},jt=i([f.Injectable()],jt));function jt(){this.events=[],this.overrides={}}var xt,Ht=(e(Bt,xt=G),Bt.prototype.supports=function(t){return!(!Mt.hasOwnProperty(t.toLowerCase())&&!this.isCustomEvent(t)||!window.Hammer&&!this.loader&&(this.console.warn('The "'+t+'" event cannot be bound because Hammer.JS is not loaded and no custom loader has been specified.'),1))},Bt.prototype.addEventListener=function(n,o,r){var i=this,a=this.manager.getZone();if(o=o.toLowerCase(),window.Hammer||!this.loader)return a.runOutsideAngular(function(){function t(t){a.runGuarded(function(){r(t)})}var e=i._config.buildHammer(n);return e.on(o,t),function(){e.off(o,t),"function"==typeof e.destroy&&e.destroy()}});var t=!1,e=function(){t=!0};return this.loader().then(function(){if(!window.Hammer)return i.console.warn("The custom HAMMER_LOADER completed, but Hammer.JS is not present."),void(e=function(){});t||(e=i.addEventListener(n,o,r))}).catch(function(){i.console.warn('The "'+o+'" event cannot be bound because the custom Hammer.JS loader failed.'),e=function(){}}),function(){e()}},Bt.prototype.isCustomEvent=function(t){return-1<this._config.events.indexOf(t)},Bt=i([f.Injectable(),a(0,f.Inject(r.DOCUMENT)),a(1,f.Inject(Pt)),a(3,f.Optional()),a(3,f.Inject(Lt)),s("design:paramtypes",[Object,kt,f.ɵConsole,Object])],Bt));function Bt(t,e,n,o){var r=xt.call(this,t)||this;return r._config=e,r.console=n,r.loader=o,r}var Ut,Vt,zt=["alt","control","meta","shift"],Ft={alt:function(t){return t.altKey},control:function(t){return t.ctrlKey},meta:function(t){return t.metaKey},shift:function(t){return t.shiftKey}},Zt=(e(Gt,Ut=G),(Vt=Gt).prototype.supports=function(t){return null!=Vt.parseEventName(t)},Gt.prototype.addEventListener=function(t,e,n){var o=Vt.parseEventName(e),r=Vt.eventCallback(o.fullKey,n,this.manager.getZone());return this.manager.getZone().runOutsideAngular(function(){return c().onAndCancel(t,o.domEventName,r)})},Gt.parseEventName=function(t){var n=t.toLowerCase().split("."),e=n.shift();if(0===n.length||"keydown"!==e&&"keyup"!==e)return null;var o=Vt._normalizeKey(n.pop()),r="";if(zt.forEach(function(t){var e=n.indexOf(t);-1<e&&(n.splice(e,1),r+=t+".")}),r+=o,0!=n.length||0===o.length)return null;var i={};return i.domEventName=e,i.fullKey=r,i},Gt.getEventFullKey=function(e){var n="",o=c().getEventKey(e);return" "===(o=o.toLowerCase())?o="space":"."===o&&(o="dot"),zt.forEach(function(t){t!=o&&(0,Ft[t])(e)&&(n+=t+".")}),n+=o},Gt.eventCallback=function(e,n,o){return function(t){Vt.getEventFullKey(t)===e&&o.runGuarded(function(){return n(t)})}},Gt._normalizeKey=function(t){switch(t){case"esc":return"escape";default:return t}},Gt=Vt=i([f.Injectable(),a(0,f.Inject(r.DOCUMENT)),s("design:paramtypes",[Object])],Gt));function Gt(t){return Ut.call(this,t)||this}function Kt(){}var qt,Wt=(e(Jt,qt=Kt),Jt.prototype.sanitize=function(t,e){if(null==e)return null;switch(t){case f.SecurityContext.NONE:return e;case f.SecurityContext.HTML:return e instanceof $t?e.changingThisBreaksApplicationSecurity:(this.checkNotSafeValue(e,"HTML"),f.ɵ_sanitizeHtml(this._doc,String(e)));case f.SecurityContext.STYLE:return e instanceof ne?e.changingThisBreaksApplicationSecurity:(this.checkNotSafeValue(e,"Style"),f.ɵ_sanitizeStyle(e));case f.SecurityContext.SCRIPT:if(e instanceof ie)return e.changingThisBreaksApplicationSecurity;throw this.checkNotSafeValue(e,"Script"),new Error("unsafe value used in a script context");case f.SecurityContext.URL:return e instanceof le||e instanceof ue?e.changingThisBreaksApplicationSecurity:(this.checkNotSafeValue(e,"URL"),f.ɵ_sanitizeUrl(String(e)));case f.SecurityContext.RESOURCE_URL:if(e instanceof le)return e.changingThisBreaksApplicationSecurity;throw this.checkNotSafeValue(e,"ResourceURL"),new Error("unsafe value used in a resource URL context (see http://g.co/ng/security#xss)");default:throw new Error("Unexpected SecurityContext "+t+" (see http://g.co/ng/security#xss)")}},Jt.prototype.checkNotSafeValue=function(t,e){if(t instanceof Xt)throw new Error("Required a safe "+e+", got a "+t.getTypeName()+" (see http://g.co/ng/security#xss)")},Jt.prototype.bypassSecurityTrustHtml=function(t){return new $t(t)},Jt.prototype.bypassSecurityTrustStyle=function(t){return new ne(t)},Jt.prototype.bypassSecurityTrustScript=function(t){return new ie(t)},Jt.prototype.bypassSecurityTrustUrl=function(t){return new ue(t)},Jt.prototype.bypassSecurityTrustResourceUrl=function(t){return new le(t)},Jt=i([f.Injectable(),a(0,f.Inject(r.DOCUMENT)),s("design:paramtypes",[Object])],Jt));function Jt(t){var e=qt.call(this)||this;return e._doc=t,e}var Xt=(Yt.prototype.toString=function(){return"SafeValue must use [property]=binding: "+this.changingThisBreaksApplicationSecurity+" (see http://g.co/ng/security#xss)"},Yt);function Yt(t){this.changingThisBreaksApplicationSecurity=t}var Qt,$t=(e(te,Qt=Xt),te.prototype.getTypeName=function(){return"HTML"},te);function te(){return null!==Qt&&Qt.apply(this,arguments)||this}var ee,ne=(e(oe,ee=Xt),oe.prototype.getTypeName=function(){return"Style"},oe);function oe(){return null!==ee&&ee.apply(this,arguments)||this}var re,ie=(e(ae,re=Xt),ae.prototype.getTypeName=function(){return"Script"},ae);function ae(){return null!==re&&re.apply(this,arguments)||this}var se,ue=(e(pe,se=Xt),pe.prototype.getTypeName=function(){return"URL"},pe);function pe(){return null!==se&&se.apply(this,arguments)||this}var ce,le=(e(fe,ce=Xt),fe.prototype.getTypeName=function(){return"ResourceURL"},fe);function fe(){return null!==ce&&ce.apply(this,arguments)||this}var de=r.ɵPLATFORM_BROWSER_ID,ye=[{provide:f.PLATFORM_ID,useValue:de},{provide:f.PLATFORM_INITIALIZER,useValue:ge,multi:!0},{provide:r.PlatformLocation,useClass:I,deps:[r.DOCUMENT]},{provide:r.DOCUMENT,useFactory:we,deps:[]}],he=[{provide:f.Sanitizer,useExisting:Kt},{provide:Kt,useClass:Wt,deps:[r.DOCUMENT]}],me=f.createPlatformFactory(f.platformCore,"browser",ye);function ge(){S.makeCurrent(),L.init()}function ve(){return new f.ErrorHandler}function we(){return document}var be,_e=[he,{provide:f.ɵAPP_ROOT,useValue:!0},{provide:f.ErrorHandler,useFactory:ve,deps:[]},{provide:z,useClass:Rt,multi:!0,deps:[r.DOCUMENT,f.NgZone,f.PLATFORM_ID]},{provide:z,useClass:Zt,multi:!0,deps:[r.DOCUMENT]},{provide:z,useClass:Ht,multi:!0,deps:[r.DOCUMENT,Pt,f.ɵConsole,[new f.Optional,Lt]]},{provide:Pt,useClass:kt,deps:[]},{provide:st,useClass:st,deps:[F,X,f.APP_ID]},{provide:f.RendererFactory2,useExisting:st},{provide:q,useExisting:X},{provide:X,useClass:X,deps:[r.DOCUMENT]},{provide:f.Testability,useClass:f.Testability,deps:[f.NgZone]},{provide:F,useClass:F,deps:[z,f.NgZone]},V],Ee=((be=Se).withServerTransition=function(t){return{ngModule:be,providers:[{provide:f.APP_ID,useValue:t.appId},{provide:D,useExisting:f.APP_ID},P]}},Se=be=i([f.NgModule({providers:_e,exports:[r.CommonModule,f.ApplicationModule]}),a(0,f.Optional()),a(0,f.SkipSelf()),a(0,f.Inject(be)),s("design:paramtypes",[Object])],Se));function Se(t){if(t)throw new Error("BrowserModule has already been loaded. If you need access to common directives such as NgIf and NgFor from a lazy loaded module, import CommonModule instead.")}function Te(){return new Ne(f.ɵɵinject(r.DOCUMENT))}var Ne=(Ae.prototype.addTag=function(t,e){return void 0===e&&(e=!1),t?this._getOrCreateElement(t,e):null},Ae.prototype.addTags=function(t,n){var o=this;return void 0===n&&(n=!1),t?t.reduce(function(t,e){return e&&t.push(o._getOrCreateElement(e,n)),t},[]):[]},Ae.prototype.getTag=function(t){return t&&this._dom.querySelector(this._doc,"meta["+t+"]")||null},Ae.prototype.getTags=function(t){if(!t)return[];var e=this._dom.querySelectorAll(this._doc,"meta["+t+"]");return e?[].slice.call(e):[]},Ae.prototype.updateTag=function(t,e){if(!t)return null;e=e||this._parseSelector(t);var n=this.getTag(e);return n?this._setMetaElementAttributes(t,n):this._getOrCreateElement(t,!0)},Ae.prototype.removeTag=function(t){this.removeTagElement(this.getTag(t))},Ae.prototype.removeTagElement=function(t){t&&this._dom.remove(t)},Ae.prototype._getOrCreateElement=function(t,e){if(void 0===e&&(e=!1),!e){var n=this._parseSelector(t),o=this.getTag(n);if(o&&this._containsAttributes(t,o))return o}var r=this._dom.createElement("meta");this._setMetaElementAttributes(t,r);var i=this._dom.getElementsByTagName(this._doc,"head")[0];return this._dom.appendChild(i,r),r},Ae.prototype._setMetaElementAttributes=function(e,n){var o=this;return Object.keys(e).forEach(function(t){return o._dom.setAttribute(n,t,e[t])}),n},Ae.prototype._parseSelector=function(t){var e=t.name?"name":"property";return e+'="'+t[e]+'"'},Ae.prototype._containsAttributes=function(e,n){var o=this;return Object.keys(e).every(function(t){return o._dom.getAttribute(n,t)===e[t]})},Ae.ngInjectableDef=f.ɵɵdefineInjectable({factory:Te,token:Ae,providedIn:"root"}),Ae=i([f.Injectable({providedIn:"root",useFactory:Te,deps:[]}),a(0,f.Inject(r.DOCUMENT)),s("design:paramtypes",[Object])],Ae));function Ae(t){this._doc=t,this._dom=c()}function Ce(){return new Oe(f.ɵɵinject(r.DOCUMENT))}var Oe=(Ie.prototype.getTitle=function(){return c().getTitle(this._doc)},Ie.prototype.setTitle=function(t){c().setTitle(this._doc,t)},Ie.ngInjectableDef=f.ɵɵdefineInjectable({factory:Ce,token:Ie,providedIn:"root"}),Ie=i([f.Injectable({providedIn:"root",useFactory:Ce,deps:[]}),a(0,f.Inject(r.DOCUMENT)),s("design:paramtypes",[Object])],Ie));function Ie(t){this._doc=t}var Re="undefined"!=typeof window&&window||{},De=function(t,e){this.msPerTick=t,this.numTicks=e},Me=(Pe.prototype.timeChangeDetection=function(t){var e=t&&t.record,n="Change Detection",o=null!=Re.console.profile;e&&o&&Re.console.profile(n);for(var r=c().performanceNow(),i=0;i<5||c().performanceNow()-r<500;)this.appRef.tick(),i++;var a=c().performanceNow();e&&o&&Re.console.profileEnd(n);var s=(a-r)/i;return Re.console.log("ran "+i+" change detection cycles"),Re.console.log(s.toFixed(2)+" ms per check"),new De(s,i)},Pe);function Pe(t){this.appRef=t.injector.get(f.ApplicationRef)}var Le="profiler";var ke,je=((ke=xe).init=function(t){var e=new ke;return e.store=t,e},xe.prototype.get=function(t,e){return void 0!==this.store[t]?this.store[t]:e},xe.prototype.set=function(t,e){this.store[t]=e},xe.prototype.remove=function(t){delete this.store[t]},xe.prototype.hasKey=function(t){return this.store.hasOwnProperty(t)},xe.prototype.onSerialize=function(t,e){this.onSerializeCallbacks[t]=e},xe.prototype.toJson=function(){for(var t in this.onSerializeCallbacks)if(this.onSerializeCallbacks.hasOwnProperty(t))try{this.store[t]=this.onSerializeCallbacks[t]()}catch(t){console.warn("Exception in onSerialize callback: ",t)}return JSON.stringify(this.store)},xe=ke=i([f.Injectable()],xe));function xe(){this.store={},this.onSerializeCallbacks={}}function He(t,e){var n=t.getElementById(e+"-state"),o={};if(n&&n.textContent)try{o=JSON.parse(function(t){var e={"&a;":"&","&q;":'"',"&s;":"'","&l;":"<","&g;":">"};return t.replace(/&[^;]+;/g,function(t){return e[t]})}(n.textContent))}catch(t){console.warn("Exception while restoring TransferState for app "+e,t)}return je.init(o)}var Be=Ue=i([f.NgModule({providers:[{provide:je,useFactory:He,deps:[r.DOCUMENT,f.APP_ID]}]})],Ue);function Ue(){}var Ve=(ze.all=function(){return function(t){return!0}},ze.css=function(e){return function(t){return null!=t.nativeElement&&c().elementMatches(t.nativeElement,e)}},ze.directive=function(e){return function(t){return-1!==t.providerTokens.indexOf(e)}},ze);function ze(){}var Fe=new f.Version("8.0.3");t.ɵangular_packages_platform_browser_platform_browser_c=_e,t.ɵangular_packages_platform_browser_platform_browser_b=we,t.ɵangular_packages_platform_browser_platform_browser_a=ve,t.ɵangular_packages_platform_browser_platform_browser_l=m,t.ɵangular_packages_platform_browser_platform_browser_d=Te,t.ɵangular_packages_platform_browser_platform_browser_i=P,t.ɵangular_packages_platform_browser_platform_browser_h=M,t.ɵangular_packages_platform_browser_platform_browser_e=Ce,t.ɵangular_packages_platform_browser_platform_browser_f=He,t.ɵangular_packages_platform_browser_platform_browser_k=U,t.ɵangular_packages_platform_browser_platform_browser_j=B,t.ɵangular_packages_platform_browser_platform_browser_g=G,t.BrowserModule=Ee,t.platformBrowser=me,t.Meta=Ne,t.Title=Oe,t.disableDebugTools=function(){j(Le,null)},t.enableDebugTools=function(t){return j(Le,new Me(t)),t},t.BrowserTransferStateModule=Be,t.TransferState=je,t.makeStateKey=function(t){return t},t.By=Ve,t.EVENT_MANAGER_PLUGINS=z,t.EventManager=F,t.HAMMER_GESTURE_CONFIG=Pt,t.HAMMER_LOADER=Lt,t.HammerGestureConfig=kt,t.DomSanitizer=Kt,t.VERSION=Fe,t.ɵELEMENT_PROBE_PROVIDERS__POST_R3__=[],t.ɵBROWSER_SANITIZATION_PROVIDERS=he,t.ɵINTERNAL_BROWSER_PLATFORM_PROVIDERS=ye,t.ɵinitDomAdapter=ge,t.ɵBrowserDomAdapter=S,t.ɵBrowserPlatformLocation=I,t.ɵTRANSITION_ID=D,t.ɵBrowserGetTestability=L,t.ɵescapeHtml=function(t){var e={"&":"&a;",'"':"&q;","'":"&s;","<":"&l;",">":"&g;"};return t.replace(/[&"'<>]/g,function(t){return e[t]})},t.ɵELEMENT_PROBE_PROVIDERS=V,t.ɵDomAdapter=d,t.ɵgetDOM=c,t.ɵsetRootDomAdapter=l,t.ɵDomRendererFactory2=st,t.ɵNAMESPACE_URIS=Q,t.ɵflattenStyles=it,t.ɵshimContentAttribute=ot,t.ɵshimHostAttribute=rt,t.ɵDomEventsPlugin=Rt,t.ɵHammerGesturesPlugin=Ht,t.ɵKeyEventsPlugin=Zt,t.ɵDomSharedStylesHost=X,t.ɵSharedStylesHost=q,t.ɵDomSanitizerImpl=Wt,Object.defineProperty(t,"__esModule",{value:!0})});