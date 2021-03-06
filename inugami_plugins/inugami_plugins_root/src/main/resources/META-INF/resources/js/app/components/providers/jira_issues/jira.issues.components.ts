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
    @Input() baseUrl                        : string;
    private issuesList                      : any[];

    /**************************************************************************
    * CONSTRUCTORS
    **************************************************************************/
    ngAfterContentInit() {
        if (isNotNull(this.event)) {
            io.inugami.events.addEventListenerByPlugin(this.pluginName, this.event, (event) => this.injectData(event));
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

    public buildJiraUrl(issue){
        let result = "#";
        if(isNotNull(this.baseUrl) && isNotNull(issue.id)){
            let buffer = [];
            if(this.baseUrl.endsWith('/')){
                buffer.push(this.baseUrl);
            }else{
                buffer.push(this.baseUrl);
                buffer.push('/');
            }
            buffer.push(issue.id);
            result = buffer.join('');
        }
        return result;
    }
}