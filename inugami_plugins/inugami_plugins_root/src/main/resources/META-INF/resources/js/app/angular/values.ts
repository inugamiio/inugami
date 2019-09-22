import {Point} from './../models/rendering/svg.interfaces';


export interface InugamiValuesScreen {
    height : number,
    width  : number
}
export interface InugamiValuesSize {
    font : number
}
export interface InugamiValuesContext {
    URL         :string,
    URL_FULL    :string,
    PROTOCOLE   :string,
    CONTEXT     :string,
    PAGE        :string,
    OPTION      :string,
    OPTION_TYPE :string
}

export interface InugamiValues {
    screen: InugamiValuesScreen,
    mouse: Point,
    size : InugamiValuesSize,
    context : InugamiValuesContext
}

export const VALUES : InugamiValues = {
    screen: {
        height : org.inugami.values.screen.height,
        width  : org.inugami.values.screen.width
    },
    mouse: org.inugami.values.mouse,
    size : {
        font : org.inugami.values.size.font
    },
    context : {
        URL         :org.inugami.values.context.URL,
        URL_FULL    :org.inugami.values.context.URL_FULL,
        PROTOCOLE   :org.inugami.values.context.PROTOCOLE,
        CONTEXT     :org.inugami.values.context.CONTEXT,
        PAGE        :org.inugami.values.context.PAGE,
        OPTION      :org.inugami.values.context.OPTION,
        OPTION_TYPE :org.inugami.values.context.OPTION_TYPE
    }
}