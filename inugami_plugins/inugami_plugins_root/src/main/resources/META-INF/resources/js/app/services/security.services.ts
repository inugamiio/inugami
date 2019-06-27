import {Injectable}                             from '@angular/core';
import {Router}                                 from '@angular/router';
import {Http}                                   from '@angular/http';

import {HeaderServices}                         from './../services/header.services';
import {SessionScope}                           from './../scopes/session.scope';

@Injectable()
export class SecurityServices {

    /**************************************************************************
    * ATTRIBUTES
    **************************************************************************/
    private urls        : any;

    /**************************************************************************
    * CONSTRUCTORS
    **************************************************************************/
    constructor(private http           : Http,
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
        return this.http.post(this.urls.login,json,options)
                        .toPromise()
                        .then(res  => {
                            session.setCorrelationId(res.headers.get(org.inugami.constants.headers.CORRELATION_ID));
                            return res.json()
                        })
                        .catch(this.handleError);
    }

    public loginWithToken(token){
        let options = this.headerServices.buildHeader({
            'Content-Type': 'application/x-authc-token',
            'token': token
        });
        let session = this.sessionScope;
        return this.http.post(this.urls.login,null,options)
                        .toPromise()
                        .then(res  => {
                            session.setCorrelationId(res.headers.get(org.inugami.constants.headers.CORRELATION_ID));
                            return res.json()
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
        return this.http.post(this.urls.logout,null,options)
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
        let errorData = error;
        try {
            errorData = error.json();
        }catch(e){}

        return Promise.reject(errorData);
    }

}
