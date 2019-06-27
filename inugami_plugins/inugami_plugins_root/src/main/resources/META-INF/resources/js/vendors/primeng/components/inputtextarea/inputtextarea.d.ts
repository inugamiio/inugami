import { ElementRef, OnInit, DoCheck } from '@angular/core';
export declare class InputTextarea implements OnInit, DoCheck {
    el: ElementRef;
    autoResize: boolean;
    rows: number;
    cols: number;
    rowsDefault: number;
    colsDefault: number;
    filled: boolean;
    constructor(el: ElementRef);
    ngOnInit(): void;
    ngDoCheck(): void;
    onInput(e: any): void;
    updateFilledState(): void;
    onFocus(e: any): void;
    onBlur(e: any): void;
    onKeyup(e: any): void;
    resize(): void;
}
export declare class InputTextareaModule {
}
