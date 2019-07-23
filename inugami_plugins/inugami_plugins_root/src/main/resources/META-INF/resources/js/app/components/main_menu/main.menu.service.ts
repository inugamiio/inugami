import {Injectable}                              from '@angular/core';
import {MainMenuLink}             from './main.menu.link';

export const MAIN_MENU_UPDATE   : string = "main_menu_update";
export const MAIN_MENU_ON_CLICK : string = "main_menu_on_click";

@Injectable()
export class MainMenuService {
    /**************************************************************************
    * ATTRIBUTES
    **************************************************************************/
    private links         : MainMenuLink[]= [];
    
    private currentTitle : string;

    /**************************************************************************
    * API
    **************************************************************************/
    public setCurrentTitle(title:string){
        this.currentTitle = title;
    }
    public getCurrentTitle(){
        return isNull(this.currentTitle)?"Inugami" : this.currentTitle;
    }

    /**************************************************************************
    * LINKS
    **************************************************************************/
    public addSubLink(menuLink:MainMenuLink){
        if(isNotNull(menuLink)){
            this.links.push(menuLink);
        }
    }

    public getLink(){
        return this.links;
    }

    public cleanLinks(){
        this.links.splice(0,this.links.length);
    }

    public updateMenu(){
        org.inugami.events.fireEvent(MAIN_MENU_UPDATE);
    }
}