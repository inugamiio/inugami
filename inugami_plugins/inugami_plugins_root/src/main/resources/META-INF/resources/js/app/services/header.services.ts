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
    private LOGGER      : any =  org.inugami.logger.factory("PluginsService");
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

      result[org.inugami.constants.headers.DEVICE_IDENTIFIER]            = org.inugami.constants.deviceIdentifier;
      result[org.inugami.constants.headers.CORRELATION_ID]               = this.sessionScope.getCorrelationId();
      result[org.inugami.constants.headers.DEVICE_TYPE]                  = org.inugami.constants.deviceType;
      result[org.inugami.constants.headers.DEVICE_CLASS]                 = org.inugami.constants.deviceClass;
      result[org.inugami.constants.headers.DEVICE_OS_VERSION]            = org.inugami.constants.deviceOsVersion;
      result[org.inugami.constants.headers.DEVICE_VERSION]               = org.inugami.constants.deviceVersion;
      result[org.inugami.constants.headers.DEVICE_NETWORK_TYPE]          = org.inugami.constants.deviceNetworkType;
      result[org.inugami.constants.headers.DEVICE_NETWORK_SPEED_DOWN]    = org.inugami.constants.deviceNetworkSpeedDown;
      result[org.inugami.constants.headers.DEVICE_NETWORK_SPEED_LATENCY] = org.inugami.constants.deviceNetworkSpeedLatency;
      
      
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
