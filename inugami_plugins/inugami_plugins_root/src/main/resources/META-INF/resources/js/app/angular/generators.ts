export interface InugamiGenerators {
    lorem            (sizeMin:number, sizeMax:number),
    randomBoolean    (),
    randomNumber     (min:number, max:number),
    randomValues<T>  (values:T[]):T
}

export const GENERATORS : InugamiGenerators = {
    lorem            :(sizeMin, sizeMax)=>org.inugami.services.generators.lorem(sizeMin, sizeMax),
    randomBoolean    :()=>org.inugami.services.generators.randomBoolean(),
    randomNumber     :(min, max)=>org.inugami.services.generators.randomNumber(min, max),
    randomValues     :(values)=>org.inugami.services.generators.randomValues(values)
}