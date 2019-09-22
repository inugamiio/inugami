export const checkIsNull = (value:any)=>{
    return org.inugami.checks.isNull(value);
};

export const checkIsNotNull = (value:any)=>{
    return org.inugami.checks.isNotNull(value);
};

export const checkNotEmpty = (value:any)=>{
    return org.inugami.checks.notEmpty(value);
};

export const checkIndexOf = (value:any, list:any, functionEquals:Function)=>{
    return org.inugami.checks.indexOf(value,list,functionEquals);
};

export const checkContains = (value:any, list:any, functionEquals:Function)=>{
    return org.inugami.checks.contains(value,list,functionEquals);
};

export const checkContainsDefault = (value:any, list:any)=>{
    return org.inugami.checks.containsDefault(value,list);
};

export const checkIsArray = (value:any)=>{
    return org.inugami.checks.isArray(value);
};