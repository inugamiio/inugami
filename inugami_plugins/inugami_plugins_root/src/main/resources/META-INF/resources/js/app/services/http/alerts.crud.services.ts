import {Injectable}                         from '@angular/core';
import {GenericCrudServices}                from './../http/generic.crud.services';
import {AlertEntity}                        from './../../models/alert.entity';



@Injectable()
export class AlertsCrudServices extends GenericCrudServices<AlertEntity> {

    /**************************************************************************
    * CONSTRUCTORS
    **************************************************************************/
    getServiceUrl(){
        return "rest/alert";
    }

    public enableAlert(uid:string){
        let url = CONTEXT_PATH+[this.getServiceUrl(), "enable",uid].join("/");
        return this.httpServices.put(url,null,{'Content-Type': 'application/json'});
    }

    public disableAlert(uid:string){
        let url = CONTEXT_PATH+[this.getServiceUrl(), "disable",uid].join("/");
        return this.httpServices.put(url,null,{'Content-Type': 'application/json'});
    }

}
