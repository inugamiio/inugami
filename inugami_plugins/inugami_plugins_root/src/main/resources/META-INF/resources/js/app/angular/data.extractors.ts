import {
    GraphiteTarget,
    SimpleGraphiteTarget,
    TimeValue
} from './../models/main.types.interfaces';

import {
    EventData,
    EventAlert
} from './../models/events.interfaces';
import {GraphiteDatapoint} from './../models/graphite.datapoint';

// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// VALIDATORS
// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
export interface InugamiDataValidatorsGraphite {
    simpleValue(targets:any[]),
    isTimeValue(targets:any[])
}

export interface InugamiDataValidators {
    graphite : InugamiDataValidatorsGraphite
}

export const DATA_VALIDATORS : InugamiDataValidators = {
    graphite:{
        simpleValue : (targets)=> org.inugami.data.validators.graphite.simpleValue(targets),
        isTimeValue : (targets)=> org.inugami.data.validators.graphite.isTimeValue(targets)
    }
};


// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// EXTRACTORS
// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
export interface InugamiDataExtractorGraphite {
    simpleValue(target:GraphiteTarget|TimeValue):SimpleGraphiteTarget,
    simpleValueFiltred(target:GraphiteTarget|TimeValue, filter:Function):SimpleGraphiteTarget,
    sortTargets(data:GraphiteTarget|TimeValue):Map<string,GraphiteTarget|TimeValue>,
    cleanDatapoints(datapoints:GraphiteDatapoint[]),
}
export interface InugamiDataExtractorStyle {
    decomposeStyleClass(value:string,separator?:string)
}


export interface InugamiDataExtractors {
    graphite    : InugamiDataExtractorGraphite,
    style       : InugamiDataExtractorStyle,
    alertMax(data:EventData):EventAlert
}
export const DATA_EXTRACTORS : InugamiDataExtractors = {
    graphite    : {
        simpleValue         :(target)           =>org.inugami.data.extractors.graphite.simpleValue(target),
        simpleValueFiltred  :(target, filter)   =>org.inugami.data.extractors.graphite.simpleValueFiltred(target, filter),
        sortTargets         :(data)             =>org.inugami.data.extractors.graphite.sortTargets(data),
        cleanDatapoints     :(datapoints)       =>org.inugami.data.extractors.graphite.cleanDatapoints(datapoints)
    },
    style       : {
     decomposeStyleClass : (value,separator)=>org.inugami.data.extractors.style.decomposeStyleClass(value,separator)
    },
    alertMax    : (data:EventData)=>org.inugami.data.extractors.alertMax(data)
};
// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// AGGREGATORS
// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

export interface InugamiDataAggregators {
    sum(values:number[]):number,
    avg(values:number[]):number,
    min(values:number[]):number,
    max(values:number[]):number,
    p50(values:number[]):number,
    p70(values:number[]):number,
    p90(values:number[]):number,
    p95(values:number[]):number,
    p99(values:number[]):number,
    percentil(values:number[],percentilValue:number):number
}
export const DATA_AGGREGATORS : InugamiDataAggregators = {
    sum : (values)=> org.inugami.data.aggregators.sum(values),
    avg : (values)=> org.inugami.data.aggregators.avg(values),
    min : (values)=> org.inugami.data.aggregators.min(values),
    max : (values)=> org.inugami.data.aggregators.max(values),
    p50 : (values)=> org.inugami.data.aggregators.p50(values),
    p70 : (values)=> org.inugami.data.aggregators.p70(values),
    p90 : (values)=> org.inugami.data.aggregators.p90(values),
    p95 : (values)=> org.inugami.data.aggregators.p95(values),
    p99 : (values)=> org.inugami.data.aggregators.p99(values),
    percentil : (values,percent)=>org.inugami.data.aggregators.percentil(values,percent)
};

