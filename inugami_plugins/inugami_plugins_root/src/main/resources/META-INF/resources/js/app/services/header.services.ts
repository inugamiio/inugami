import {Injectable}                                 from '@angular/core';
import {HttpRequest}                                from '@angular/common/http';
import {SessionScope}                               from './../scopes/session.scope';



@Injectable()
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
    public buildHeader(headerInfos) : HttpRequest{
      
      let headerData = isNull(headerInfos)?{}:headerInfos;
      if(this.sessionScope.isConnected()){
        headerData["Authorization"]=this.sessionScope.getToken();
      }

      headerData[org.inugami.constants.headers.DEVICE_IDENTIFIER]            = org.inugami.constants.deviceIdentifier;
      headerData[org.inugami.constants.headers.CORRELATION_ID]               = this.sessionScope.getCorrelationId();
      headerData[org.inugami.constants.headers.DEVICE_TYPE]                  = org.inugami.constants.deviceType;
      headerData[org.inugami.constants.headers.DEVICE_CLASS]                 = org.inugami.constants.deviceClass;
      headerData[org.inugami.constants.headers.DEVICE_OS_VERSION]            = org.inugami.constants.deviceOsVersion;
      headerData[org.inugami.constants.headers.DEVICE_VERSION]               = org.inugami.constants.deviceVersion;
      headerData[org.inugami.constants.headers.DEVICE_NETWORK_TYPE]          = org.inugami.constants.deviceNetworkType;
      headerData[org.inugami.constants.headers.DEVICE_NETWORK_SPEED_DOWN]    = org.inugami.constants.deviceNetworkSpeedDown;
      headerData[org.inugami.constants.headers.DEVICE_NETWORK_SPEED_LATENCY] = org.inugami.constants.deviceNetworkSpeedLatency;
      
      return new HttpRequest({ headers: new Headers(headerData) });
    }
}
