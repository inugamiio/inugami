import {GraphiteDatapoint} from './graphite.datapoint';

export interface GraphiteTarget {
    target:string,
    datapoints:GraphiteDatapoint[]
}
export interface SimpleGraphiteTarget{
    target:string,
    value:number,
    timestamp:number
}
export interface TimeValue{
    path:string,
    value:number,
    timestamp:number
}


export interface TrendValue{
    trend:string,
    trendClass:string
}

