import {Component,Input,forwardRef}                     from '@angular/core';
import {NG_VALUE_ACCESSOR,ControlValueAccessor}         from '@angular/forms';


export const CAROUSEL_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => Carousel),
    multi: true
};
    

@Component({
selector      : 'carousel',
template      : `<div [ngClass]="'carousel'" [class]="styleClass" [style.width.px]="width">
                    <ul class="carousel-images">
                        <li *ngFor="let img of imagesToDisplay; let i = index"
                            [ngClass]="'carousel-item-image'"
                            [class.display]="isSelected(i)">
                            <img [src]="img.source" [title]="img.title" [alt]="img.alt"/>
                        </li>
                    </ul>
                    <ul class="carousel-controls" *ngIf="isMultiImages" [style.marginLeft.px]="marginLeft">
                        <li *ngFor="let img of imagesToDisplay; let i = index" 
                            [class.display]="isSelected(i)"
                            (click)="selectImage(i)"
                            [ngClass]="'carousel-item-selector'">
                            <span class="carousel-item-selector-ctrl"></span>
                        </li>
                        <div class="carousel-controls-end"></div>
                    </ul>
                </div>
                `,
directives    : [ ],
providers     : [CAROUSEL_VALUE_ACCESSOR]
})
export class Carousel implements  ControlValueAccessor{

    /***************************************************************************
     * ATTRIBUTES
     **************************************************************************/
    @Input() styleClass                             : string;
    @Input() width                                  : number = 512;
    @Input() height                                 : number;
    @Input() duration                               : number = 5000;

    private marginLeft                              : number  = 0;
    private ctrlItemSize                            : number  = 10;
    private imagesToDisplay                         : any[]   = [];
    private imageSelected                           : number  = 0;
    private isMultiImages                           : boolean = false;

    /***************************************************************************
     * CONSTRUCTOR
    ***************************************************************************/
    constructor() {
        let self = this;
        setInterval(function(){self.changeImage()}, this.duration);
    }

    /***************************************************************************
    * ACTIONS
    ***************************************************************************/
    private changeImage(){
        if(this.imagesToDisplay.length>0){
            this.imageSelected++;
            if(this.imageSelected >= this.imagesToDisplay.length){
                this.imageSelected = 0;
            }
        }
    }

    public selectImage(index:number){
        this.imageSelected = index;
    }

    /***************************************************************************
    * GETTERS
    ***************************************************************************/
    public isSelected(i:number){
        return i == this.imageSelected;
    }

    /*****************************************************************************
     * IMPLEMENTS ControlValueAccessor
     *****************************************************************************/
    writeValue(value: any[]) {
        this.imagesToDisplay = [];
        if(isNotNull(value)){
            for(let img of value){
                this.imagesToDisplay.push({
                    "source": CONTEXT_PATH + img.source,
                    "title": isNull(img.title)?"":img.title,
                    "alt": isNull(img.alt)?"":img.alt,
                });
            }

            this.isMultiImages = this.imagesToDisplay.length>1;
            if(this.isMultiImages){
                let ctrlsSize =  this.imagesToDisplay.length*this.ctrlItemSize;
                this.marginLeft    = (this.width/this.imagesToDisplay.length)-(ctrlsSize/2);
            }
            
        }
    }

    registerOnChange(fn: any) {
        this.onChangeCallback = fn;
    }
    registerOnTouched(fn: any) {
        this.onTouchedCallback = fn;
    }
}