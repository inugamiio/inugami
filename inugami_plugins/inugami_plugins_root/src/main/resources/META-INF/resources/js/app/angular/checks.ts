export const checkIsNull = (value:any)=>{
    return io.inugami.checks.isNull(value);
};

export const checkIsNotNull = (value:any)=>{
    return io.inugami.checks.isNotNull(value);
};

export const checkNotEmpty = (value:any)=>{
    return io.inugami.checks.notEmpty(value);
};

export const checkIndexOf = (value:any, list:any, functionEquals:Function)=>{
    return io.inugami.checks.indexOf(value,list,functionEquals);
};

export const checkContains = (value:any, list:any, functionEquals:Function)=>{
    return io.inugami.checks.contains(value,list,functionEquals);
};

export const checkContainsDefault = (value:any, list:any)=>{
    return io.inugami.checks.containsDefault(value,list);
};

export const checkIsArray = (value:any)=>{
    return io.inugami.checks.isArray(value);
};