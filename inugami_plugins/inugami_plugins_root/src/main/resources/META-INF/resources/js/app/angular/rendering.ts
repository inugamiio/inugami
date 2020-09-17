import {EventAlert} from './../models/events.interfaces';


export interface InugamiRendering{
    createNode(nodeName:string, styleClass:string, id?:string):HTMLElement,
    styleIfNotNull(node:any, styleClass:string),
    styleIfNull(node:any, styleClass:string),
    clearDiv():HTMLElement,
    alertsStyles(styleClass:string, alerts:EventAlert):string
}

export const RENDERING : InugamiRendering = {
    createNode      : (nodeName, styleClass, id)=>io.inugami.rendering.createNode(nodeName, styleClass, id)[0],
    styleIfNotNull  : (node, styleClass)=>io.inugami.rendering.styleIfNotNull(node, styleClass),
    styleIfNull     : (node, styleClass)=>io.inugami.rendering.styleIfNull(node, styleClass),
    clearDiv        : ()=>io.inugami.rendering.clearDiv()[0],
    alertsStyles    : (styleClass, alerts)=>io.inugami.rendering.alertsStyles(styleClass, alerts),
}