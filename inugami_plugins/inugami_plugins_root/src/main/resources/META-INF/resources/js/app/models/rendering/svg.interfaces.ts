export interface Point {
    x:number,
    y:number
}

export interface Size {
    bottom : number,
    height : number,
    left   : number,
    right  : number,
    top    : number,
    width  : number,
    x      : number,
    y      : number
}

export interface TransformInformation{
    x       : number,
    y       : number,
    scaleX  : number,
    scaleY  : number
}
export interface ComponentDimension{
    height : number,
    width  : number,
    font   : number
}


export interface SvgNodeCallBack{
    apply(node:SvgNode,...args: any[]):any
}
export interface SvgNode{
    _groups  : HTMLElement[],
    _parents :  HTMLElement[],
    append      (nodeName:string):SvgNode,
    attr        (attributeName:string,value?:any):string,
    call        (callback:SvgNodeCallBack):SvgNode,
    classed     (name:string, value?:any):SvgNode,
    clone       (value:any):SvgNode,
    data        (data:any, join?:SvgNode):SvgNode,
    datum       (value:any):any,
    dispatch    (value:any,data?:any):any,
    each        (callback:SvgNodeCallBack):SvgNode,
    empty       ():boolean,
    enter       ():SvgNode,
    exit        ():SvgNode,
    filter      (filter:(node:SvgNode,data:any,index:number)=>any):SvgNode,
    html        (value?:string):string,
    insert      (name:string, before?:any):SvgNode,
    interrupt   (value:any):any,
    join        (name:string,n:(value:any)=>any,e:(value:SvgNode)=>any):any,
    lower       ():SvgNode,
    merge       (node:SvgNode):any,
    node        ():HTMLElement,
    nodes       ():HTMLElement[],
    on          (type:string, listener:(value:any)=>any, capture:any),
    order       ():SvgNode,
    property    (name:string, value?:any):SvgNode,
    raise       ():SvgNode,
    remove      ():SvgNode,
    select      (querySelector:string):SvgNode,
    selectAll   (querySelector:string):SvgNode[],
    size        ():number,
    sort        (comparator:(ref:any,value:any)=>number),
    style       (name:string, value?:string, priority?:string):string,
    text        (value?:string):string,
    transition  (value:any):any,
}