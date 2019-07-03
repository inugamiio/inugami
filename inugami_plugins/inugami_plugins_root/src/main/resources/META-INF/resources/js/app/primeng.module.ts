import {NgModule} from '@angular/core';
import {ButtonModule} from 'primeng/button';
import {GrowlModule} from 'primeng/growl';
import {TableModule}  from 'primeng/table';
import {CheckboxModule} from 'primeng/checkbox';

@NgModule({
    imports : [
        ButtonModule,
        GrowlModule,
        TableModule,
        CheckboxModule
    ],
    exports : [
        ButtonModule,
        GrowlModule,
        TableModule,
        CheckboxModule
    ]
})
export class PrimeNgModule {
}