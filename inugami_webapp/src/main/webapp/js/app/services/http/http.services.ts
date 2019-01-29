import {Injectable}                              from '@angular/core';
import {Http, Response,Headers, RequestOptions}  from '@angular/http';
import {SessionScope}                            from './../../scopes/session.scope';
import {HeaderServices}                          from './../header.services'

@Injectable()
export class HttpServices {


    /**************************************************************************
    * CONSTRUCTORS
    **************************************************************************/
    constructor(private http: Http,
                private headerServices:HeaderServices,
                private sessionScope : SessionScope) {

    }


    /**************************************************************************
    * API
    **************************************************************************/
    public get(url:string, header?:any){
        if(this.sessionScope.isConnected()){
            let headerData = this.headerServices.buildHeader(header);
            let session = this.sessionScope;
            return this.http.get(url,headerData)
                .toPromise()
                .then(res  => {
                    session.setCorrelationId(res.headers.get(org.inugami.constants.headers.CORRELATION_ID));
                    return res.json()
                })
                .catch(this.handleError);
        }else{
            return Promise.reject("please login");
        }
    }


    public post(url:string, data, header?:any){
        let headerData = this.headerServices.buildHeader(header);
        let session    = this.sessionScope;
        return this.http
                   .post(url,JSON.stringify(data),headerData)
                   .toPromise()
                   .then(res  => {
                        session.setCorrelationId(res.headers.get(org.inugami.constants.headers.CORRELATION_ID));
                        return res;
                   })
                   .catch(this.handleError);
    }

    public put(url:string, data, header?:any){
        let headerData = this.headerServices.buildHeader(header);
        let session    = this.sessionScope;
        return this.http
                   .put(url,JSON.stringify(data),headerData)
                   .toPromise()
                   .then(res  => {
                        session.setCorrelationId(res.headers.get(org.inugami.constants.headers.CORRELATION_ID));
                        return res;
                   })
                   .catch(this.handleError);
    }

    public delete(url:string, data, header?:any){
        let session     = this.sessionScope;
        let headerData  = this.headerServices.buildHeader(header);
        headerData.body = JSON.stringify(data);
        return this.http
                   .delete(url,headerData)
                   .toPromise()
                   .then(res  => {
                        session.setCorrelationId(res.headers.get(org.inugami.constants.headers.CORRELATION_ID));
                        return res;
                   })
                   .catch(this.handleError);
    }

    /**************************************************************************
    * HANDLING ERRORS
    **************************************************************************/
    private handleError(error: any): Promise<any> {
        let errorData = {
            "status":error.status||500,
            "statusText":error.statusText|| "error",
            "url": error.url, 
            "data":null
        }

        if(isNotNull(error._body)){
            let json = null;
            try{
                json=JSON.parse(error._body);
                errorData.data=json;
            }catch(err){}
             
        }
        return Promise.reject(errorData);
    }

}
