import { ElementRef, AfterViewInit, OnDestroy, EventEmitter } from '@angular/core';
import { Message } from '../common/api';
import { DomHandler } from '../dom/domhandler';
export declare class Growl implements AfterViewInit, OnDestroy {
    el: ElementRef;
    domHandler: DomHandler;
    sticky: boolean;
    life: number;
    style: any;
    styleClass: string;
    onClose: EventEmitter<any>;
    valueChange: EventEmitter<Message[]>;
    containerViewChild: ElementRef;
    _value: Message[];
    zIndex: number;
    container: HTMLDivElement;
    timeout: any;
    preventRerender: boolean;
    constructor(el: ElementRef, domHandler: DomHandler);
    ngAfterViewInit(): void;
    value: Message[];
    handleValueChange(): void;
    remove(index: number, msgel: any): void;
    removeAll(): void;
    ngOnDestroy(): void;
}
export declare class GrowlModule {
}
