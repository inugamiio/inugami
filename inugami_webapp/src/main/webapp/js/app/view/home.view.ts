import {Component,OnInit,OnDestroy}                           from '@angular/core';
import {ActivatedRoute,Router,NavigationStart,NavigationEnd}  from '@angular/router';
import {PluginsService}                                       from './../services/plugins.service';
import {MenuLink}                                             from './../models/menu.link';
import {SimpleValue}                                          from './../components/charts/simple_value/simple.value';
import {SessionScope}                                         from './../scopes/session.scope';

@Component({
    templateUrl: 'js/app/view/home.view.html',
    directives : [SimpleValue]
})
export class HomeView implements OnInit, OnDestroy{

  /**************************************************************************
  * ATTRIBUTES
  **************************************************************************/
  private showLink      : boolean = false;
  private pluginsLinks  : MenuLink[];
  
  /**************************************************************************
  * CONSTRUCTOR
  **************************************************************************/
  constructor(private route: ActivatedRoute,
              private pluginsService:PluginsService,
              private router: Router,
              private sessionScope : SessionScope){
      this.showLinks = true;
  }


  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      if(!this.sessionScope.isConnected()){
        this.router.navigate(['/login']);
      }else{
        this.sessionScope.openMainMenu();
        this.grabAllMenuLinks();
      }
    });
  }

  /**************************************************************************
  * INITIALIZE
  **************************************************************************/

  grabAllMenuLinks(){
      this.pluginsService.getAllMenuLinks().then(data =>{
        this.initPluginsLinks(data);
      });
  }
  initPluginsLinks(data){
        this.pluginsLinks = data;
        this.showLink=true;
  }

}
