import {HttpServices}  from './../services/http/http.services';
import {EventResult}   from './../models/events.interfaces'


export interface InugamiSseTimeReboot {
    hour:number,
    min :number
}

export interface InugamiSseTime {
    MIN                   :number,
    TWO_MIN               :number,
    TEN_MIN               :number,
    HOUR                  :number,
    maxUnactivity         :number,
    reboot                :InugamiSseTimeReboot,
}

export interface InugamiSse {
	httpConnector         : HttpServices,
	enableLogger 	      : boolean,
	state 			      : string,
    nbRetry               : number,
    times                 : InugamiSseTime,
    lastIncommingData     :number,
    forceRefresh          (),
    register              (pluginName:string, filterHandler?:(eventName:string)=>boolean, alertsHandler?:(event:EventResult)=>any),
    connect               (channelName:string),
    connectSSESocket      (force:boolean),
    closeSocket           (),
    reboot                (),
    fromLastIncommingData (),
    fromCloseSocket       ()
}

export const SSE : InugamiSse = {
	httpConnector         : org.inugami.sse.httpConnector,
	enableLogger 	      : org.inugami.sse.enableLogger,
	state 			      : org.inugami.sse.state,
    nbRetry               : org.inugami.sse.nbRetry,
    times                 : {
        MIN                   :org.inugami.sse.times.MIN,
        TWO_MIN               :org.inugami.sse.times.TWO_MIN,
        TEN_MIN               :org.inugami.sse.times.TEN_MIN,
        HOUR                  :org.inugami.sse.times.HOUR,
        maxUnactivity         :org.inugami.sse.times.maxUnactivity,
        reboot                :{
            hour:org.inugami.sse.times.reboot.hour,
            min :org.inugami.sse.times.reboot.min
        }
    },
    lastIncommingData     :org.inugami.sse.lastIncommingData,
    forceRefresh          :()=>org.inugami.sse.forceRefresh(),
    register              :(pluginName, filterHandler, alertsHandler)=>org.inugami.sse.register(pluginName, filterHandler, alertsHandler),
    connect               :(channelName)=>org.inugami.sse.connect(channelName),
    connectSSESocket      :(force)=>org.inugami.sse.connectSSESocket(force),
    closeSocket           :()=>org.inugami.sse.closeSocket(),
    reboot                :()=>org.inugami.sse.reconnect.reboot(),
    fromLastIncommingData :()=>org.inugami.sse.reconnect.fromLastIncommingData(),
    fromCloseSocket       :()=>org.inugami.sse.reconnect.fromCloseSocket()
}