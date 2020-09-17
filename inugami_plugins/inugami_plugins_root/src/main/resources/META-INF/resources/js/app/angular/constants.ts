export interface RootApplicationContext {
    appPath             :string,
    context             :string,
    resourcesPath       :string,
    vendorPath          :string,
    messages            :any,
    pluginsComponents   :any,

}

export const APP_ROOT_CONTEXT : RootApplicationContext = document["root_application_context"];



export interface InugamiConstantsMimeType{
    JSON :  string
}
export interface InugamiConstantsHeadersDefaultJson{
    contentType:string
}

export interface InugamiConstantsHeaders{
    defaultJson                 : InugamiConstantsHeadersDefaultJson,
    deviceIdentifier            : string,
    correlationId               : string,
    deviceType                  : string,
    deviceClass                 : string,
    deviceOsVersion             : string,
    deviceVersion               : string,
    deviceNetworkType           : string,
    deviceNetworkSpeedDown      : string,
    deviceNetworkSpeedLatency   : string
}

export interface InugamiConstantsCode{
    types : string[]
}
export interface InugamiConstantsLorem{
    data : string[],
    size : number
}

export interface InugamiConstantsContext{
    url         :string,
    protocole   :string,
    context     :string,
    page        :string,
    option      :any,
    optionType  :any,
    locale      :string
}
export interface InugamiConstantsMath{
    twoPi       :number,
    twoPiRatio  :number
}
export interface InugamiConstants{
    mimetype                    :InugamiConstantsMimeType,
    headers                     :InugamiConstantsHeaders
    code                        :InugamiConstantsCode,
    lorem                       : InugamiConstantsLorem,
    context                     :InugamiConstantsContext,
    math                        :InugamiConstantsMath,
    token                       :string,
    uuid                        :string,
    deviceIdentifier            :string,
    deviceClass                 :string,
    deviceType                  :string,
    deviceOsVersion             :string,
    deviceVersion               :string,
    deviceNetworkType           :string,
    deviceNetworkSpeedDown      :number,
    deviceNetworkSpeedLatency   :number,

}

export const CONSTANTS : InugamiConstants  = {
    mimetype : {
        JSON:io.inugami.constants.mimetype.JSON
    },
    headers : {
        defaultJson:{
           contentType: io.inugami.constants.headers.DEFAULT_JSON
        },
        deviceIdentifier            : io.inugami.constants.headers.DEVICE_IDENTIFIER,
        correlationId               : io.inugami.constants.headers.CORRELATION_ID,
        deviceType                  : io.inugami.constants.headers.DEVICE_TYPE,
        deviceClass                 : io.inugami.constants.headers.DEVICE_CLASS,
        deviceOsVersion             : io.inugami.constants.headers.DEVICE_OS_VERSION,
        deviceVersion               : io.inugami.constants.headers.DEVICE_VERSION,
        deviceNetworkType           : io.inugami.constants.headers.DEVICE_NETWORK_TYPE,
        deviceNetworkSpeedDown      : io.inugami.constants.headers.DEVICE_NETWORK_SPEED_DOWN,
        deviceNetworkSpeedLatency   : io.inugami.constants.headers.DEVICE_NETWORK_SPEED_LATENCY
    },
    code  :{
        types:io.inugami.constants.codes.types
    },
    lorem : {
        data:io.inugami.constants.lorem.data,
        size:io.inugami.constants.lorem.size

    },
    context:{
        url         :io.inugami.constants.context.URL,
        protocole   :io.inugami.constants.context.PROTOCOLE,
        context     :io.inugami.constants.context.CONTEXT,
        page        :io.inugami.constants.context.PAGE,
        option      :io.inugami.constants.context.OPTION,
        optionType  :io.inugami.constants.context.OPTION_TYPE,
        locale      :io.inugami.constants.context.LOCALE
    },
    math:{
        twoPi       :io.inugami.constants.math.TWO_PI,
        twoPiRatio  :io.inugami.constants.math.TWO_PI_RATIO
    },
    token                       :io.inugami.constants.token,
    uuid                        :io.inugami.constants.uuid,
    deviceIdentifier            :io.inugami.constants.deviceIdentifier,
    deviceClass                 :io.inugami.constants.deviceClass,
    deviceType                  :io.inugami.constants.deviceType,
    deviceOsVersion             :io.inugami.constants.deviceOsVersion,
    deviceVersion               :io.inugami.constants.deviceVersion,
    deviceNetworkType           :io.inugami.constants.deviceNetworkType,
    deviceNetworkSpeedDown      :io.inugami.constants.deviceNetworkSpeedDown,
    deviceNetworkSpeedLatency   :io.inugami.constants.deviceNetworkSpeedLatency,
};


