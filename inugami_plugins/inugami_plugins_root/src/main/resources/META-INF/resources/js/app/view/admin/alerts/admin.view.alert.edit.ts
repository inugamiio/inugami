import {
    Component, Inject, OnInit, Input,
    Output, forwardRef, EventEmitter
} from '@angular/core';
import {
    NG_VALUE_ACCESSOR, ControlValueAccessor, Validators,
    FormGroup, FormControl, FormArray, FormBuilder
} from '@angular/forms';

import { DomSanitizer } from '@angular/platform-browser';

import { AlertsCrudServices } from './../../../services/http/alerts.crud.services';
import { AlertEntity } from './../../../models/alert.entity';
import { InputBloc } from './../../../components/forms/input.bloc';
import { Msg } from './../../../components/msg/msg';
import { inputTimeSlotsValidator } from './validators/input-time-slots.validator';
import { dynamicLevelsValidator } from './validators/dynamic-levels.validator';
import { HttpServices } from '../../../services/http/http.services';
import { Tag } from '../../../models/tag';
import { DynamicLevel } from '../../../models/dynamic.level';
import { DynamicLevelValues } from '../../../models/dynamic.level.values';
import { TimeSlot } from '../../../models/time.slot';
import { ProviderSource } from '../../../models/provider.source';
import { AlertsDynamicCrudServices } from '../../../services/http/alerts.dynamic.crud.services';


export const ADMIN_VIEW_ALERT_EDIT_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => AdminViewAlertEdit),
    multi: true
};



@Component({
    selector: 'admin-view-alert-edit',
    templateUrl: 'js/app/view/admin/alerts/admin.view.alert.edit.html',
    providers: [ADMIN_VIEW_ALERT_EDIT_VALUE_ACCESSOR],
    directives: [InputBloc, Msg]
})
export class AdminViewAlertEdit implements AfterViewInit {

    /**************************************************************************
    * ATTRIBUTES
    **************************************************************************/
    @Input() styleClass: string;
    @Input() edit: boolean = false;

    private detailData: string;
    private validators: any = io.inugami.validators;
    private innerValue: AlertEntity;
    private innerValueChannels: string[];
    private isNotEdit: boolean;
    private providers : any[];
    private fileUri: any;
    private dynamic: boolean;


    private channels: any[] = [];
    private addedLevel: any = {};

    private alertForm: any;
    private resp: any;
    private formatter: any;

    private labelInverseMin  :string = io.inugami.formatters.message("alert.edit.alert.inverse.min");
    private labelInverseMax  :string = io.inugami.formatters.message("alert.edit.alert.inverse.max");

    private dataProviderPlaceHolder : string = io.inugami.formatters.message("alert.edit.sources.data.provider");

    @Output() onClose: EventEmitter<any> = new EventEmitter();
    @Output() onError: EventEmitter<any> = new EventEmitter();
    @Output() onSuccess: EventEmitter<any> = new EventEmitter();
    @Output() onCleanMessage: EventEmitter<any> = new EventEmitter();

    private cols : any = [
        { field: 'uid', header: 'uid' },
        { field: 'created', header: 'created' },
        { field: 'dynamicAlerting', header: 'dynamicAlerting' },
        { field: 'level', header: 'level' },
        { field: 'label', header: 'label' }
    ];
    /**************************************************************************
    * CONSTRUCTOR
    **************************************************************************/
    constructor(private alertsCrudServices: AlertsDynamicCrudServices, private fb: FormBuilder, private httpService: HttpServices, private sanitizer: DomSanitizer) {
        this.initValue();
    }
    ngAfterContentInit() {
        this.isNotEdit = !this.edit;
    }

    /**************************************************************************
    * INIT
    **************************************************************************/
    private initValue() {
        if (isNull(this.alertForm)) {
            this.alertForm = this.fb.group({
                name: ['', Validators.required],
                duration: [''],
                mainMessage: [''],
                detailedMessage: [''],
                url: [''],
                tag: [''],
                channelsData: this.fb.array([]),
                sources: this.fb.group({
                    dataProvider: [''],
                    interval: [''],
                    from: [''],
                    to: [''],
                    eventName: [''],
                    query: ['']
                }),
                activation: this.fb.array([this.createFormActivationLine()]),
                levelPointsBeforeTriggered: this.fb.array([]),
                dynamicLevels: ['', [dynamicLevelsValidator]],
                scripts: [''],
                inverse: [''],
                addedLevelPoints: [''],
                showedMaxValue:[''],
                showedMinValue:[''],
                dynamic:[''],
            })

            let self = this;
            this.alertForm.controls['dynamic'].valueChanges.subscribe((value)=> {self.dynamic = value})

            this.initChannels();
            this.initProviders();
        } else {
            this.alertForm.controls['levelPointsBeforeTriggered'].controls = [];
            this.alertForm.reset();
        }

        this.alertForm.valueChanges.subscribe(val => {
            let jsonData = JSON.stringify(val);
            this.fileUri = this.sanitizer.bypassSecurityTrustUrl("data:text/json;charset=UTF-8," + encodeURIComponent(jsonData));

        })



        this.applyDefaultValues();

    }

