import {Component,OnInit,OnDestroy}     from '@angular/core';
import {ActivatedRoute}                 from '@angular/router';

import {AdminService}                   from './../services/admin.services'
import {Plugin}                         from './../models/plugin';
import {PluginsService}                 from './../services/plugins.service';
import {PluginInfos}                    from './../components/plugin_infos/plugin.info';
import {SessionScope}                   from './../scopes/session.scope';
import {ValueBloc}                      from './../components/charts/value_bloc/value.bloc';
import {MainMenuService,
        MAIN_MENU_ON_CLICK}             from './../components/main_menu/main.menu.service';
import {MainMenuLink}                   from './../components/main_menu/main.menu.link';

@Component({
    templateUrl: 'js/app/view/admin.view.html',
    directives : [PluginInfos,ValueBloc]
})
export class AdminView implements OnInit, OnDestroy{

    /**************************************************************************
    * ATTRIBUTES
    **************************************************************************/
    private msgKonami            : string;
    private konamiMode           : boolean;
    private lastTimeUp           : any     = null;
    private lastTimeDown         : any     = null;
    private upTime               : string  = "";
    private serverUp             : boolean = false;
    private plugins              : Plugin[];
    private sseSocket            : any;
    private pluginsEventsRunning : any[]    = [];
    private caches               : string[] = [];

    private os                   : string ="";
    private cpu                  : number =0;
    private nbThreads            : number =0;
    private memory               : number =0;
    private socketsOpen          : number =0;
    private users                : any[];
    
    private instance             : string ="";
    private applicationName      : string ="";
    private applicationHostName  : string ="";
    private adminCustomStyle     : string ="";

    private sectionToDisplay     : string = "health"

    private noFormatNumber       : any    = org.inugami.formatters.noFormatNumber;

    private menuItems            : any [] = [
      {"label":"Health"  ,"name":"health"},
      {"label":"Caches"  ,"name":"caches"},
      {"label":"Alerts"  ,"name":"alerts"},
      {"label":"Plugins" ,"name":"plugins"},
      {"label":"Events"  ,"name":"events"},
      {"label":"Actions" ,"name":"actions"}
    ];
    
    /**************************************************************************
    * CONSTRUCTORS
    **************************************************************************/
    constructor(private route: ActivatedRoute,
                private pluginsService:PluginsService,
                private adminService:AdminService,
                private sessionScope : SessionScope,
                private mainMenuService  : MainMenuService){
        org.inugami.sse.register("administration");

        let self = this;
        org.inugami.events.addEventListener("konami", function(event){
          self.msgKonami="";
          self.konamiMode= true;
        });

        org.inugami.events.addEventListener(MAIN_MENU_ON_CLICK, function(data){
          self.updateSectionToDisplay(data);
        });
    }


