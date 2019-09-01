import {Injectable}                              from '@angular/core';
import {HttpClient}                              from '@angular/common/http';
import {SessionScope}                            from './../../scopes/session.scope';
import {HeaderServices}                          from './../header.services'

@Injectable({
    providedIn: 'root',
})
export class HttpServices {


    /**************************************************************************
    * CONSTRUCTORS
    **************************************************************************/
    constructor(private http: HttpClient,
                private headerServices:HeaderServices,
                private sessionScope : SessionScope) {

    }


    /**************************************************************************
    * API
    **************************************************************************/
    public get(url:string, header?:any):Promise<any>{
        if(this.sessionScope.isConnected()){
            let options = this.headerServices.buildHeader(header);
            let session = this.sessionScope;
            return this.http.get(url,{"headers":options,observe: "response" })
                .toPromise()
                .then(res  => {
                    session.setCorrelationId(res.headers.get(org.inugami.constants.headers.CORRELATION_ID));
                    return res.body;
                })
                .catch(this.handleError);
        }else{
            return Promise.reject("please login");
        }
    }


    public post(url:string, data?, header?:any):Promise<any>{
        let options = this.headerServices.buildHeader(header);
        let session    = this.sessionScope;
        return this.http
                   .post(url,JSON.stringify(data),{"headers":options,observe: "response" })
                   .toPromise()
                   .then(res  => {
                        session.setCorrelationId(res.headers.get(org.inugami.constants.headers.CORRELATION_ID));
                        return res.body;
                   })
                   .catch(this.handleError);
    }

    public put(url:string, data, header?:any):Promise<any>{
        let options    = this.headerServices.buildHeader(header);
        let session    = this.sessionScope;
        return this.http
                   .put(url,JSON.stringify(data),{"headers":options,observe: "response" })
                   .toPromise()
                   .then(res  => {
                        session.setCorrelationId(res.headers.get(org.inugami.constants.headers.CORRELATION_ID));
                        return res.body;
                   })
                   .catch(this.handleError);
    }

    public delete(url:string, data?, header?:any):Promise<any>{
        let session     = this.sessionScope;
        let options     = this.headerServices.buildHeader(header);
        
        return this.http
                   .delete(url,{"headers":options,observe: "response" })
                   .toPromise()
                   .then(res  => {
                        session.setCorrelationId(res.headers.get(org.inugami.constants.headers.CORRELATION_ID));
                        return res.body;
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
            }catch(err){
                errorData.data = error._body + " \n"+error.statusText;
                errorData.data = errorData.data.replace(/&quot;/g,'"')
             }
             
        }
        return Promise.reject(errorData);
    }

}
