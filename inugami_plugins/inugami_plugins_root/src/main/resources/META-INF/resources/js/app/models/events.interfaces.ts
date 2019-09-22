export interface EventResult {
    channel:string,
    cron:string,
    data:EventData,
    name:string,
    time:number
}
export interface EventData {
  alerts:EventAlert[],
  channel:string,
  event:string,
  scheduler:string,
  values:any
}
export interface EventAlert {
    alerteName:string,
    level:string,
    url:string,
    levelType:EventAlertLevelType,
    levelNumber:number,
    message:string,
    subLabel:string,
    created:number,
    duration:number,
    data:any,
    channel:string,
    multiAlerts:boolean,
    providers:string[]
}

export enum EventAlertLevelType{
    ERROR   ,
    WARN    ,
    FATAL   ,
    INFO    ,
    DEBUG   ,
    TRACE   ,
    OFF     ,
    UNDEFINE
}
