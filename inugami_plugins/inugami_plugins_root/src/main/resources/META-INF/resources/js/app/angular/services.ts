export interface InugamiService {
    generateId          (componentName:string):string,
    getIdOrGeneratedId  (id:string, componentName:string):string,
    normalizeId         (value:string):string,
    defaultValueEmpty   (value:string):string
    defaultValue        (value:string, defaultValue:string):string,
    getParent           (nodeName:string, currentNode):string,
    getFunction         (name:string):(...args:any[])=>any,
    removeFromList<T>   (value:T, list:T[], functionEquals:(ref:T,value:T)=>boolean):T[]
}

export const SERVICES : InugamiService = {
    generateId          :(componentName)=>io.inugami.services.generateId(componentName),
    getIdOrGeneratedId  :(id, componentName)=>io.inugami.services.getIdOrGeneratedId(id, componentName),
    normalizeId         :(value)=>io.inugami.services.normalizeId(value),
    defaultValueEmpty   :(value)=>io.inugami.services.defaultValueEmpty(value),
    defaultValue        :(value, defaultValue)=>io.inugami.services.defaultValue(value, defaultValue),
    getParent           :(nodeName, currentNode)=>io.inugami.services.getParent(nodeName, currentNode),
    getFunction         :(name)=>io.inugami.services.getFunction(name),
    removeFromList      :(value, list, functionEquals)=>io.inugami.services.removeFromList(value, list, functionEquals),
}