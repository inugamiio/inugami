import { OnDestroy } from '@angular/core';
import { MenuItem } from '../common/api';
export declare class Breadcrumb implements OnDestroy {
    model: MenuItem[];
    style: any;
    styleClass: string;
    home: MenuItem;
    itemClick(event: any, item: MenuItem): void;
    onHomeClick(event: any): void;
    ngOnDestroy(): void;
}
export declare class BreadcrumbModule {
}
