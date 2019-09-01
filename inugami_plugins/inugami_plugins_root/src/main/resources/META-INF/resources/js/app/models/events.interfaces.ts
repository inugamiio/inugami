export interface EventData {
    channel:string,
    name:string,
    time?:number,
    data?:EventDataContent
}
export interface EventDataContent {
    channel?:string,
    subEvent?:string,
    alerts?:EventAlert,
    values?:any
}
export interface EventAlert {
}
