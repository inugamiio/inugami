import { Component, OnInit, OnDestroy }         from '@angular/core';
import { ActivatedRoute,Router,NavigationEnd }  from '@angular/router';

import {SessionScope}                           from './scopes/session.scope';
import {SecurityServices}                       from './services/security.services'
import {SystemNotification}                     from './components/system_notification/system.notification';
import {HttpServices}                           from './services/http/http.services'
import {PluginsService}                         from './services/plugins.service'
import {MainMenu}                       from './components/main_menu/main.menu';

@Component({
    selector: 'app-component',
    templateUrl: 'js/app/app-component.html'
    directives : [SystemNotification,MainMenu]
})
export class AppComponent {
    /**************************************************************************
    * ATTRIBUTES
    **************************************************************************/
    private LOGGER  : any = org.inugami.logger.factory("org.inugami.app.AppComponent");

    

    /**************************************************************************
    * CONSTRUCTOR
    **************************************************************************/
    
    constructor(private route            : ActivatedRoute,
                private router           : Router,
                private sessionScope     : SessionScope,
                private securityServices : SecurityServices,
                private httpServices     : HttpServices,
                private pluginsService   : PluginsService) {

      org.inugami.sse.httpConnector =httpServices;
            
      router.events.subscribe((val) => {
        if(val instanceof NavigationEnd){
          let realUrl =val.url;
          if(val.url.startsWith('/?page=')){
            realUrl = val.url.substring(7,val.url.length).replace('%2F','/');
          }
          let urlPart =  realUrl.split("/");
          let cssPart = ["inugami"].concat(urlPart);
          let css = cssPart.join(' ');

          let body =  document.getElementsByTagName('body')[0];
          body.setAttribute("class",css);

          let title = document.getElementsByTagName('title')[0];
          title.text = "Inugami " +urlPart.join(" ");
        }
      });

      var self = this;
      org.inugami.events.addEventListener(org.inugami.events.type.APP_COMPONENTS_EVENT, function(event) {
          var preEvent = org.inugami.events.type.APP_COMPONENTS_EVENT_CALLBACK_BEFORE;
          var callBackEvent = org.inugami.events.type.APP_COMPONENTS_EVENT_CALLBACK;
          var eventData = null;
          var handler = null;

          if(isNotNull(event.detail)){
              if(isNotNull(event.detail.data)){
                  eventData = event.detail.data;
              }

              if(isNotNull(event.detail.preEvent)){
                preEvent = event.detail.preEvent;
              }

              if(isNotNull(event.detail.event)){
                callBackEvent = event.detail.event;
              }

              if(isNotNull(event.detail.handler)){
                handler = org.inugami.services.getFunction(event.detail.handler);
              }
          }

          var data = {
              route:self.route,
              router:self.router,
              sessionScope:self.sessionScope,
              data:eventData
          };


          if(isNotNull(preEvent)){
            org.inugami.events.fireEvent(preEvent, data);
          }

          if(isNotNull(handler)){
            handler(data);
          }
          org.inugami.events.fireEvent(callBackEvent, data);
      });
    }

    /**************************************************************************
    * ON INIT
    **************************************************************************/

    public ngOnInit() {
      this.sub = this.route.queryParams.subscribe(params => {
         this.sessionScope.openMainMenu();
         let redirectPage = "/";

         if(params['page']!==undefined){
              redirectPage = '/'+params['page'];
              let token = params['token'];


              if(isNotNull(token)){
                localStorage.setItem(org.inugami.constants.token,token);
              }
              
              
              if(!this.sessionScope.isConnected()){
                this.sessionScope._postRedirect = redirectPage;
                this.router.navigate(['/login']);
              }else{
                this.router.navigate(['/'+params['page']])
                org.inugami.events.updateResize();
                org.inugami.sse.filterEvent = null; 
             }
         }
      });
    }
}
