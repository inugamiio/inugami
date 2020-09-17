export interface InugamiGenerators {
    lorem            (sizeMin:number, sizeMax:number),
    randomBoolean    (),
    randomNumber     (min:number, max:number),
    randomValues<T>  (values:T[]):T
}

export const GENERATORS : InugamiGenerators = {
    lorem            :(sizeMin, sizeMax)=>io.inugami.services.generators.lorem(sizeMin, sizeMax),
    randomBoolean    :()=>io.inugami.services.generators.randomBoolean(),
    randomNumber     :(min, max)=>io.inugami.services.generators.randomNumber(min, max),
    randomValues     :(values)=>io.inugami.services.generators.randomValues(values)
}