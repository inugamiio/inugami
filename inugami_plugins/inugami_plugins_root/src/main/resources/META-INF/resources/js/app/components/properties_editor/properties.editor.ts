import {Component,Input,forwardRef}                         from '@angular/core';
import {NG_VALUE_ACCESSOR,ControlValueAccessor}             from '@angular/forms';
//import {ConfirmationService}                                from 'primeng/api';

import {InputBloc}                                          from './../forms/input.bloc';
import {Property}                                           from './../../models/property';

export const PROPERTIES_EDITOR_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => PropertiesEditor),
    multi: true
};


@Component({
  selector      : 'properties-editor',
  templateUrl   : 'js/app/components/properties_editor/properties.editor.html',
  directives    : [InputBloc ],
  providers     : [PROPERTIES_EDITOR_VALUE_ACCESSOR]
})
export class PropertiesEditor  implements ControlValueAccessor{

    /***************************************************************************
     * ATTRIBUTES
     **************************************************************************/
    @Input() styleClass                 : string;
    @Input() tabindex                   : number;
    //private property                    : Property = new Property();
    private innerValue                  : any      = null;
    private validators                  : any      = org.inugami.validators;
    private displayState                : any      = {};
    private focus                       : any      = {};

    /***************************************************************************
     * CONSTRUCTOR
    ***************************************************************************/
   /*
    constructor(private confirmationService: ConfirmationService) {
    }
*/
    /***************************************************************************
     * ACTIONS
    ***************************************************************************/
    public addProperty(){
        //this.innerValue.push(this.property);
        //this.property = new Property();
    }

    public onEnter(){
        if(event.keyCode == 13) {
            this.addProperty();
        }
        
    }

    public removeProperty(index:number){
        let property = this.innerValue[index];
        let confirmationLable   = org.inugami.formatters.message("properties.delete.confirmation",[property.key]);
/*
        this.confirmationService.confirm({

            message: confirmationLable,
            accept: () => {
                this.innerValue.splice(index,1);
            }
        });
        */
    }

    /***************************************************************************
    * Display / edit mode
    ***************************************************************************/
    public isDisplayMode(index : number){ 
       let state = this.displayState[index];
       if(isNull(state)){
            state = "display";
            this.displayState[index]=state;
       }
       return "display"==state;
    }

    public isEditMode(index : number){
        let state = this.displayState[index];
        if(isNull(state)){
             state = "display";
        }
        return "edit"==state;
    }

    public dblClickToggleEdit(index : number){
      if(this.displayState[index] != "edit"){
          this.toggleEdit(index);
      }
    }

    public toggleEdit(index : number){
        if(this.displayState[index] == null || this.displayState[index] == "display"){
            this.displayState[index] = "edit";
        }else{
            this.displayState[index] = "display";
            this.disableFocus(index);
        }
    }

    public getPropertyState(index:number){
        let state = this.displayState[index];
        let focus =  this.focus[index];

        let result = [isNull(state)?"display":state,
                      isNull(focus)?"":focus];
        
        return result.join(" ");
    }

    public enableFocus(index:number){
        this.focus[index]="focus";
    }

    public disableFocus(index:number){
        this.focus[index]="";
    }
    /***************************************************************************
    * IMPLEMENTS ControlValueAccessor
    ***************************************************************************/
    writeValue(value: any) {
        if (value !== this.innerValue) {
            this.innerValue = value;
        }

        if(isNull(this.innerValue)){
            this.innerValue = [];
        }
    }
    registerOnChange(fn: any) {}
    registerOnTouched(fn: any) {}
}