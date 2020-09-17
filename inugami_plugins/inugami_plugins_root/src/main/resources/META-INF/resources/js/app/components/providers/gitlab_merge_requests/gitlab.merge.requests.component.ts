// ANGULAR CORE MODULES --------------------------------------------------------
import {Component, Input, AfterContentInit}     from '@angular/core';

@Component({
    selector: 'gitlab-merge-requests',
    templateUrl: "js/app/components/providers/gitlab_merge_requests/gitlab.merge.requests.component.html"
})
export class GitlabMergeRequestsComponent implements AfterContentInit {
    /**************************************************************************
    * ATTRIBUTES
    **************************************************************************/
    @Input() styleClass                     : string;
    @Input() event                          : string;
    @Input() pluginName                     : string;

    private data                            : any[];

    /**************************************************************************
    * CONSTRUCTORS
    **************************************************************************/
    ngAfterContentInit() {
        if (isNotNull(this.event)) {
            io.inugami.events.addEventListenerByPlugin(this.pluginName, this.event, (event) => this.injectData(event));
        }
    }

    private injectData(event){
        if (isNotNull(event.detail.data) && isNotNull(event.detail.data.values)) {
            this.data = event.detail.data.values.mergeRequests;            
        }
    }

    public formatDate(date:string){
        let result = "";
        if(isNotNull(date)){
            result = date.split('T')[0];
        }
        return result;
    }
}