    ngOnInit() {
      this.initMainMenu();
      let self = this;
      this.sub = this.route.params.subscribe(params => {
         this.sessionScope.openMainMenu();
         org.inugami.values.context.CONTEXT=window.location.href+"/admin";


         this.initData();

         org.inugami.events.addEventListener(org.inugami.sse.events.ERROR, function(event){
           self.onServerDown(event);
         );
         org.inugami.events.addEventListener(org.inugami.sse.events.OPEN, function(event){
           self.onServerUp(event);
         );
         org.inugami.events.addEventListener("administration_start_stop_events", function(event){
           self.onEventStartStop(event);
         );
         org.inugami.events.addEventListener("administration_system", function(event){
          self.onSystemInfo(event);
        );
         
      });

      window.setInterval(function() {
            self.updateUptime();
      }, 1000);
    }


    /**************************************************************************
    * INITIALIZE
    **************************************************************************/
    initMainMenu(){
      this.mainMenuService.cleanLinks();
      this.mainMenuService.setCurrentTitle("Administration");
      this.mainMenuService.addSubLink(new MainMenuLink("Health", "health","icon-enable health", false , 'admin',true));
      this.mainMenuService.addSubLink(new MainMenuLink("Caches", "caches","icon-enable caches", false, 'admin'));
      this.mainMenuService.addSubLink(new MainMenuLink("Alerts", "alerts","icon-enable alerts", false, 'admin'));
      this.mainMenuService.addSubLink(new MainMenuLink("Plugins", "plugins","icon-enable plugins", false, 'admin'));
      this.mainMenuService.addSubLink(new MainMenuLink("Events", "events","icon-enable Events", false, 'admin'));
      this.mainMenuService.addSubLink(new MainMenuLink("Actions", "actions","icon-enable actions", false, 'admin'));
      this.mainMenuService.updateMenu();
    }
    initData() {
      this.grabAllPlugins();
      this.grabUpTime();
      this.grabAllCaches();
    }

    grabUpTime(){
      this.adminService.getUpTime().then(data =>{
        this.lastTimeUp = new Date().getTime()-data;
        this.serverUp =true;
      });
    }

    grabAllPlugins(){
      this.pluginsService.getAllPlugins().then(data =>{
        this.plugins = data;
      });
    }

    grabAllCaches(){
      this.adminService.cache().then(data =>{
        this.caches = data;
      });
    }

    /**************************************************************************
    * ACTIONS
    **************************************************************************/
    forceRunEvents(){
      this.adminService.forceRun();
    }
    runEvents(){
      this.adminService.run();
    }
    forceRefresh(){
      this.adminService.forceRefresh();
    }
    beginUpdate(){
      this.adminService.beginUpdate();
    }
    endUpdate(){
      this.adminService.endUpdate();
    }

    clearAllCaches(){
      this.adminService.clearAllCaches();
    }

    clearCache(id){
      this.adminService.clearCache(id);
    }


    changeSection(name){
      this.sectionToDisplay=name;
    }

    isActiveSection(name){
      return this.sectionToDisplay==name?"active":"not-active";
    }
    

    /**************************************************************************
    * SERVER STATE
    **************************************************************************/
    onServerDown(event){
      this.serverUp = false;
      this.lastTimeUp =null;
      if(isNull(this.lastTimeDown)){
            this.lastTimeDown = new Date().getTime();
      }
    }

    onServerUp(event){
      this.serverUp = true;
      this.lastTimeDown=null;
      this.initData();
    }

    onEventStartStop(event){
      this.pluginsEventsRunning = event.detail.data;
    }

    /**************************************************************************
    * on System Info
    **************************************************************************/
    onSystemInfo(event){
      let data = event.detail.data;
      this.cpu       = data.cpu.use;
      this.nbThreads = data.threads.nbThreads;
      this.memory    = (data.memory.used/data.memory.max)*100;
      this.socketsOpen =data.nbSockets;
      this.os = [data.os,data.osArchitecture,data.osVersion].join(" ");
      this.users = data.users;
      
      this.instance             = data.instance;
      this.applicationName      = data.applicationName;
      this.applicationHostName  = data.applicationHostName;

      this.adminCustomStyle     = [this.instance,
                                   this.applicationName,
                                   this.applicationHostName
                                  ].join(" ");
    }

    simpleExtractor(data){
      return {"value":data};
    }
    percentFormatter(data){
      return isNull(data)?"0.00":org.inugami.formatters.truncateNumber(data,2);
    }

    percentAlertHandler(data){
        let result = "info";
        if(data>50){
          result= "error";
        }else if(data >=25){
          result= "warn";
        }
        return result;
    }

    threadAlertHandler(data){
      let result = "info";
      if(data>400){
        result= "error";
      }else if(data >=200){
        result= "warn";
      }
      return result;
    }

    socketsAlertHandler(data){
      let result = "info";
      if(data>50){
        result= "error";
      }else if(data>=20){
        result= "warn";
      }
      return result;
    }

    closeKonami(){
      this.konamiMode = false;
    }
    runOperation(index){
      let self = this;
      this.adminService.executeOperation(index).then(data =>{
         self.msgKonami = data._body;
      });
    }
    /**************************************************************************
    * UPDATE
    **************************************************************************/
    updateUptime(){
      let upOrDownTime = isNotNull(this.lastTimeUp) ? this.lastTimeUp : this.lastTimeDown;
      if(isNotNull(upOrDownTime)){
        let diff = new Date().getTime()-upOrDownTime;
        let buffer = [];

        let time = Math.round(diff/1000);

        let hours = Math.trunc(time / (3600));
        let minutes = Math.trunc((time - (3600 * hours)) / 60);
        let second = time - (3600 * hours) - (60 * minutes);


        buffer.push(hours);
        buffer.push(this.formatTime(minutes));
        buffer.push(this.formatTime(second));

        this.upTime = buffer.join(":");
      }
    }

    formatMillisTime(value){
      return org.inugami.formatters.timestampToDateTime(value/1000);
    }

    formatTime(value): string{
        return org.inugami.formatters.number(value, 2);
    }


    updateSectionToDisplay(event:any){
      this.sectionToDisplay = event.detail.path;
    }
    /**************************************************************************
    * GETTERS
    **************************************************************************/
    getUpTimeClass():string{
      return isNull(this.lastTimeDown)?"uptime":"downtime";
    }
}
