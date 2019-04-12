// ANGULAR CORE MODULES --------------------------------------------------------
import {Component, Input, AfterContentInit}     from '@angular/core';

@Component({
    selector: 'jenkins-jobs',
    templateUrl: "js/app/components/providers/jenkins_jobs/jenkins.jobs.component.html"
})
export class JenkinsJobsComponent implements AfterContentInit {
    /**************************************************************************
    * ATTRIBUTES
    **************************************************************************/
    @Input() styleClass                     : string;
    @Input() event                          : string;
    @Input() pluginName                     : string;
    @Input() jobsLimit                      : number = 18;

    private jobsData                        : any;
    private color                           : any;
    private eventEmptyMessage               : string;
    private dataMessage                     : string = org.inugami.formatters.message("jenkins.jobs.received.data");
    private receivedData                    : boolean = false;

    /**************************************************************************
    * CONSTRUCTORS
    **************************************************************************/
    ngAfterContentInit() {
        // EVENT LISTENER
        if (isNotNull(this.event)) {
            org.inugami.events.addEventListenerByPlugin(this.pluginName, this.event, (event) => this.injectData(event));
        }
    }

    private injectData(event){
        if (isNotNull(event.detail.data) && isNotNull(event.detail.data.values)) {
            this.receivedData = true;            
            this.jobsData = event.detail.data.values.jobs;
            if (this.jobsData === null || this.jobsData.length === 0) {
                this.jobsData = [];
                this.setEventEmptyMessage(event.detail.name);
            } else {
                this.jobsData.sort(this.compare);
            }
        }
    }

    private compare(name1: String, name2: String) {
        let result = 0;
        if (name1.name < name2.name) {
            result = -1;
        }
        if (name1.name > name2.name) {
            result = 1;
        }
        return result;
    }

    private setEventEmptyMessage(eventName: String) {
        if (eventName === "jenkins-jobs-failed-job") {
            this.eventEmptyMessage = org.inugami.formatters.message("jenkins.jobs.failed.none");
        }
        if (eventName === "jenkins-jobs-failed-and-running-jobs") {
            this.eventEmptyMessage = org.inugami.formatters.message("jenkins.jobs.failed.and.running.none");
        }
        if (eventName === "jenkins-jobs-filter-jobs") {
            this.eventEmptyMessage = org.inugami.formatters.message("jenkins.jobs.filter.none");
        }
    }
}