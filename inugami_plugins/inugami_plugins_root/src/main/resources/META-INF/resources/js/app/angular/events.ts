import {Observable,Subscriber} from 'rxjs'

//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// EVENTS
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
class HandlerEvent{
    handler : Subscriber = null;
    observer : Observable<any> =null;
    eventName:string;

    constructor(eventName){
        this.eventName=eventName;
        this.observer= new Observable((handlerEvent)=>{
            this.handler=handlerEvent;
        });

        document.addEventListener(eventName, ()=>{
            if(this.handler!=null){
                this.handler.next();
            }
        });
    }
}

const localObservers = {
    onResize                            :new HandlerEvent(org.inugami.events.type.RESIZE),
    onUpdateConfiguration               :new HandlerEvent(org.inugami.events.type.UPDATE_CONFIGURATION),
    onChangeScreenBefore                :new HandlerEvent(org.inugami.events.type.CHANGE_SCREEN_BEFORE),
    onChangeScreen                      :new HandlerEvent(org.inugami.events.type.CHANGE_SCREEN),
    onAppComponentsEvent                :new HandlerEvent(org.inugami.events.type.APP_COMPONENTS_EVENT),
    onAppComponentsEventCallBackBefore  :new HandlerEvent(org.inugami.events.type.APP_COMPONENTS_EVENT_CALLBACK_BEFORE),
    onAppComponentsEventCallBack        :new HandlerEvent(org.inugami.events.type.APP_COMPONENTS_EVENT_CALLBACK),
    onEverySecond                       :new HandlerEvent(org.inugami.events.type.EVERY_SECOND),
    onEveryMinute                       :new HandlerEvent(org.inugami.events.type.EVERY_MINUTE),
    onEveryPlainMinute                  :new HandlerEvent(org.inugami.events.type.EVERY_PLAIN_MINUTE),
    onEveryPlainHour                    :new HandlerEvent(org.inugami.events.type.EVERY_PLAIN_HOUR),
    onEveryPlainDay                     :new HandlerEvent(org.inugami.events.type.EVERY_PLAIN_DAY)
}

console.log("localObservers :",localObservers);

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
    onEveryPlainDay                       :Observable<any>
}

export interface InugamiEventsService{
    defaultEvents : InugamiDefaultEvents
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
        onEveryPlainDay                     : localObservers.onEveryPlainDay.observer
    }
}
