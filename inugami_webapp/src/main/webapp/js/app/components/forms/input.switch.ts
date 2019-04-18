import { Component, OnInit, forwardRef, Input, Output, EventEmitter, ChangeDetectorRef } from '@angular/core';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';

export const INPUTSWITCH_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => InputSwitch),
    multi: true
}; 

@Component({
    selector: 'input-switch',
    providers: [INPUTSWITCH_VALUE_ACCESSOR],
    template: `
    <div [ngClass]="getBaseStyleClass()" 
    [ngStyle]="style" [class]="styleClass" (click)="onClick($event, cb)" role="checkbox" [attr.aria-checked]="checked">
        <div class="ui-helper-hidden-accessible">
        <input #cb type="checkbox" [attr.id]="inputId" [attr.name]="name" [attr.tabindex]="tabindex" [checked]="checked" (change)="onInputChange($event)"
                (focus)="onCompoFocused($event)" (blur)="onBlur($event)" [disabled]="disabled" />
        </div>
        <span class="background inugami-visible-switch ">
            <span class="slider">
                <span class="icon"></span>
            </span>
        </span>
        <span [ngClass]="{'switch-label': true}" [class.show]="displayLabel()">{{displayedLabel}}</span>
    </div>
  `
})
export class InputSwitch implements OnInit, ControlValueAccessor {

    /*****************************************************************************
    * ATTRIBUTES
    *****************************************************************************/
    @Input() style                  : any;

    @Input() styleClass             : string;

    @Input() tabindex               : number;

    @Input() inputId                : string;

    @Input() name                   : string;

    @Input() disabled               : boolean           = false;

    @Input() readonly               : boolean           = false;

    @Input() labelOn                : string;

    @Input() labelOff               : string;

    @Input() validator              : any;

    @Output() onFocus               : EventEmitter<any> = new EventEmitter();

    @Output() onChange              : EventEmitter<any> = new EventEmitter();

    onModelChange                   : Function          = () => { };

    onModelTouched                  : Function          = () => { };

    displayedLabel                  : string;

    styleClasses                    : string[];

    focused                         : boolean;

    checked                         : boolean           = false;

    valid                           : boolean           = true;

    enableValidator                 : boolean           = false;

    /*****************************************************************************
    * INIT
    *****************************************************************************/

    ngOnInit() {
        this.checked = true;
        this.styleClasses = []
        this.displayedLabel = this.labelOn;
    }
    constructor(private cd: ChangeDetectorRef) { }

    /****************** ***********************************************************
     * IMPLEMENTS ControlValueAccessor
     *****************************************************************************/


    writeValue(checked: any): void {
        this.checked = checked;
        this.displayedLabel = checked ? this.labelOn : this.labelOff;
        this.cd.markForCheck();
    }

    registerOnChange(fn: Function): void {
        this.onModelChange = fn;
    }

    registerOnTouched(fn: Function): void {
        this.onModelTouched = fn;
    }
    setDisabledState(val: boolean): void {
        this.disabled = val;
    }

    /*****************************************************************************
    * METHODS
    *****************************************************************************/


    toggle(event: Event) {
        this.updateModel(event, !this.checked);
    }

    updateModel(event: Event, value: boolean) {
        this.checked = value;
        this.processValidator();
        this.displayedLabel = value ? this.labelOn : this.labelOff;
        this.onModelChange(this.checked);
        this.onChange.emit({
            originalEvent: event,
            checked: this.checked
        });
    }

    getBaseStyleClass(){
        let result= ["inugami-input-switch-button"];
        if(!this.valid){
            result.push("validation-error");
        }
        if(this.checked){
            result.push("state-on");
        }else{
            result.push("state-off");
        }
        return result.join(" ");
    }
    /*****************************************************************************
     * HANDLERS
     *****************************************************************************/
    onCompoFocused($event) {
        if (this.focused === false) {
            this.onFocus.emit({
                originalEvent: event
            })
        }
        this.focused = true;
    }

    onClick(event: Event, cb: HTMLInputElement) {
        if (!this.disabled && !this.readonly) {
            this.toggle(event)
            cb.focus()
        }
    }

    onBlur($event) {
        this.focused = false;
    }

    displayLabel() {
        return this.isNotNull(this.displayLabel) && ("" + this.displayLabel).length > 0;
    }

    /*****************************************************************************
      * VALIDATOR
      *****************************************************************************/
    private processValidator() {
        if (this.enableValidator && this.isNotNull(this.validator)) {
            let error = this.validator(this.checked);
            if (this.isNotNull(error)) {
                this.valid = false;
            } else {
                this.valid = true;
            }
        }
    }
    /*****************************************************************************
      TOOLS
      *****************************************************************************/
    isNotNull(value) {
        return (value != undefined && value != null)
    }
}
