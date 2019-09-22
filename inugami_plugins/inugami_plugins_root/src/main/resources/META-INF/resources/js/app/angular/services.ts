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
    generateId          :(componentName)=>org.inugami.services.generateId(componentName),
    getIdOrGeneratedId  :(id, componentName)=>org.inugami.services.getIdOrGeneratedId(id, componentName),
    normalizeId         :(value)=>org.inugami.services.normalizeId(value),
    defaultValueEmpty   :(value)=>org.inugami.services.defaultValueEmpty(value),
    defaultValue        :(value, defaultValue)=>org.inugami.services.defaultValue(value, defaultValue),
    getParent           :(nodeName, currentNode)=>org.inugami.services.getParent(nodeName, currentNode),
    getFunction         :(name)=>org.inugami.services.getFunction(name),
    removeFromList      :(value, list, functionEquals)=>org.inugami.services.removeFromList(value, list, functionEquals),
}