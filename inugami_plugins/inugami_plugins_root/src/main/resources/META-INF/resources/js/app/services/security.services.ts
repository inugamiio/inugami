import {Injectable}                             from '@angular/core';
import {Router}                                 from '@angular/router';
import {HttpClient}                             from '@angular/common/http';

import {HeaderServices}                         from './../services/header.services';
import {SessionScope}                           from './../scopes/session.scope';

import {DATA_EXTRACTORS} from './../angular/data.extractors';

@Injectable({
    providedIn: 'root',
})
export class SecurityServices {

    /**************************************************************************
    * ATTRIBUTES
    **************************************************************************/
    private urls        : any;

    /**************************************************************************
    * CONSTRUCTORS
    **************************************************************************/
    constructor(private http           : HttpClient,
                private router         : Router,
                private sessionScope   : SessionScope,
                private headerServices : HeaderServices) {
        this.urls = {
            login  : CONTEXT_PATH+"rest/security/authenticate",
            logout : CONTEXT_PATH+"rest/security/authenticate/logout"
        };
        
        
    }

    /**************************************************************************
    * API
    **************************************************************************/
    public login(login, password){
        let options = this.headerServices.buildHeader({
            'Content-Type': 'application/x-authc-username-password+json'
        });
        
        let json = JSON.stringify({
            "userId":login,
            "password":password
        });
        let session = this.sessionScope;
        return this.http.post(this.urls.login,json,{"headers":options,observe: "response" })
                        .toPromise()
                        .then(res  => {
                            session.setCorrelationId(res.headers.get(io.inugami.constants.headers.CORRELATION_ID));
                            return res.body;
                        })
                        .catch(this.handleError);
    }

    public loginWithToken(token){
        let options = this.headerServices.buildHeader({
            'Content-Type': 'application/x-authc-token',
            'token': token
        });
        let session = this.sessionScope;
        return this.http.post(this.urls.login,null,{"headers":options,observe: "response" })
                        .toPromise()
                        .then(res  => {
                            session.setCorrelationId(res.headers.get(io.inugami.constants.headers.CORRELATION_ID));
                            return res.body;
                        })
                        .catch(this.handleError);
    }

    public logout(){
        let token   = this.sessionScope.getToken();
        let options = this.headerServices.buildHeader({
            'Content-Type': 'application/x-authc-token',
            'token': token
        });
        this.sessionScope.user = null;
        this.sessionScope.resetCorrelationId();
        localStorage.removeItem("inugami_token");
        return this.http.post(this.urls.logout,null,{"headers":options,observe: "response"})
                        .toPromise()
                        .then(res  => {
                            window.location.href = CONTEXT_PATH;
                        })
                        .catch(error=>{
                            this.handleError;
                            window.location.href = CONTEXT_PATH;
                        });

    }
      
    /**************************************************************************
    * HANDLING ERRORS
    **************************************************************************/
    private handleError(error: any): Promise<any> {
        console.error(error);
        let errorData = error;
        try {
            errorData = error.json();
        }catch(e){}

        return Promise.reject(errorData);
    }

}
