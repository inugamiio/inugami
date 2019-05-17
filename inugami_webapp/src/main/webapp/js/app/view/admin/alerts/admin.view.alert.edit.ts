import {Component,Inject,OnInit,Input,
        Output,forwardRef,EventEmitter}                     from '@angular/core';
import {NG_VALUE_ACCESSOR,ControlValueAccessor,Validators,
        FormGroup,FormControl,FormArray,FormBuilder}        from '@angular/forms';

import {AlertsCrudServices}                                 from './../../../services/http/alerts.crud.services';
import {AlertEntity}                                        from './../../../models/alert.entity';
import {InputBloc}                                          from './../../../components/forms/input.bloc';
import {Msg}                                                from './../../../components/msg/msg';

export const ADMIN_VIEW_ALERT_EDIT_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => AdminViewAlertEdit),
    multi: true
    };

    

@Component({
  selector      : 'admin-view-alert-edit',
  templateUrl   : 'js/app/view/admin/alerts/admin.view.alert.edit.html',
  providers     : [ADMIN_VIEW_ALERT_EDIT_VALUE_ACCESSOR],
  directives    : [InputBloc,Msg]
})
export class AdminViewAlertEdit implements AfterViewInit{

    /**************************************************************************
    * ATTRIBUTES
    **************************************************************************/
    @Input() styleClass                 : string;
    @Input() edit                       : boolean = false;

    private detailData                  : string;
    private validators                  : any = org.inugami.validators;
    private innerValue                  : AlertEntity; 
    private isNotEdit                   : boolean;

    private activationDaysData          : any;
    private dynamicLevelsLevels         : any;
    private channels                    : any;
    private addedLevel                  : any = {};
    
    private alertForm                   : any;

    private formatter                   : any;

    @Output() onClose                   : EventEmitter<any> = new EventEmitter();
    @Output() onError                   : EventEmitter<any> = new EventEmitter();
    @Output() onSuccess                 : EventEmitter<any> = new EventEmitter();
    @Output() onCleanMessage            : EventEmitter<any> = new EventEmitter();


    /**************************************************************************
    * CONSTRUCTOR
    **************************************************************************/
    constructor(private alertsCrudServices : AlertsCrudServices,private fb: FormBuilder) {
        this.initValue();
    }
    ngAfterContentInit(){
        this.isNotEdit = !this.edit;
    }

    /**************************************************************************
    * INIT
    **************************************************************************/
    private initValue(){
        if(isNull(this.innerValue)){
            this.innerValue = new AlertEntity();
        }
        if(isNull(this.innerValue.duration)){
            this.innerValue.duration = 60;
        }
        if(isNull(this.innerValue.level)){
            this.innerValue.level= "info";
        }
        if(isNull(this.innerValue.channel)){
            this.innerValue.channel = "@all";
        }
        
        if(isNull(this.innerValue.data)){
            this.detailData = null;
        }else{
            let strJson = this.innerValue.data.trim();
            if(strJson.startsWith('"{')){
                strJson = strJson.substring(1,strJson.length);
            }
            if(strJson.endsWith('}"')){
                strJson = strJson.substring(0,strJson.length-1);
            }
            this.detailData =  strJson.split("\\u0022").join("\""); 
            
            
        }
        if(isNull(this.activationDaysData)){
            let timeSlots = []
            let days = [];
            let lineData = {
                days:days,
                timeSlots:timeSlots
            }

            this.activationDaysData = [lineData];
        }
        if(isNull(this.dynamicLevelsLevels)){
            this.dynamicLevelsLevels = [];
        }
        if(isNull(this.channels)){
            this.channels = ["OpsGenie","Teams","SSE","Mail"];
        }
        // faire gavffa a bien init channeles avant de faire ca 
        const channelsFormControls = this.channels.map(control => new FormControl(false));
        
        if(isNull(this.alertForm)){
            this.alertForm = this.fb.group({
                name: ['',Validators.required],
                duration:  [''],
                visibility: [''],
                mainMessage: [''],
                detailedMessage: [''],
                tag:[''],
                channelsData: this.fb.array(channelsFormControls),
                sources: this.fb.group({
                    dataProvider: [''],
                    interval: [''],
                    from:[''],
                    to:[''],
                    query:['']
                }),                         
                activation: this.fb.array([this.createFormActivationLine()]),
                levelPointsBeforeTriggered: this.fb.array([]),
                dynamicLevels:[''],
                scripts:['']
            })                
        }else{
            this.alertForm.reset();
        }
        this.formatter = null;
    }
    

    /**************************************************************************
    * ACTIONS
    **************************************************************************/
    onSuccessSave(){
        let msg= org.inugami.formatters.messageValue("action.success");
        this.onSuccess.emit({"severity":'success', "summary":msg});
    }
    cleanMessage(){
        this.onCleanMessage.emit();
    }
    handlerError(error){
        let errorMsg = null;
        if(isNotNull(error.data) && isNotNull(error.data.errorCode)){
            errorMsg = org.inugami.formatters.messageValue(["dashboard.tv.error",error.data.errorCode].join("."))
        }
        if(isNull(errorMsg)){
            errorMsg = error.statusText;
        }

        this.onSuccess.emit({"severity":'error', "summary":errorMsg});
    }
    
