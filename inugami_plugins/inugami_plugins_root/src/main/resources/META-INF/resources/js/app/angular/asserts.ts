export const assertNotNull = (value:any, message:string)=>{
    org.inugami.asserts.notNull(value,message);
};

export const assertIsFalse = (condition:boolean, message:string)=>{
    org.inugami.asserts.isFalse(condition,message);
};

export const assertisTrue = (condition:boolean, message:string)=>{
    org.inugami.asserts.isTrue(condition,message);
};


export const assertisString = (value:any)=>{
    org.inugami.asserts.type.isString(value);
};

export const assertisArray= (value:any)=>{
    org.inugami.asserts.type.isArray(value);
};