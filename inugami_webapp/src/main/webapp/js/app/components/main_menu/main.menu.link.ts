export class MainMenuLink {
    constructor(
        public title       : string,
        public path        : string,
        public styleClass  : string,
        public rooterLink  : boolean = true,
        public role?       : string
    ){ }
}

