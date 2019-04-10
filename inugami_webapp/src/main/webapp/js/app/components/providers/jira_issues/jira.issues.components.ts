// ANGULAR CORE MODULES --------------------------------------------------------
import {Component, Input, OnInit, AfterContentInit}     from '@angular/core';

@Component({
    selector    : 'jira-issues',
    templateUrl : 'js/app/components/providers/jira_issues/jira.issues.components.html'
})
export class JiraIssuesComponent implements OnInit, AfterContentInit{
    /**************************************************************************
    * ATTRIBUTES
    **************************************************************************/
    @Input() styleClass                     : string;
    @Input() event                          : string;
    @Input() pluginName                     : string;
    @Input() transformer                    : any;
    private issuesList                      : any[];

    /**************************************************************************
    * CONSTRUCTORS
    **************************************************************************/
    ngAfterContentInit() {
        if (isNotNull(this.event)) {
            org.inugami.events.addEventListenerByPlugin(this.pluginName, this.event, (event) => this.injectData(event));
        }
    }

    /**************************************************************************
    * METHODS
    **************************************************************************/

    private injectData(event){
        if (isNotNull(event.detail.data) && isNotNull(event.detail.data.values)) {
            if(isNotNull(this.transformer)){
                this.issuesList = this.transformer(event.detail.data.values);
            }else{
                throw "jira transformer is mandatory";
            }
        }
    }
}