    initChannels() {
        let self = this;
        this.resp = this.httpService.get(window.location.origin+CONTEXT_PATH+"rest/alert/providers");
        this.resp.then(data => self.addChannels(data));
    }

    initProviders(){
        let self = this;
        let resp = this.httpService.get(window.location.origin+CONTEXT_PATH+"rest/provider");
        resp.then(data => self.addProviders(data));
    }

    addChannels(data) {
        let channelsData = this.alertForm.get('channelsData');
        let channelArray = [];
        for (let provider of data) {
            if (isNull(this.channels.find(function (element) {
                return element.name == provider.name;
            }))) {
                this.channels.push({ name: provider.name, enable: provider.enable });
                this.alertForm.get('channelsData').push(this.fb.control(channelArray));
            }
        }
        this.applyAllertProviderOnForm();
        if(isNull(this.alertForm.get('inverse').value)){
            this.alertForm.get('inverse').patchValue("false");
        }
    }

    addProviders(data){
        this.providers = [];
        for(let provider of data){
            let tempProvider = {label:provider,value:provider}
            this.providers.push(tempProvider);
        }
    }
    applyDefaultValues(){
        this.alertForm.get('duration').patchValue(60);
        this.alertForm.get('addedLevelPoints').patchValue(1);
    }
    /**************************************************************************
    * ACTIONS
    **************************************************************************/
    onSuccessSave() {
        let msg = io.inugami.formatters.messageValue("action.success");
        this.onSuccess.emit({ "severity": 'success', "summary": msg });
    }
    cleanMessage() {
        this.onCleanMessage.emit();
    }
    handlerError(error) {
        let errorMsg = null;
        if (isNotNull(error.data) && isNotNull(error.data.errorCode)) {
            errorMsg = io.inugami.formatters.messageValue(["io.inugami.error", error.data.errorCode].join("."))
        }
        if (isNull(errorMsg)) {
            errorMsg = error.data;
        }

        this.onSuccess.emit({ "severity": 'error', "summary": errorMsg });
    }

    /*************************************************************************
    * ACTIONS
    **************************************************************************/


    saveAlert() {
        let alertEntity = this.convertFormToAlert();
        this.cleanMessage();
        let alerts = [alertEntity];
        if (this.edit) {
            this.alertsCrudServices.merge(alerts)
                .then((data) => {
                    this.onSuccessSave();
                })
                .catch((error) => this.handlerError(error));
        } else {
            this.alertsCrudServices.save(alerts)
                .then((data) => {
                    this.onSuccessSave();
                })
                .catch((error) => this.handlerError(error));
        }
    }


    saveAlertAndClose() {
        this.saveAlert();
        this.initValue();
    }

    close() {
        this.initValue();
        this.edit = false;
        this.onClose.emit();
    }

    removeActivationLine(index) {
        this.removeFormActivationLine(index);
    }


    addDynamicLevelsLevel(graph,levelName) {
        if (isNotNull(this.addedLevel.name) &&
            this.addedLevel.name != "" &&
            isNull(io.inugami.validators.notNegativeNumber(this.addedLevel.pointsBeforeTriggered))) {

                    if (contains(this.addedLevel, this.alertForm.get('levelPointsBeforeTriggered').controls, function (a, b) {
                        return a.value.name === b.name;
                    })) {
                        alert(io.inugami.formatters.message("alert.edit.level.already.added"));
                    } else {
                        this.addFormLevelLine();
                        graph.addNewData(this.addedLevel.name);
                        levelName.innerValue = null;
                    }
        }
    }

    removeDynamicLevelsLevel(i, graph) {
        let array = this.alertForm.get('levelPointsBeforeTriggered');
        let name = array.at(i).value.name;
        array.removeAt(i);

        graph.deleteLine(name)
    }


