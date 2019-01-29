import { ElementRef, OnInit, OnDestroy, EventEmitter, Renderer2, ChangeDetectorRef } from '@angular/core';
import { DomHandler } from '../dom/domhandler';
import { MenuItem } from '../common/api';
import { Router } from '@angular/router';
export declare class SplitButton implements OnInit, OnDestroy {
    el: ElementRef;
    domHandler: DomHandler;
    renderer: Renderer2;
    router: Router;
    cd: ChangeDetectorRef;
    model: MenuItem[];
    icon: string;
    iconPos: string;
    label: string;
    onClick: EventEmitter<any>;
    onDropdownClick: EventEmitter<any>;
    style: any;
    styleClass: string;
    menuStyle: any;
    menuStyleClass: string;
    disabled: boolean;
    tabindex: number;
    menuVisible: boolean;
    documentClickListener: any;
    constructor(el: ElementRef, domHandler: DomHandler, renderer: Renderer2, router: Router, cd: ChangeDetectorRef);
    ngOnInit(): void;
    onDefaultButtonClick(event: Event): void;
    itemClick(event: Event, item: MenuItem): void;
    onDropdownButtonClick(event: Event, menu: HTMLDivElement, container: Element): void;
    ngOnDestroy(): void;
}
export declare class SplitButtonModule {
}
