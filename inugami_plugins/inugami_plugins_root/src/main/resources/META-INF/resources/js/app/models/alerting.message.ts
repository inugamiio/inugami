import {AlertingTag} from './alerting.message';
export class AlertingMessage {
    constructor(
        public id               : string,
        public level            : string,
        public label            : string,
        public labelBuilder     : any,
        public subLabel         : string,
        public subLabelBuilder  : any,
        public created          : number = new Date().getTime(),
        public duration         : number = 60,
        public tags             : AlertingTag[],
        public multiAlerts      : boolean
    ){ }
}

