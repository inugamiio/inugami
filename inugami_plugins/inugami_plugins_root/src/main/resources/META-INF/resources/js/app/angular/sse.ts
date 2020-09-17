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
	httpConnector         : io.inugami.sse.httpConnector,
	enableLogger 	      : io.inugami.sse.enableLogger,
	state 			      : io.inugami.sse.state,
    nbRetry               : io.inugami.sse.nbRetry,
    times                 : {
        MIN                   :io.inugami.sse.times.MIN,
        TWO_MIN               :io.inugami.sse.times.TWO_MIN,
        TEN_MIN               :io.inugami.sse.times.TEN_MIN,
        HOUR                  :io.inugami.sse.times.HOUR,
        maxUnactivity         :io.inugami.sse.times.maxUnactivity,
        reboot                :{
            hour:io.inugami.sse.times.reboot.hour,
            min :io.inugami.sse.times.reboot.min
        }
    },
    lastIncommingData     :io.inugami.sse.lastIncommingData,
    forceRefresh          :()=>io.inugami.sse.forceRefresh(),
    register              :(pluginName, filterHandler, alertsHandler)=>io.inugami.sse.register(pluginName, filterHandler, alertsHandler),
    connect               :(channelName)=>io.inugami.sse.connect(channelName),
    connectSSESocket      :(force)=>io.inugami.sse.connectSSESocket(force),
    closeSocket           :()=>io.inugami.sse.closeSocket(),
    reboot                :()=>io.inugami.sse.reconnect.reboot(),
    fromLastIncommingData :()=>io.inugami.sse.reconnect.fromLastIncommingData(),
    fromCloseSocket       :()=>io.inugami.sse.reconnect.fromCloseSocket()
}