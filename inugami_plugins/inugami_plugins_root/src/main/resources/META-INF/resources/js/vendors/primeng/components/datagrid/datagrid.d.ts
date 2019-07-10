import { ElementRef, AfterViewInit, AfterContentInit, EventEmitter, QueryList, TemplateRef } from '@angular/core';
import { BlockableUI } from '../common/api';
export declare class DataGrid implements AfterViewInit, AfterContentInit, BlockableUI {
    el: ElementRef;
    paginator: boolean;
    rows: number;
    totalRecords: number;
    pageLinks: number;
    rowsPerPageOptions: number[];
    lazy: boolean;
    emptyMessage: string;
    onLazyLoad: EventEmitter<any>;
    style: any;
    styleClass: string;
    paginatorPosition: string;
    alwaysShowPaginator: boolean;
    trackBy: Function;
    onPage: EventEmitter<any>;
    header: any;
    footer: any;
    templates: QueryList<any>;
    _value: any[];
    itemTemplate: TemplateRef<any>;
    dataToRender: any[];
    first: number;
    page: number;
    constructor(el: ElementRef);
    ngAfterViewInit(): void;
    ngAfterContentInit(): void;
    value: any[];
    handleDataChange(): void;
    updatePaginator(): void;
    paginate(event: any): void;
    updateDataToRender(datasource: any): void;
    isEmpty(): boolean;
    createLazyLoadMetadata(): any;
    getBlockableElement(): HTMLElement;
}
export declare class DataGridModule {
}
