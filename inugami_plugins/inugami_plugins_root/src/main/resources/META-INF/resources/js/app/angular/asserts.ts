export const assertNotNull = (value:any, message:string)=>{
    io.inugami.asserts.notNull(value,message);
};

export const assertIsFalse = (condition:boolean, message:string)=>{
    io.inugami.asserts.isFalse(condition,message);
};

export const assertisTrue = (condition:boolean, message:string)=>{
    io.inugami.asserts.isTrue(condition,message);
};


export const assertisString = (value:any)=>{
    io.inugami.asserts.type.isString(value);
};

export const assertisArray= (value:any)=>{
    io.inugami.asserts.type.isArray(value);
};