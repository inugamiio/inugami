import {Component,OnInit,OnDestroy}      from '@angular/core';
import {ActivatedRoute,Router}           from '@angular/router';

import {SecurityServices}                from './../services/security.services';
import {SessionScope}                    from './../scopes/session.scope';
import {Bloc}                            from './../components/display/bloc/bloc';


@Component({
    templateUrl: 'js/app/view/login.view.html',
    directives : [Bloc]
})
export class LoginView implements OnInit, OnDestroy{

  /**************************************************************************
  * ATTRIBUTES
  **************************************************************************/
  private login                 : string;
  private password              : string;
  private error                 : string;
  private disableAction         : string;
  private localStorageTokenKey  : string = org.inugami.constants.token;
  private enableLoginForm       : string = "hidden";

  /**************************************************************************
  * CONSTRUCTOR
  **************************************************************************/
  constructor(private route: ActivatedRoute,
              private router: Router,
              private securityServices : SecurityServices,
              private sessionScope : SessionScope){
                 
  }


  ngOnInit() {
    this.sessionScope.closeMainMenu();

    let storedToken = localStorage.getItem(this.localStorageTokenKey);
    if(isNotNull(storedToken)){
        this.securityServices.loginWithToken(storedToken)
                             .then (data =>{this.onLoginSuccess(data)})
                             .catch(error=>{this.showForm()});
    }else{
      this.showForm();
    }
    
  }

  /**************************************************************************
  * ACTION
  **************************************************************************/
  private showForm(){
    localStorage.removeItem(this.localStorageTokenKey);
    this.enableLoginForm = "show";
  }
  public handleKeyDown(event:any){
    if(event.keycode == 13){
     this.processLogin();
    }
  }
  public processLogin(){
     this.error=null;
     if(isNull(this.disableAction)){
        this.disableAction = "disable";
      
        this.securityServices.login(this.login,this.password)
                            .then (data =>{this.onLoginSuccess(data)})
                            .catch(error=>{this.loginFail()});
     }
  }


  /**************************************************************************
  * HANDLING
  **************************************************************************/
  private loginFail(){
    this.error="Echec d'identification, veuillez vérifier votre login et mot de passe";
    this.disableAction =null;
  }

  private onLoginSuccess(data){
    if(isNotNull(data.token)){
      this.sessionScope.user=data;
      org.inugami.sse.userToken=this.sessionScope.getToken();
  
      localStorage.setItem(this.localStorageTokenKey,data.token);
      this.processRedirect();
    }
    else{
      this.loginFail();
    }
  }


  private processRedirect(){
    let home = "/";
    if(isNotNull(this.sessionScope._postRedirect) && "/login"!=this.sessionScope._postRedirect){
      home = this.sessionScope._postRedirect;
    }
    this.disableAction =null;
    this.router.navigate([home]);

  }
   
}
