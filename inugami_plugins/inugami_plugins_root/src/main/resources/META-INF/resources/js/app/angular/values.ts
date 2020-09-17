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
        height : io.inugami.values.screen.height,
        width  : io.inugami.values.screen.width
    },
    mouse: io.inugami.values.mouse,
    size : {
        font : io.inugami.values.size.font
    },
    context : {
        URL         :io.inugami.values.context.URL,
        URL_FULL    :io.inugami.values.context.URL_FULL,
        PROTOCOLE   :io.inugami.values.context.PROTOCOLE,
        CONTEXT     :io.inugami.values.context.CONTEXT,
        PAGE        :io.inugami.values.context.PAGE,
        OPTION      :io.inugami.values.context.OPTION,
        OPTION_TYPE :io.inugami.values.context.OPTION_TYPE
    }
}