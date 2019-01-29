import { MenuItem } from '../common/api';
export declare class BasePanelMenuItem {
    handleClick(event: any, item: any): void;
}
export declare class PanelMenuSub extends BasePanelMenuItem {
    item: MenuItem;
    expanded: boolean;
}
export declare class PanelMenu extends BasePanelMenuItem {
    model: MenuItem[];
    style: any;
    styleClass: string;
    animating: boolean;
    unsubscribe(item: any): void;
    ngOnDestroy(): void;
    handleClick(event: any, item: any): void;
}
export declare class PanelMenuModule {
}
