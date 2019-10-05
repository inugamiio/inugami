!function(e,t){"object"==typeof exports&&"undefined"!=typeof module?t(exports,require("@angular/core"),require("@angular/platform-browser"),require("@angular/animations"),require("@angular/animations/browser"),require("@angular/common")):"function"==typeof define&&define.amd?define("@angular/platform-browser/animations",["exports","@angular/core","@angular/platform-browser","@angular/animations","@angular/animations/browser","@angular/common"],t):t(((e=e||self).ng=e.ng||{},e.ng.platformBrowser=e.ng.platformBrowser||{},e.ng.platformBrowser.animations={}),e.ng.core,e.ng.platformBrowser,e.ng.animations,e.ng.animations.browser,e.ng.common)}(this,function(e,i,t,r,n,o){"use strict";var a=function(e,t){return(a=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,t){e.__proto__=t}||function(e,t){for(var n in t)t.hasOwnProperty(n)&&(e[n]=t[n])})(e,t)};function s(e,t){function n(){this.constructor=e}a(e,t),e.prototype=null===t?Object.create(t):(n.prototype=t.prototype,new n)}function u(e,t,n,r){var i,o=arguments.length,a=o<3?t:null===r?r=Object.getOwnPropertyDescriptor(t,n):r;if("object"==typeof Reflect&&"function"==typeof Reflect.decorate)a=Reflect.decorate(e,t,n,r);else for(var s=e.length-1;0<=s;s--)(i=e[s])&&(a=(o<3?i(a):3<o?i(t,n,a):i(t,n))||a);return 3<o&&a&&Object.defineProperty(t,n,a),a}function c(n,r){return function(e,t){r(e,t,n)}}function l(e,t){if("object"==typeof Reflect&&"function"==typeof Reflect.metadata)return Reflect.metadata(e,t)}function d(e,t){var n="function"==typeof Symbol&&e[Symbol.iterator];if(!n)return e;var r,i,o=n.call(e),a=[];try{for(;(void 0===t||0<t--)&&!(r=o.next()).done;)a.push(r.value)}catch(e){i={error:e}}finally{try{r&&!r.done&&(n=o.return)&&n.call(o)}finally{if(i)throw i.error}}return a}function p(){for(var e=[],t=0;t<arguments.length;t++)e=e.concat(d(arguments[t]));return e}var h,f=(s(m,h=r.AnimationBuilder),m.prototype.build=function(e){var t=this._nextAnimationId.toString();this._nextAnimationId++;var n=Array.isArray(e)?r.sequence(e):e;return A(this._renderer,null,t,"register",[n]),new g(t,this._renderer)},m=u([i.Injectable(),c(1,i.Inject(o.DOCUMENT)),l("design:paramtypes",[i.RendererFactory2,Object])],m));function m(e,t){var n=h.call(this)||this;n._nextAnimationId=0;var r={id:"0",encapsulation:i.ViewEncapsulation.None,styles:[],data:{animation:[]}};return n._renderer=e.createRenderer(t.body,r),n}var y,g=(s(_,y=r.AnimationFactory),_.prototype.create=function(e,t){return new v(this._id,e,t||{},this._renderer)},_);function _(e,t){var n=y.call(this)||this;return n._id=e,n._renderer=t,n}var v=(b.prototype._listen=function(e,t){return this._renderer.listen(this.element,"@@"+this.id+":"+e,t)},b.prototype._command=function(e){for(var t=[],n=1;n<arguments.length;n++)t[n-1]=arguments[n];return A(this._renderer,this.element,this.id,e,t)},b.prototype.onDone=function(e){this._listen("done",e)},b.prototype.onStart=function(e){this._listen("start",e)},b.prototype.onDestroy=function(e){this._listen("destroy",e)},b.prototype.init=function(){this._command("init")},b.prototype.hasStarted=function(){return this._started},b.prototype.play=function(){this._command("play"),this._started=!0},b.prototype.pause=function(){this._command("pause")},b.prototype.restart=function(){this._command("restart")},b.prototype.finish=function(){this._command("finish")},b.prototype.destroy=function(){this._command("destroy")},b.prototype.reset=function(){this._command("reset")},b.prototype.setPosition=function(e){this._command("setPosition",e)},b.prototype.getPosition=function(){return 0},b);function b(e,t,n,r){this.id=e,this.element=t,this._renderer=r,this.parentPlayer=null,this._started=!1,this.totalTime=0,this._command("create",n)}function A(e,t,n,r,i){return e.setProperty(t,"@@"+n+":"+r,i)}var w="@",C="@.disabled",I=(R.prototype.createRenderer=function(t,e){var n=this,r=this.delegate.createRenderer(t,e);if(!(t&&e&&e.data&&e.data.animation)){var i=this._rendererCache.get(r);return i||(i=new N("",r,this.engine),this._rendererCache.set(r,i)),i}var o=e.id,a=e.id+"-"+this._currentId;return this._currentId++,this.engine.register(a,t),e.data.animation.forEach(function(e){return n.engine.registerTrigger(o,a,t,e.name,e)}),new D(this,a,r,this.engine)},R.prototype.begin=function(){this._cdRecurDepth++,this.delegate.begin&&this.delegate.begin()},R.prototype._scheduleCountTask=function(){var e=this;this.promise.then(function(){e._microtaskId++})},R.prototype.scheduleListenerCallback=function(e,t,n){var r=this;0<=e&&e<this._microtaskId?this._zone.run(function(){return t(n)}):(0==this._animationCallbacksBuffer.length&&Promise.resolve(null).then(function(){r._zone.run(function(){r._animationCallbacksBuffer.forEach(function(e){var t=d(e,2);(0,t[0])(t[1])}),r._animationCallbacksBuffer=[]})}),this._animationCallbacksBuffer.push([t,n]))},R.prototype.end=function(){var e=this;this._cdRecurDepth--,0==this._cdRecurDepth&&this._zone.runOutsideAngular(function(){e._scheduleCountTask(),e.engine.flush(e._microtaskId)}),this.delegate.end&&this.delegate.end()},R.prototype.whenRenderingDone=function(){return this.engine.whenRenderingDone()},R=u([i.Injectable(),l("design:paramtypes",[i.RendererFactory2,n.ɵAnimationEngine,i.NgZone])],R));function R(e,t,n){this.delegate=e,this.engine=t,this._zone=n,this._currentId=0,this._microtaskId=1,this._animationCallbacksBuffer=[],this._rendererCache=new Map,this._cdRecurDepth=0,this.promise=Promise.resolve(0),t.onRemovalComplete=function(e,t){t&&t.parentNode(e)&&t.removeChild(e.parentNode,e)}}var N=(Object.defineProperty(k.prototype,"data",{get:function(){return this.delegate.data},enumerable:!0,configurable:!0}),k.prototype.destroy=function(){this.engine.destroy(this.namespaceId,this.delegate),this.delegate.destroy()},k.prototype.createElement=function(e,t){return this.delegate.createElement(e,t)},k.prototype.createComment=function(e){return this.delegate.createComment(e)},k.prototype.createText=function(e){return this.delegate.createText(e)},k.prototype.appendChild=function(e,t){this.delegate.appendChild(e,t),this.engine.onInsert(this.namespaceId,t,e,!1)},k.prototype.insertBefore=function(e,t,n){this.delegate.insertBefore(e,t,n),this.engine.onInsert(this.namespaceId,t,e,!0)},k.prototype.removeChild=function(e,t,n){this.engine.onRemove(this.namespaceId,t,this.delegate,n)},k.prototype.selectRootElement=function(e,t){return this.delegate.selectRootElement(e,t)},k.prototype.parentNode=function(e){return this.delegate.parentNode(e)},k.prototype.nextSibling=function(e){return this.delegate.nextSibling(e)},k.prototype.setAttribute=function(e,t,n,r){this.delegate.setAttribute(e,t,n,r)},k.prototype.removeAttribute=function(e,t,n){this.delegate.removeAttribute(e,t,n)},k.prototype.addClass=function(e,t){this.delegate.addClass(e,t)},k.prototype.removeClass=function(e,t){this.delegate.removeClass(e,t)},k.prototype.setStyle=function(e,t,n,r){this.delegate.setStyle(e,t,n,r)},k.prototype.removeStyle=function(e,t,n){this.delegate.removeStyle(e,t,n)},k.prototype.setProperty=function(e,t,n){t.charAt(0)==w&&t==C?this.disableAnimations(e,!!n):this.delegate.setProperty(e,t,n)},k.prototype.setValue=function(e,t){this.delegate.setValue(e,t)},k.prototype.listen=function(e,t,n){return this.delegate.listen(e,t,n)},k.prototype.disableAnimations=function(e,t){this.engine.disableAnimations(e,t)},k);function k(e,t,n){this.namespaceId=e,this.delegate=t,this.engine=n,this.destroyNode=this.delegate.destroyNode?function(e){return t.destroyNode(e)}:null}var B,D=(s(j,B=N),j.prototype.setProperty=function(e,t,n){t.charAt(0)==w?"."==t.charAt(1)&&t==C?(n=void 0===n||!!n,this.disableAnimations(e,n)):this.engine.process(this.namespaceId,e,t.substr(1),n):this.delegate.setProperty(e,t,n)},j.prototype.listen=function(e,t,n){var r,i=this;if(t.charAt(0)!=w)return this.delegate.listen(e,t,n);var o=function(e){switch(e){case"body":return document.body;case"document":return document;case"window":return window;default:return e}}(e),a=t.substr(1),s="";return a.charAt(0)!=w&&(a=(r=d(function(e){var t=e.indexOf("."),n=e.substring(0,t),r=e.substr(t+1);return[n,r]}(a),2))[0],s=r[1]),this.engine.listen(this.namespaceId,o,a,s,function(e){var t=e._data||-1;i.factory.scheduleListenerCallback(t,n,e)})},j);function j(e,t,n,r){var i=B.call(this,t,n,r)||this;return i.factory=e,i.namespaceId=t,i}var P,O=(s(E,P=n.ɵAnimationEngine),E=u([i.Injectable(),c(0,i.Inject(o.DOCUMENT)),l("design:paramtypes",[Object,n.AnimationDriver,n.ɵAnimationStyleNormalizer])],E));function E(e,t,n){return P.call(this,e.body,t,n)||this}function x(){return n.ɵsupportsWebAnimations()?new n.ɵWebAnimationsDriver:new n.ɵCssKeyframesDriver}function S(){return new n.ɵWebAnimationsStyleNormalizer}function M(e,t,n){return new I(e,t,n)}var T=new i.InjectionToken("AnimationModuleType"),F=[{provide:r.AnimationBuilder,useClass:f},{provide:n.ɵAnimationStyleNormalizer,useFactory:S},{provide:n.ɵAnimationEngine,useClass:O},{provide:i.RendererFactory2,useFactory:M,deps:[t.ɵDomRendererFactory2,n.ɵAnimationEngine,i.NgZone]}],z=p([{provide:n.AnimationDriver,useFactory:x},{provide:T,useValue:"BrowserAnimations"}],F),q=p([{provide:n.AnimationDriver,useClass:n.ɵNoopAnimationDriver},{provide:T,useValue:"NoopAnimations"}],F),V=L=u([i.NgModule({exports:[t.BrowserModule],providers:z})],L);function L(){}var U=W=u([i.NgModule({exports:[t.BrowserModule],providers:q})],W);function W(){}e.ɵangular_packages_platform_browser_animations_animations_f=N,e.ɵangular_packages_platform_browser_animations_animations_d=z,e.ɵangular_packages_platform_browser_animations_animations_e=q,e.ɵangular_packages_platform_browser_animations_animations_b=S,e.ɵangular_packages_platform_browser_animations_animations_c=M,e.ɵangular_packages_platform_browser_animations_animations_a=x,e.BrowserAnimationsModule=V,e.NoopAnimationsModule=U,e.ANIMATION_MODULE_TYPE=T,e.ɵBrowserAnimationBuilder=f,e.ɵBrowserAnimationFactory=g,e.ɵAnimationRenderer=D,e.ɵAnimationRendererFactory=I,e.ɵInjectableAnimationEngine=O,Object.defineProperty(e,"__esModule",{value:!0})});