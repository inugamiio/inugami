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
    extractJiraName             :(jiraUrl)=> org.inugami.formatters.extractJiraName(jiraUrl),
    hour                        :(value, delimiter)=>org.inugami.formatters.hour(value, delimiter),
    noFormatNumber              :(value)=>org.inugami.formatters.noFormatNumber(value),
    truncateNumberOfMaxValue    :(value, nbFloatDigit, maxValue, resultMax)=>org.inugami.formatters.truncateNumberOfMaxValue(value, nbFloatDigit, maxValue, resultMax),
    truncateNumber              :(value, nbFloatDigit)=>org.inugami.formatters.truncateNumber(value, nbFloatDigit),
    truncate                    :(value, maxLength,suffix)=>org.inugami.formatters.truncate(value, maxLength,suffix),
    defaultTrendFormat          :(value, oldValue, maxInt)=>org.inugami.formatters.defaultTrendFormat(value, oldValue, maxInt),
    number                      :(value,nbDigit, digitToAppend)=>org.inugami.formatters.number(value,nbDigit, digitToAppend),
    date                        :{
        timestampToDate             : (timestamp)=>org.inugami.formatters.timestampToDate(timestamp),
        timestampToDateTime         : (timestamp)=>org.inugami.formatters.timestampToDateTime(timestamp),
        timestampToHour             : (timestamp)=>org.inugami.formatters.timestampToHour(timestamp),
        timestampToTimeFormat       : (timestamp,format)=>org.inugami.formatters.timestampToTimeFormat(timestamp,format),
        simpleTimeMin               : (value)=>org.inugami.formatters.time.simpleTimeMin(value)
    },
    message                     :{
        messageValue                : (key, values)=>org.inugami.formatters.messageValue(key, values),
        message                     : (key, values)=>org.inugami.formatters.message(key, values),
        messages                    : (baseKey, values)=>org.inugami.formatters.messages(baseKey, values),
    }
}