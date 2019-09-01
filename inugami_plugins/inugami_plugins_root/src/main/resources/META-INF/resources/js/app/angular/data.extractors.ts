import {GraphiteTarget,
        SimpleGraphiteTarget
        } from './../models/main.types.interfaces';

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
    simpleValue(target):SimpleGraphiteTarget,
    simpleValueFiltred(target, filter):SimpleGraphiteTarget,
    sortTargets(data),
    cleanDatapoints(datapoints),
}
export interface InugamiDataExtractorStyle {
    decomposeStyleClass(value:string,separator?:string)
}


export interface InugamiDataExtractors {
    graphite    : InugamiDataExtractorGraphite,
    style       : InugamiDataExtractorStyle,
    alertMax(data)
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
    alertMax    : (data)=>org.inugami.data.extractors.alertMax(data)
};
// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// AGGREGATORS
// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
/*
export interface InugamiDataAggregators {
    
}
export const DATA_AGGREGATORS : InugamiDataValidators = {
};
*/