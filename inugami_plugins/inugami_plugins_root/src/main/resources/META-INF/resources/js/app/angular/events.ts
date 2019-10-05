import {Observable,Subscriber} from 'rxjs'
import {EventResult,EventAlert} from './../models/events.interfaces';

//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// EVENTS
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
class HandlerEvent<T>{
    handler : Subscriber = null;
    observer : Observable<T> =null;
    eventName:string;

    constructor(eventName){
        this.eventName=eventName;
        this.observer= new Observable((handlerEvent)=>{
            this.handler=handlerEvent;
        });

        let currentEvent = eventName;
        document.addEventListener(eventName, (data)=>{
            if(this.handler!=null){
                this.handler.next(data);
            }
        });
    }
}

const localObservers = {
    onResize                            :new HandlerEvent<any>(org.inugami.events.type.RESIZE),
    onUpdateConfiguration               :new HandlerEvent<any>(org.inugami.events.type.UPDATE_CONFIGURATION),
    onChangeScreenBefore                :new HandlerEvent<any>(org.inugami.events.type.CHANGE_SCREEN_BEFORE),
    onChangeScreen                      :new HandlerEvent<any>(org.inugami.events.type.CHANGE_SCREEN),
    onAppComponentsEvent                :new HandlerEvent<any>(org.inugami.events.type.APP_COMPONENTS_EVENT),
    onAppComponentsEventCallBackBefore  :new HandlerEvent<any>(org.inugami.events.type.APP_COMPONENTS_EVENT_CALLBACK_BEFORE),
    onAppComponentsEventCallBack        :new HandlerEvent<any>(org.inugami.events.type.APP_COMPONENTS_EVENT_CALLBACK),
    onEverySecond                       :new HandlerEvent<any>(org.inugami.events.type.EVERY_SECOND),
    onEveryMinute                       :new HandlerEvent<any>(org.inugami.events.type.EVERY_MINUTE),
    onEveryPlainMinute                  :new HandlerEvent<any>(org.inugami.events.type.EVERY_PLAIN_MINUTE),
    onEveryPlainHour                    :new HandlerEvent<any>(org.inugami.events.type.EVERY_PLAIN_HOUR),
    onEveryPlainDay                     :new HandlerEvent<any>(org.inugami.events.type.EVERY_PLAIN_DAY),

    onSseOpen                           :new HandlerEvent<any>(org.inugami.sse.events.OPEN),
    onSseError                          :new HandlerEvent<any>(org.inugami.sse.events.ERROR),
    onSseUpdate                         :new HandlerEvent<any>(org.inugami.sse.events.UPDATE),
    onSseAlreadyOpen                    :new HandlerEvent<any>(org.inugami.sse.events.ALREADY_OPEN),
    onSseOpenOrAlreadyOpen              :new HandlerEvent<any>(org.inugami.sse.events.OPEN_OR_ALREADY_OPEN),
    onSseForceRefresh                   :new HandlerEvent<any>(org.inugami.sse.events.FORCE_REFRESH),
    onAlerts                            :new HandlerEvent<EventResult>(org.inugami.sse.events.ALERTS),

    onKonami                            :new HandlerEvent<any>("konami"),
    onHadoken                           :new HandlerEvent<any>("hadoken"),

    pluginsEvents                       :{},
    genericsEvents                      :{},
}

//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// API
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
export interface InugamiDefaultEvents{
    onResize                              :Observable<any>,
    onUpdateConfiguration                 :Observable<any>,
    onChangeScreenBefore                  :Observable<any>,
    onChangeScreen                        :Observable<any>,
    onAppComponentsEvent                  :Observable<any>,
    onAppComponentsEventCallBackBefore    :Observable<any>,
    onAppComponentsEventCallBack          :Observable<any>,
    onEverySecond                         :Observable<any>,
    onEveryMinute                         :Observable<any>,
    onEveryPlainMinute                    :Observable<any>,
    onEveryPlainHour                      :Observable<any>,
    onEveryPlainDay                       :Observable<any>,

    onSseError                            :Observable<any>,
    onSseOpen                             :Observable<any>,
    onSseUpdate                           :Observable<any>,
    onSseAlreadyOpen                      :Observable<any>,
    onSseOpenOrAlreadyOpen                :Observable<any>,
    onSseForceRefresh                     :Observable<any>,
    onAlerts                              :Observable<EventResult>
}

