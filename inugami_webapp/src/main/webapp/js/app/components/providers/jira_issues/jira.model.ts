export class JiraModel {
    constructor(
        public id               : string,
        public title            : string,
        public status           : string,
        public statusKeyword    : string,
        public assignee         : string,
        public reporter         : string,
        public created          : number,
        public update           : number,
        public affectedVersion  : string[],
        public customFields     : string[]
    ){ }
}