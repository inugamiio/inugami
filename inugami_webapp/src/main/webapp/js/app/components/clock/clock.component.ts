import {Component, Input, OnInit, AfterContentInit}     from '@angular/core';

@Component({
    selector: 'clock',
    template: `
        <div [class]="styleClass" [ngClass]="'clock'">
            <div class="time">{{time}}</div>
        </div>
    `
})
export class ClockComponent implements OnInit, AfterContentInit{
    /**************************************************************************
    * ATTRIBUTES
    **************************************************************************/
    private time                        : string;
    @Input() styleClass                 : string;
    @Input() timeFormat                 : string;            

    /**************************************************************************
    * CONSTRUCTORS
    **************************************************************************/
    ngAfterContentInit() {
        this.updateTime();
    }

    ngOnInit() {
        let self = this;
        window.setInterval(function() {
            self.updateTime();
        }, 1000 * 60);
    }

    /**************************************************************************
    * TIME HANDLER
    **************************************************************************/
    updateTime() {
        let timeStamp = Math.round(Date.now()/1000);
        this.time = org.inugami.formatters.timestampToTimeFormat(timeStamp.toFixed(0), this.timeFormat);
    }
}