export interface InugamiEventsService{
    defaultEvents : InugamiDefaultEvents,
    buildEventFullName(pluginName:string, eventName:string),
    fireEvent(eventName:string, data:any),
    fireEventPlugin(pluginName:string, eventName:string, data:any,timestamp:number, alerts:EventAlert[]),

    eventListenerByPlugin(pluginName:string,eventName:string):Observable<EventResult>,
    eventListener(eventName:string):Observable<any>,
    updateResize(),

    keys : InugamiEventsKeysService
}

export interface InugamiEventsKeysService{
    addKeyListener(identifier:string, keysInput:number[]):Observable<any>,
    onKonami:Observable<any>,
    onHadoken:Observable<any>,

    enableKeysListerner(),
    disableKeysListerner(),
    defineMaxKeys(max:number)
}
export const EVENTS : InugamiEventsService = {
    defaultEvents : {
        onResize                            : localObservers.onResize.observer,
        onUpdateConfiguration               : localObservers.onUpdateConfiguration.observer,
        onChangeScreenBefore                : localObservers.onChangeScreenBefore.observer,
        onChangeScreen                      : localObservers.onChangeScreen.observer,
        onAppComponentsEvent                : localObservers.onAppComponentsEvent.observer,
        onAppComponentsEventCallBackBefore  : localObservers.onAppComponentsEventCallBackBefore.observer,
        onAppComponentsEventCallBack        : localObservers.onAppComponentsEventCallBack.observer,
        onEverySecond                       : localObservers.onEverySecond.observer,
        onEveryMinute                       : localObservers.onEveryMinute.observer,
        onEveryPlainMinute                  : localObservers.onEveryPlainMinute.observer,
        onEveryPlainHour                    : localObservers.onEveryPlainHour.observer,
        onEveryPlainDay                     : localObservers.onEveryPlainDay.observer,

        onSseError                          : localObservers.onSseError.observer,
        onSseOpen                           : localObservers.onSseOpen.observer,
        onSseUpdate                         : localObservers.onSseUpdate.observer,
        onSseAlreadyOpen                    : localObservers.onSseAlreadyOpen.observer,
        onSseOpenOrAlreadyOpen              : localObservers.onSseOpenOrAlreadyOpen.observer,
        onSseForceRefresh                   : localObservers.onSseForceRefresh.observer,
        onAlerts                            : localObservers.onAlerts.observer
    },

    buildEventFullName    : (pluginName, eventName) => org.inugami.events.buildEventFullName(pluginName,eventName),
    fireEvent             : (eventName,data)        =>org.inugami.events.fireEvent(eventName,data),
    fireEventPlugin       : (pluginName, eventName, data,timestamp, alerts)=>org.inugami.events.fireEventPlugin(pluginName, eventName, data,timestamp, alerts),
    updateResize          : ()=> org.inugami.events.updateResize(),
    eventListenerByPlugin : (pluginName,eventName) => {
        let fullEventName = EVENTS.buildEventFullName(pluginName,eventName);

        let result = localObservers.pluginsEvents[fullEventName];
        if(result == undefined || result==null){
            result = new HandlerEvent<EventResult>(fullEventName);
            localObservers.pluginsEvents[fullEventName]=result;
        }
        return result.observer;
    },

    eventListener : (eventName)=>{
        let result = localObservers.genericsEvents[eventName];
        if(result == undefined || result==null){
            result = new HandlerEvent<any>(eventName);
            localObservers.genericsEvents[eventName]=result;
        }
        return result.observer;
    },

    keys : {
        onKonami  : localObservers.onKonami.observer,
        onHadoken : localObservers.onHadoken.observer,

        addKeyListener : (identifier, keysInput) => {
            org.inugami.events._inner.enableCheat=true;
            org.inugami.events._inner.keysListener[identifier]=keysInput;
            let result = localObservers.genericsEvents[identifier];
            if(result == undefined || result == null){
                result = new HandlerEvent<any>(identifier);
                localObservers.genericsEvents[identifier] = result;
            }
            return result.observer;
        },

        enableKeysListerner  : ()=> org.inugami.events._inner.enableCheat=true,
        disableKeysListerner : ()=> org.inugami.events._inner.enableCheat=false,
        defineMaxKeys        : (max)=> org.inugami.events._inner.maxKey=max
    }
}
