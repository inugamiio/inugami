export class User {
    constructor(
        public login              : string,
        public firstName          : string ,
        public lastName           : string,
        public roles              : string[],
        public groups             : string[],
        public level              : number
    ){ }
}