    fileChanged(event) {
        let file = event.target.files[0];
        let reader = new FileReader();
        let text;
        reader.onload = (e) => {
            text = reader.result;
            let jsonData = JSON.parse(text);
            jsonData = this.patchChannelsData(jsonData);
            this.setFormGroupForFile(jsonData);
            this.alertForm.patchValue(jsonData);
            event.target.value = null;
        }
        reader.readAsText(file);
    }

    setFormGroupForFile(jsonData) {
        this.removeAllFormLevelLine();
        this.removeAllFormActivationLine();
        if (isNotNull(jsonData.levelPointsBeforeTriggered)) {
            for (let i = 0; i < jsonData.levelPointsBeforeTriggered.length; i++) {
                this.addFormLevelLine();
            }
        }
        if (isNotNull(jsonData.activation)) {
            for (let i = 0; i < jsonData.activation.length; i++) {
                this.addFormActivationLine();
            }
        }
    }

    patchChannelsData(jsonData) {
        let channelsData = jsonData.channelsData;
        let self = this;
        for (let channelTab of channelsData) {
            if (isNotNull(channelTab)) {
                let index = this.channels.findIndex(function (element) {
                    return element.name == channelTab[0];
                })
                if (index != -1) {
                    self.alertForm.get("channelsData").at(index).patchValue([self.channels[index].name]);
                }
            }
        }
        delete jsonData.channelsData;
        return jsonData;
    }
    /*****************************************************************************
     * IMPLEMENTS ControlValueAccessor
     *****************************************************************************/
    writeValue(value: any) {
        this.alertForm.reset();
        if (isNotNull(value)) {
            if (isNotNull(value.uid)) {
                this.edit = true;
                this.isNotEdit = !this.edit;
            } else {
                this.edit = false;
                this.isNotEdit = !this.edit;
            }
            this.convertAlertToForm(value)
        } else {
            this.edit = false;
            this.isNotEdit = !this.edit;
            this.applyDefaultValues();
        }
    }

    registerOnChange(fn: any) {
        this.onChangeCallback = fn;
    }

    registerOnTouched(fn: any) {
        this.onTouchedCallback = fn;
    }

    applyAllertProviderOnForm() {
        if (isNotNull(this.innerValueChannels) && isNotNull(this.channels)) {
            for (let provider of this.innerValueChannels) {
                for (let i = 0; i < this.channels.length; i++) {
                    if (provider == this.channels[i].name) {
                        this.alertForm.get('channelsData').at(i).patchValue([provider]);
                    }
                }
            }
        }
    }

    /*****************************************************************************
    * FORMS TOOLS METHOD
    *****************************************************************************/
    get channelsData() {
        return this.alertForm.get('channelsData') as FormArray;
    }

    createFormActivationLine(): FormGroup {
        return this.fb.group({
            days: [''],
            hours: ['', [Validators.required, inputTimeSlotsValidator()]],
        })
    }
    addFormActivationLine() {
        this.alertForm.get('activation').push(this.createFormActivationLine());

    }

    removeFormActivationLine(index) {
        this.alertForm.get('activation').removeAt(index);
    }
    createFormLevelLine() {
        return this.fb.group({
            points: '',
            name: ''
        })
    }

    addFormLevelLine() {
        let array = this.alertForm.get('levelPointsBeforeTriggered');
        let group = this.createFormLevelLine()
        array.push(group);
        group.patchValue({ name: this.addedLevel.name, points: this.addedLevel.pointsBeforeTriggered });
    }

    removeFormLevelLine(index) {
        this.alertForm.get('levelPointsBeforeTriggered').removeAt(index);
    }

    setLevelName(event) {
        this.addedLevel.name = event;
    }

    setPointsBeforeTriggered(event) {
        this.addedLevel.pointsBeforeTriggered = event;
    }


    setGraphMinValue(event, graph) {
        graph.setMinValue(parseFloat(event.target.value));
    }

    setGraphMaxValue(event, graph) {
        graph.setMaxValue(parseFloat(event.target.value));
    }

    setFormatter(event) {
        let precision = parseFloat(event.target.value);
        if (precision > 0) {
            precision = Math.pow(10, precision);
            this.formatter = (value) => Math.round(value * precision + Number.EPSILON) / precision;
        }
    }

    removeAllFormLevelLine() {
        let length = this.alertForm.get('levelPointsBeforeTriggered').length
        for (let i = length - 1; i >= 0; i--) {
            this.removeFormLevelLine(i);
        }
    }

    removeAllFormActivationLine() {
        let length = this.alertForm.get('activation').length;
        for (let i = length - 1; i >= 0; i--) {
            this.removeFormActivationLine(i);
        }
    }