    /*************************************************************************
    * ACTIONS
    **************************************************************************/
    saveAlert(){
        this.cleanMessage();
        let alerts = [this.innerValue];
        if(this.edit){
            this.alertsCrudServices.merge(alerts)
                                   .then((data)=>{
                                        this.onSuccessSave();
                                    })
                                    .catch((error)=>this.handlerError(error));
        }else{
            this.alertsCrudServices.save(alerts)
                                   .then((data)=>{
                                        this.onSuccessSave();
                                    })
                                    .catch((error)=>this.handlerError(error));
        }
    }


    saveAlertAndClose(){
        this.saveAlert();
        this.initValue();
    }

    close(){
        this.initValue();
        this.onClose.emit();
    }

    removeActivationLine(index){
        this.activationDaysData.splice(index,1);
        this.removeFormActivationLine(index);
    }

    addActivationLine(){
        this.activationDaysData.push({});
        this.addFormActivationLine();
    }
    addDynamicLevelsLevel(graph){
        if( isNotNull(this.addedLevel.pointsBeforeTriggered)    &&
            isNotNull(this.addedLevel.name)                     &&
            this.addedLevel.name != ""                          &&
            this.addedLevel.pointsBeforeTriggered != ""         &&
            isNull(org.inugami.validators.notNegativeNumber(this.addedLevel.pointsBeforeTriggered))){
               if(contains(this.addedLevel,this.dynamicLevelsLevels,function(a,b){
                   return a.name === b.name;
               })){
                   alert(org.inugami.formatters.message("alert.edit.level.already.added"));
               }else{
                    this.dynamicLevelsLevels.push({pointsBeforeTriggered:this.addedLevel.pointsBeforeTriggered,
                                                    name:this.addedLevel.name});
                    this.addFormLevelLine();
                    
                    graph.addNewData(this.addedLevel.name);
                    
                }
        }
    }

    removeDynamicLevelsLevel(name,i,graph){
        let index = indexOf(name,this.dynamicLevelsLevels,function(list,value){
            return list.name === value;
        })
        if(index > -1){
            this.dynamicLevelsLevels.splice(index,1);
            this.removeFormLevelLine(i);
            graph.deleteLine(name);
        }
    }


    fileChanged(event){
        let file = event.target.files[0];
        let reader = new FileReader();
        let text;
        reader.onload = (e) => {
            text = reader.result;
        }
        reader.readAsText(file);
       
        //apply data on form
    }
 /*****************************************************************************
  * IMPLEMENTS ControlValueAccessor
  *****************************************************************************/
    writeValue(value: any) {
        this.innerValue = value;
        this.initValue();
    }
    registerOnChange(fn: any) {
        this.onChangeCallback = fn;
    }
    registerOnTouched(fn: any) {
        this.onTouchedCallback = fn;
    }

  /*****************************************************************************
  * FORMS TOOLS METHOD
  *****************************************************************************/
    get channelsData(){
        return this.alertForm.get('channelsData') as FormArray;
    }

    createFormActivationLine() : FormGroup{
        return this.fb.group({
            days: '',
            timeSlots: '',
        })
    }
    addFormActivationLine(){
        this.alertForm.get('activation').push(this.createFormActivationLine());
    }

    removeFormActivationLine(index){
        this.alertForm.get('activation').removeAt(index);
    }
    createFormLevelLine(){
        return this.fb.group({
            points: '',
            name: ''
        })
    }

    addFormLevelLine(){
        let array = this.alertForm.get('levelPointsBeforeTriggered');
        let group = this.createFormLevelLine()
        array.push(group);
        group.patchValue({name:this.addedLevel.name,points:this.addedLevel.pointsBeforeTriggered});
    }

    removeFormLevelLine(index){
        this.alertForm.get('levelPointsBeforeTriggered').removeAt(index);
    }

    setLevelName(event){
        this.addedLevel.name = event;
    }

    setPointsBeforeTriggered(event){
        this.addedLevel.pointsBeforeTriggered = event;
    }

    setGraphMode(event,graph){
        graph.dynamicMode = event;
    }

    setGraphMinValue(event,graph){
      graph.setMinValue(parseFloat(event.target.value));
    }
    
    setGraphMaxValue(event,graph){
        graph.setMaxValue( parseFloat(event.target.value)); 
    }

    setFormatter(event){
        let precision  = parseFloat(event.target.value);
        if(precision > 0){
            precision = Math.pow(10,precision);
            this.formatter = (value) => Math.round(value * precision + Number.EPSILON) / precision;
        }
    }
}
