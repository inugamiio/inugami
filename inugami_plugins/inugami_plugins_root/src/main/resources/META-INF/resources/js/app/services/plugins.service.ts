import {Injectable}                                 from '@angular/core';
import {HttpClient}                                 from '@angular/common/http';
import {Plugin}                                     from './../models/plugin';
import {HttpServices}                               from './http/http.services';
import {APP_ROOT_CONTEXT}                           from './../angular/constants';

@Injectable({
    providedIn: 'root',
})
export class PluginsService {

    /**************************************************************************
    * ATTRIBUTES
    **************************************************************************/
    private LOGGER      : any =  io.inugami.logger.factory("PluginsService");
    private urls        : any;
    private lastCalls   : any = {};

    /**************************************************************************
    * CONSTRUCTORS
    **************************************************************************/
    constructor(private httpSerivce    : HttpServices) {
        this.urls = {
            allPlugins                  : `${APP_ROOT_CONTEXT.context}rest/plugins`,
            allMenuLinks                : `${APP_ROOT_CONTEXT.context}rest/plugins/menu-links`,
            callPluginEventsProcessing  : `${APP_ROOT_CONTEXT.context}rest/plugins/events-data`,
            allPluginData               : `${APP_ROOT_CONTEXT.context}rest/plugins/all-plugins-data`
        };
        
        let self = this;
        io.inugami.events.addEventListenerByPlugin("globale","all-plugins-data", function(event){
            self.processCallAllPluginsData();
        });
    }

    /**************************************************************************
    * API
    **************************************************************************/
    getAllPlugins(): Promise<Plugin[]> {
        return this.httpSerivce.get(this.urls.allPlugins,null);
    }

    getAllMenuLinks(): Promise<any[]> {
      return this.httpSerivce.get(this.urls.allMenuLinks,null);
    }

    callPluginEventsProcessingGav(groupId, artifactId,excludeEvents){
        if(this.allowToCallServer(groupId, artifactId,excludeEvents)){
            this.LOGGER.info("call plugin events processing for : {0}:{1}",[groupId, artifactId]);
            io.inugami.asserts.notNull(groupId, "Plugin groupId is mandatory");
            io.inugami.asserts.notNull(artifactId, "Plugin artifactId is mandatory");
            let shortGav = [groupId,artifactId].join(':');
            let gav = PLUGINS_GAVS.plugins[pluginName];
            io.inugami.asserts.notNull(gav, "Can't found plugin GAV for name :"+pluginName);
            this.processCallPluginEvents(gav,excludeEvents);
        }
    }


    callPluginEventsProcessingBaseName(pluginName, excludeEvents){
      if(this.allowToCallServer(pluginName, pluginName,excludeEvents)){
        this.LOGGER.info("call plugin events processing for : {0}",[pluginName]);
        io.inugami.asserts.notNull(pluginName, "Plugin angular module name is mandatory");
        let gav = PLUGINS_GAVS.frontPlugins[pluginName];
        io.inugami.asserts.notNull(gav, "Can't found plugin GAV for name :"+pluginName);
        this.processCallPluginEvents(gav,excludeEvents);
      }
    }


    private allowToCallServer(groupId, artifactId,excludeEvents){
        let key = [groupId,artifactId,excludeEvents].join('_');
        let lastCall = this.lastCalls[key];
        let now = new Date().getTime;

        let result = lastCall == undefined || lastCall==null || (lastCall-now) < 500;
        this.lastCalls[key] = now;

        return result;
    }

    private processCallPluginEvents(gav,excludeEvents){
        let gavParm = [gav.groupId,gav.artifactId, gav.version];
        if(isNotNull(gav.qualifier)){
          gavParm.push(gav.qualifier);
        }

      
        let exclude  =  this.buildExclude(excludeEvents);
        let urlParts = [this.urls.callPluginEventsProcessing, "?gav=",gavParm.join(':'),exclude ];
        this.httpSerivce.get(urlParts.join(''),null)
                        .then(data=>{
                            this.fireAllEvents(data);
                        })
                        .catch(error=>{
                            console.log(error);
                        })
    }

    private processCallAllPluginsData(){
        this.httpSerivce.get(this.urls.allPluginData,null)
            .then(data=>{
                this.fireAllEvents(data);
            })
            .catch(error=>{
                console.log(error);
            });
    }

    private buildExclude(exclude):string{
        let result = "";
        if(isNotNull(exclude)){
            result = "&exclude="+exclude.replace(new RegExp("[|]", 'g'), "%7C");
        }
        return result;
    }
  


    /**************************************************************************
    * FIRE DATA 
    **************************************************************************/
    fireAllEvents(data){
        for(let eventData of data){
            try{
                let json = null;
                if(isNotNull(eventData.data)){
                    json =JSON.parse(eventData.data) ;
                }

                var name = isNull(eventData.event)?"":eventData.event.name;
                var localData = {
                    "channel" : eventData.channel,
                    "name"    : name,
                    "time"    : Date.now(),
                    "data"    : {
                      "channel" : eventData.channel,
                      "event"   : name,
                      "alerts"  : eventData.alerts,
                      "values"  : json
                    }
                  };


                let eventName = io.inugami.events.buildEventFullName(localData.channel,localData.name);
                io.inugami.events.fireEvent(eventName,localData);
            }catch(error){
                console.log(error);
            }
        }
    }


    /**************************************************************************
    * HANDLING ERRORS
    **************************************************************************/
    private handleError(error: any): Promise<any> {
        return Promise.reject(error.message || error);
    }

}
