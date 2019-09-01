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
        JSON:org.inugami.constants.mimetype.JSON
    },
    headers : {
        defaultJson:{
           contentType: org.inugami.constants.headers.DEFAULT_JSON
        },
        deviceIdentifier            : org.inugami.constants.headers.DEVICE_IDENTIFIER,
        correlationId               : org.inugami.constants.headers.CORRELATION_ID,
        deviceType                  : org.inugami.constants.headers.DEVICE_TYPE,
        deviceClass                 : org.inugami.constants.headers.DEVICE_CLASS,
        deviceOsVersion             : org.inugami.constants.headers.DEVICE_OS_VERSION,
        deviceVersion               : org.inugami.constants.headers.DEVICE_VERSION,
        deviceNetworkType           : org.inugami.constants.headers.DEVICE_NETWORK_TYPE,
        deviceNetworkSpeedDown      : org.inugami.constants.headers.DEVICE_NETWORK_SPEED_DOWN,
        deviceNetworkSpeedLatency   : org.inugami.constants.headers.DEVICE_NETWORK_SPEED_LATENCY
    },
    code  :{
        types:org.inugami.constants.codes.types
    },
    lorem : {
        data:org.inugami.constants.lorem.data,
        size:org.inugami.constants.lorem.size

    },
    context:{
        url         :org.inugami.constants.context.URL,
        protocole   :org.inugami.constants.context.PROTOCOLE,
        context     :org.inugami.constants.context.CONTEXT,
        page        :org.inugami.constants.context.PAGE,
        option      :org.inugami.constants.context.OPTION,
        optionType  :org.inugami.constants.context.OPTION_TYPE,
        locale      :org.inugami.constants.context.LOCALE
    },
    math:{
        twoPi       :org.inugami.constants.math.TWO_PI,
        twoPiRatio  :org.inugami.constants.math.TWO_PI_RATIO
    },
    token                       :org.inugami.constants.token,
    uuid                        :org.inugami.constants.uuid,
    deviceIdentifier            :org.inugami.constants.deviceIdentifier,
    deviceClass                 :org.inugami.constants.deviceClass,
    deviceType                  :org.inugami.constants.deviceType,
    deviceOsVersion             :org.inugami.constants.deviceOsVersion,
    deviceVersion               :org.inugami.constants.deviceVersion,
    deviceNetworkType           :org.inugami.constants.deviceNetworkType,
    deviceNetworkSpeedDown      :org.inugami.constants.deviceNetworkSpeedDown,
    deviceNetworkSpeedLatency   :org.inugami.constants.deviceNetworkSpeedLatency,
};


