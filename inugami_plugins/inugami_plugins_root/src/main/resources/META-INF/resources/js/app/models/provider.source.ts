export class ProviderSource{
    constructor(
        public provider             : string,
        public cronExpression       : string,
        public from                 : string,
        public to                   : string,
        public eventName            : string,
        public query                : string
    ){}
}