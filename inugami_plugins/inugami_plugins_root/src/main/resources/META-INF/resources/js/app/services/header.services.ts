import {Injectable}                                 from '@angular/core';
import {HttpHeaders}                                from '@angular/common/http';
import {SessionScope}                               from './../scopes/session.scope';



@Injectable({
  providedIn: 'root',
})
export class HeaderServices {

    /**************************************************************************
    * ATTRIBUTES
    **************************************************************************/
    private LOGGER      : any =  io.inugami.logger.factory("PluginsService");
    private urls        : any;

    /**************************************************************************
    * CONSTRUCTORS
    **************************************************************************/
    constructor(private sessionScope : SessionScope) {
    }

    /**************************************************************************
    * API
    **************************************************************************/
    public buildHeader(headerInfos) : HttpHeaders{
      let result = {};
      let headerData = isNull(headerInfos)?{}:headerInfos;
      if(this.sessionScope.isConnected()){
        result["Authorization"]=this.sessionScope.getToken();
      }

      result[io.inugami.constants.headers.DEVICE_IDENTIFIER]            = io.inugami.constants.deviceIdentifier;
      result[io.inugami.constants.headers.CORRELATION_ID]               = this.sessionScope.getCorrelationId();
      result[io.inugami.constants.headers.DEVICE_TYPE]                  = io.inugami.constants.deviceType;
      result[io.inugami.constants.headers.DEVICE_CLASS]                 = io.inugami.constants.deviceClass;
      result[io.inugami.constants.headers.DEVICE_OS_VERSION]            = io.inugami.constants.deviceOsVersion;
      result[io.inugami.constants.headers.DEVICE_VERSION]               = io.inugami.constants.deviceVersion;
      result[io.inugami.constants.headers.DEVICE_NETWORK_TYPE]          = io.inugami.constants.deviceNetworkType;
      result[io.inugami.constants.headers.DEVICE_NETWORK_SPEED_DOWN]    = io.inugami.constants.deviceNetworkSpeedDown;
      result[io.inugami.constants.headers.DEVICE_NETWORK_SPEED_LATENCY] = io.inugami.constants.deviceNetworkSpeedLatency;
      
      
      if(isNotNull(headerInfos)){
         for(let key of Object.keys(headerInfos)){
          result[key]=headerInfos[key];
         }
      }

      let resultData = {};
      for(let key of Object.keys(result)){
        if(isNotNull(result[key])){
          resultData[key]=result[key];
        }
      }
      
      return new  HttpHeaders(resultData);
    }
}
