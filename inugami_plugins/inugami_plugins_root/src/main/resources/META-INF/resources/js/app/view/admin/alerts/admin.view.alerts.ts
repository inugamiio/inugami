import {Component,Inject,OnInit,Input,Output,forwardRef}    from '@angular/core';
import {HttpClient}                                         from '@angular/common/http';

import {GrowlModule}                                        from 'primeng/growl';
import {Message}                                            from 'primeng/api';
import {TableModule}                                        from 'primeng/table';




import {AlertsCrudServices}                                 from './../../../services/http/alerts.crud.services'
import {AlertEntity}                                        from './../../../models/alert.entity';
import {InputBloc}                                          from './../../../components/forms/input.bloc';
import {Msg}                                                from './../../../components/msg/msg';
import {AdminViewAlertEdit}                                 from './../../../view/admin/alerts/admin.view.alert.edit'

@Component({
  selector      : 'admin-view-alerts',
  templateUrl   : 'js/app/view/admin/alerts/admin.view.alerts.html',
  directives    : [InputBloc,Msg,AdminViewAlertEdit]
})
export class AdminViewAlerts{

    /**************************************************************************
    * ATTRIBUTES
    **************************************************************************/
    private alerts                : AlertEntity[]  = [];
    private selectedAlert         : AlertEntity;
    //private msgs                  : Message[] = [];
    private sectionSelected       : string = "table";

    private msgLabels         : any = {
        uid     : io.inugami.formatters.message("alert.edit.alert.name"),
        created : io.inugami.formatters.message("alert.edit.alert.created"),
        level   : io.inugami.formatters.message("alert.edit.alert.level"),
        label   : io.inugami.formatters.message("alert.edit.alert.label"),
        actions : io.inugami.formatters.message("title.generic.actions")
    }
    /**************************************************************************
    * CONSTRUCTOR
    **************************************************************************/
    constructor(private alertsCrudServices : AlertsCrudServices,private http : HttpClient) {
        this.initAlerts();

        let self = this;
        io.inugami.events.addEventListener("administration_alerts_update", function(event){
            self.initAlerts();
        });
        
       
    }
    

    /**************************************************************************
    * INIT
    **************************************************************************/
    private initAlerts(){
        this.alertsCrudServices.findAll().then(data =>{
            this.alerts = data;
        });
    }

    /**************************************************************************
    * ACTIONS
    **************************************************************************/
    onMessage(msg){
        this.msgs.push(msg);
        this.initAlerts();
    }
    
    addMessageAndWait(msg){
        this.msgs.push(msg);
        let self = this;
        setTimeout(function(){
            self.msgs=[] 
        }, 5000);
    }

    onAlertSelect(event){
        if(isNotNull(event.data) && isNotNull(event.data.dynamicAlerting) && event.data.dynamicAlerting){
            this.selectedAlert=event.data;
            this.setActiveSection('createView');
        }
    }

    showNewAlert(){
        this.selectedAlert = null;
        this.setActiveSection('createView');
    }
    
    cleanMessage(){
        this.msgs =[];
    }

    unSelectedAlert(){
        this.selectedAlert=null;
    }


    onAlertActivationChange(alert){
        if(alert.enable){
            this.alertsCrudServices.enableAlert(alert.uid)
                                   .then((data)=>{
                                        let msg = io.inugami.formatters.messageValue("alert.enable.success");
                                        this.addMessageAndWait({"severity":'success', "summary":msg});
                                   })
                                   .catch((error)=>this.addMessageAndWait(error));
        }else{
            this.alertsCrudServices.disableAlert(alert.uid)
                                   .then((data)=>{
                                        let msg = io.inugami.formatters.messageValue("alert.disable.success");
                                        this.addMessageAndWait({"severity":'warn', "summary":msg});
                                    })
                                    .catch((error)=>this.addMessageAndWait(error));
        }
    }
    
    deleteAlert(alert:AlertEntity){
        let uids = [alert.uid];
        this.alertsCrudServices.delete(uids).then((data)=>{
            let msg = io.inugami.formatters.messageValue("alert.delete.success");
            this.onMessage({"severity":'success', "summary":msg});;
        })
        .catch((error)=>this.handlerError(error));
    }

    /*************************************************************************
    * HANDLER ERROR
    **************************************************************************/
    handlerError(error){
        let errorMsg = null;
        if(isNotNull(error.data) && isNotNull(error.data.errorCode)){
            errorMsg = io.inugami.formatters.messageValue(["dashboard.tv.error",error.data.errorCode].join("."));
        }
        if(isNull(errorMsg)){
            errorMsg = error.statusText;
        }

        this.onMessage({"severity":'error', "summary":errorMsg});
    }

    /*************************************************************************
    * GETTERS & SETTERS
    **************************************************************************/
    isActiveSection(name:string){
        return this.sectionSelected == name;
    }
    setActiveSection(name:string){
        this.sectionSelected = name;
        this.msgs=[];
        if("table" ==name){
            this.initAlerts();
        }
    }

    formateDate(time:number){
        let timestamp = Math.round(time/1000);
        return io.inugami.formatters.timestampToDate(timestamp);
    }
    
    isDynamicAlert(value:boolean){
        return value?"alert-element dynamic-alert fas fa-cog":"alert-element  default-alert";
    }
}
