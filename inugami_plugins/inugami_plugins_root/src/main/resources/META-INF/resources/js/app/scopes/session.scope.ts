import { Injectable }   from '@angular/core';
import {User}           from './../models/user';

@Injectable({
  providedIn: 'root',
})
export class SessionScope {
    /**************************************************************************
    * ATTRIBUTES
    **************************************************************************/
    private _postRedirect         : string;
    private showMainMenu          : boolean = false;
    private user                  : User;
    private data                  : any = {};
    private deviceIdentifier      : string = io.inugami.constants.deviceIdentifier;
    private correlationId         : string = null;



    /**************************************************************************
    * ACTIONS
    **************************************************************************/
    public toggleMainMenu(){
      if(this.showMainMenu){
        this.closeMainMenu();
      }else{
        this.openMainMenu();
      }
    }
    public closeMainMenu(){
      if(this.showMainMenu){
        this.showMainMenu =false;
        io.inugami.events.updateResize();
      }
    }
    
    public openMainMenu(){
        if(!this.showMainMenu){
          this.showMainMenu =true;
          io.inugami.events.updateResize();
        }
    }

    /**************************************************************************
    * TRACKING
    **************************************************************************/
    public getDeviceIdentifier(){
      return this.deviceIdentifier;
    }

    public getCorrelationId(){
      return this.correlationId;
    }
    public setCorrelationId(value:string){
      if((isNull(this.correlationId) || ""== this.correlationId) && isNotNull(value) && value != ""){
        this.correlationId=value;
      }
    }

    public resetCorrelationId(){
      this.correlationId=null;
    }

    /**************************************************************************
    * SECURITY
    **************************************************************************/
    public isConnected(){
      return isNotNull(this.user);
    }

    public hasRole(role){
      return isNotNull(this.user) && io.inugami.checks.contains(role,this.user.roles);
    }

    public isAdmin(){
      return this.hasRole('admin');
    }
    public getToken(){
      return "Token "+this.user.token;
    }


}
