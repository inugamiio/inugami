import {Injectable}                              from '@angular/core';
import {SessionScope}                            from './../scopes/session.scope';
import {HttpServices}                            from './../services/http/http.services';

@Injectable()
export class AdminService {

    /**************************************************************************
    * ATTRIBUTES
    **************************************************************************/
    private urls: any;

    /**************************************************************************
    * CONSTRUCTORS
    **************************************************************************/
    constructor(
                private sessionScope   : SessionScope,
                private httpService    : HttpServices) {
        this.urls = {
            upTime      : CONTEXT_PATH+"rest/administration/uptime",
            forceRun    : CONTEXT_PATH+"rest/administration/force-run",
            run         : CONTEXT_PATH+"rest/administration/run",
            refresh     : CONTEXT_PATH+"rest/administration/forceRefresh",
            beginUpdate : CONTEXT_PATH+"rest/administration/beginUpdate",
            endUpdate   : CONTEXT_PATH+"rest/administration/endUpdate",
            cache       : CONTEXT_PATH+"rest/administration/cache",
            operation   : CONTEXT_PATH+"rest/administration/operation"
        };
    }

    
    /**************************************************************************
    * API
    **************************************************************************/
    getUpTime(): Promise<any> {
        return this.processGet(this.urls.upTime);
    }
    forceRun(): Promise<any> {
        return this.processGet(this.urls.forceRun);
    }
    run(): Promise<any> {
        return this.processGet(this.urls.run);
    }
    forceRefresh(): Promise<any> {
        return this.processGet(this.urls.refresh);
    }

    beginUpdate(): Promise<any> {
        return this.processGet(this.urls.beginUpdate);
    }
    endUpdate(): Promise<any> {
        return this.processGet(this.urls.endUpdate);
    }
    cache(): Promise<any> {
        return this.processGet(this.urls.cache);
    }
    clearAllCaches(): Promise<any> {
        return this.processDelete(this.urls.cache);
    }
    clearCache(id): Promise<any> {
        return this.processDelete([this.urls.cache,id].join('/'));
    }

    executeOperation(index): Promise<any> {
        let url = [this.urls.operation,index].join('/');
        return this.httpService.post(url);
    }
    /**
    * Allow to grab data from backend webservice
    **/
    private processGet(url) : Promise<any> {
        if(this.sessionScope.hasRole('admin')){
        return this.httpService
                 .get(url)
                 .then(res  => {
                    return res;
                 })
                 .catch(this.handleError);
        }else{
            return this.handleError("only administrator can use this service");
        }
    }




    private processDelete(url): Promise<any> {
        if(this.sessionScope.hasRole('admin')){
        return this.httpService
                   .delete(url)
                   .then(res  => {
                        return res;
                   })
                   .catch(this.handleError);
        }else{
            return this.handleError("only administrator can use this service");
        }
    }



    private handleError(error: any): Promise<any> {
        return Promise.reject(error.message || error);
    }

}
