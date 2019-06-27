import { OnDestroy } from '@angular/core';
import { MenuItem } from '../common/api';
export declare class TabMenu implements OnDestroy {
    model: MenuItem[];
    activeItem: MenuItem;
    popup: boolean;
    style: any;
    styleClass: string;
    ngOnInit(): void;
    itemClick(event: Event, item: MenuItem): void;
    ngOnDestroy(): void;
    unsubscribe(item: any): void;
}
export declare class TabMenuModule {
}
