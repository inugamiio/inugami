import {Observable,Subscriber} from 'rxjs'
import {ObservableEvent} from './../models/tools/events.tools';

const localEvent :ObservableEvent<LogEvent> = new ObservableEvent<LogEvent>();
const localObserver : Observable<LogEvent> =localEvent.getObserver();

export interface InugamiLoggerLevel{
    level:number,
    title:string
}
export interface LogEvent{
    time:number,
    level:InugamiLoggerLevel,
    loggerName:string
    message:string
}
export interface InugamiLogger{
    trace(message:string, values?:any[]),
    debug(message:string, values?:any[]),
    info(message:string, values?:any[]),
    warn(message:string, values?:any[]),
    error(message:string, values?:any[]),
    fatal(message:string, values?:any[])
}


export interface InugamiLoggerService{
    factory(loggerName:string):InugamiLogger,
    appender:Observable<LogEvent>
}

export const LOGGERS : InugamiLoggerService = {
    factory  : (loggerName) => org.inugami.logger.factory(loggerName),
    appender : localObserver
}

org.inugami.logger._appenders.push({
    log : (localTime,localLevel,localLoggerName,localMessage)=>{
        localEvent.fireEvent({
            time:localTime,
            level:localLevel,
            loggerName:localLoggerName,
            message:localMessage
        });
    }
})