    toggleFormTooltip(tooltip) {
        if (tooltip.classList.contains('hidden')) {
            tooltip.classList.remove('hidden');
        } else {
            tooltip.classList.add('hidden');
        }
    }

    /*****************************************************************************
    * ALERT <--> FORMS MAPPER
    *****************************************************************************/

    convertFormToAlert() {
        let alert = new AlertEntity();
        let form = this.alertForm.value;
        alert.alerteName = form.name;
        alert.label = form.mainMessage;
        alert.subLabel = form.detailedMessage;
        alert.url = form.url;
        alert.duration = form.duration;
        alert.script = form.scripts;
        alert.level = "info";
        alert.dynamic = form.dynamic;

        if(isNotNull(form.dynamicLevels)){
            alert.levels = form.dynamicLevels.data;
            alert.maxValue = form.dynamicLevels.maxValue;
            alert.minValue = form.dynamicLevels.minValue;
        }
        alert.inverse = form.inverse == "true" ? true : false;

        if (isNotNull(form.dynamicLevels) && isNotNull(form.levelPointsBeforeTriggered)) {
            for (let level of form.levelPointsBeforeTriggered) {
                let levelValue = alert.levels.find(element => { return element.name == level.name });
                levelValue.activationDelais = level.points;
            }
        }

        if (isNotNull(form.sources)) {
            alert.source = new ProviderSource(
                form.sources.dataProvider,
                form.sources.interval,
                form.sources.from,
                form.sources.to,
                form.sources.eventName,
                form.sources.query
            )
        }
        if (isNotNull(form.tag)) {
            alert.tags = [];
            for (let tagname of form.tag) {
                let tag = new Tag(tagname)
                alert.tags.push(tag);
            }
        }

        alert.providers = [];
        for (let provider of form.channelsData) {
            if (isNotNull(provider)) {
                alert.providers.push(provider[0]);
            }
        }

        alert.activations = form.activation;

        return alert;
    }

    convertAlertToForm(value) {
        this.innerValueChannels = value.providers;
        this.initChannels();
        this.alertForm.get('name').patchValue(value.alerteName);
        this.alertForm.get('duration').patchValue(value.duration);
        this.alertForm.get('mainMessage').patchValue(value.label);
        this.alertForm.get('detailedMessage').patchValue(value.subLabel);
        this.alertForm.get('url').patchValue(value.url);
        this.alertForm.get('scripts').patchValue(value.script);
        let inverse = value.inverse ? "true" : "false";
        this.alertForm.get('inverse').patchValue(inverse);
        this.alertForm.get('dynamic').patchValue(value.dynamic,{emitEvent:true});
        


        if (isNotNull(value.source)) {
            this.alertForm.get('sources').get('interval').patchValue(value.source.cronExpression);
            this.alertForm.get('sources').get('dataProvider').patchValue(value.source.provider);
            this.alertForm.get('sources').get('from').patchValue(value.source.from);
            this.alertForm.get('sources').get('to').patchValue(value.source.to);
            this.alertForm.get('sources').get('eventName').patchValue(value.source.eventName);
            this.alertForm.get('sources').get('query').patchValue(value.source.query);
        }
        if (isNotNull(value.tags)) {
            let tags = [];
            for (let tag of value.tags) {
                tags.push(tag.name);
            }
            this.alertForm.get('tag').patchValue(tags);
        }

        if (isNotNull(value.activations)) {
            let activations = [];
            for (let activationTime of value.activations) {
                this.addFormActivationLine();
                let activation = { days: '', hours: '' };
                activation.days = activationTime.days;
                activation.hours = activationTime.hours;
                activations.push(activation);
            }
            this.alertForm.get('activation').patchValue(activations);
        }

        if (isNotNull(value.levels)) {
            let levels = [];
            let array = this.alertForm.get('levelPointsBeforeTriggered');
            for (let levelValue of value.levels) {
                let group = this.createFormLevelLine()
                array.push(group);
                let level = { points: '', name: '' };
                level.points = levelValue.activationDelais;
                level.name = levelValue.name;
                levels.push(level);
            }
            array.patchValue(levels);

            let dynamicLevels = {'minValue':value.minValue,'maxValue':value.maxValue,'data':value.levels};     

            this.alertForm.get('dynamicLevels').patchValue(dynamicLevels);
            this.alertForm.get('showedMinValue').patchValue(value.minValue);
            this.alertForm.get('showedMaxValue').patchValue(value.maxValue);
        }


        this.innerValueChannels = value.providers;
        this.initChannels();
    }
}