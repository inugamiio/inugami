import {TrendValue} from './../models/main.types.interfaces';

export interface InugamiFormatterDateService{
    timestampToDate             (timestamp:number):string,
    timestampToDateTime         (timestamp:number):string,
    timestampToHour             (timestamp:number):string,
    timestampToTimeFormat       (timestamp:number,format:string):string,
    simpleTimeMin               (value:number):string
}

export interface InugamiFormatterTextService{
    messageValue(key:string, values?:any[]):string,
    message (key:string, values?:any[]):string,
    messages(baseKey:string, values?:any[]):string,
}


export interface InugamiFormatterService{
    extractJiraName             (jiraUrl:string):string,
    hour                        (value:number, delimiter:string):string,
    noFormatNumber              (value:number):number,
    truncateNumberOfMaxValue    (value:number, nbFloatDigit:number, maxValue:number, resultMax:number),
    truncateNumber              (value:number, nbFloatDigit):string
    truncate                    (value:string, maxLength:number,suffix:string):string,
    defaultTrendFormat          (value:number, oldValue:number, maxInt:number):TrendValue,
    number                      (value:number,nbDigit:number, digitToAppend?:string):string

    date : InugamiFormatterDateService,
    message : InugamiFormatterTextService
}

export const FORMATTERS : InugamiFormatterService = {
    extractJiraName             :(jiraUrl)=> io.inugami.formatters.extractJiraName(jiraUrl),
    hour                        :(value, delimiter)=>io.inugami.formatters.hour(value, delimiter),
    noFormatNumber              :(value)=>io.inugami.formatters.noFormatNumber(value),
    truncateNumberOfMaxValue    :(value, nbFloatDigit, maxValue, resultMax)=>io.inugami.formatters.truncateNumberOfMaxValue(value, nbFloatDigit, maxValue, resultMax),
    truncateNumber              :(value, nbFloatDigit)=>io.inugami.formatters.truncateNumber(value, nbFloatDigit),
    truncate                    :(value, maxLength,suffix)=>io.inugami.formatters.truncate(value, maxLength,suffix),
    defaultTrendFormat          :(value, oldValue, maxInt)=>io.inugami.formatters.defaultTrendFormat(value, oldValue, maxInt),
    number                      :(value,nbDigit, digitToAppend)=>io.inugami.formatters.number(value,nbDigit, digitToAppend),
    date                        :{
        timestampToDate             : (timestamp)=>io.inugami.formatters.timestampToDate(timestamp),
        timestampToDateTime         : (timestamp)=>io.inugami.formatters.timestampToDateTime(timestamp),
        timestampToHour             : (timestamp)=>io.inugami.formatters.timestampToHour(timestamp),
        timestampToTimeFormat       : (timestamp,format)=>io.inugami.formatters.timestampToTimeFormat(timestamp,format),
        simpleTimeMin               : (value)=>io.inugami.formatters.time.simpleTimeMin(value)
    },
    message                     :{
        messageValue                : (key, values)=>io.inugami.formatters.messageValue(key, values),
        message                     : (key, values)=>io.inugami.formatters.message(key, values),
        messages                    : (baseKey, values)=>io.inugami.formatters.messages(baseKey, values),
    }
}