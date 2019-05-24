import {Component}                          from '@angular/core';
import {ActivatedRoute,Router}              from '@angular/router';
import {SessionScope}                       from './../../scopes/session.scope';
import {MainMenuService,
        MAIN_MENU_UPDATE,
        MAIN_MENU_ON_CLICK}                 from './main.menu.service';
import {MainMenuLink}                       from './main.menu.link';
import {SecurityServices}                   from './../../services/security.services';

@Component({
  selector      : 'main-menu',
  templateUrl   : 'js/app/components/main_menu/main.menu.html',
  directives    : [ ],
})
export class MainMenu{

    /**************************************************************************
    * ATTRIBUTES
    **************************************************************************/
    private links        : MainMenuLink[]= [];
    private currentTitle : string;
    /**************************************************************************
    * CONSTRUCTOR
    **************************************************************************/
    constructor(private route           : ActivatedRoute,
                private router           : Router,
                private sessionScope     : SessionScope,
                private mainMenuService  : MainMenuService,
                private securityServices : SecurityServices) {

        router.events.subscribe((val) => {
            this.updateMainMenu();
        }); 
        
        let self = this;
        org.inugami.events.addEventListener(MAIN_MENU_UPDATE, function(data){
            self.updateMainMenu();
        });
    }

    /**************************************************************************
    * ACTIONS
    **************************************************************************/
    private updateMainMenu(){
        this.currentTitle = this.mainMenuService.getCurrentTitle();
        this.links = this.mainMenuService.getLink();
    }
    
    private hasRole(link : MainMenuLink){
        let result = true;

        if(isNotNull(link.role)){
            result = this.sessionScope.hasRole(link.role);
        }
        return result;
    }

    public routeToVirtualLink(subLink:MainMenuLink){
        for(let link of this.links){
            link.selected = false;
        }
        subLink.selected = true;
        org.inugami.events.fireEvent(MAIN_MENU_ON_CLICK,subLink);
    }

    public logout(){
        this.securityServices.logout();
      }
}
