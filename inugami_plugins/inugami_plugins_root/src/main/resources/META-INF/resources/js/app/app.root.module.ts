// ANGULAR CORE MODULES --------------------------------------------------------
import {RouterModule,Routes}            from '@angular/router';
import {NgModule}                       from '@angular/core'
import {CommonModule}                   from '@angular/common'
import {BrowserModule}                  from '@angular/platform-browser'
import { HttpClientModule }             from '@angular/common/http';
import {ReactiveFormsModule}            from '@angular/forms'
import {FormsModule}                    from '@angular/forms'
import {BrowserAnimationsModule}        from '@angular/platform-browser/animations';
import {PrimeNgModule}                  from './primeng.module';




// SCOPES ----------------------------------------------------------------------
import {SessionScope}                    from './scopes/session.scope';

// COMPONENTS ------------------------------------------------------------------
import {PluginInfos}                    from './components/plugin_infos/plugin.info';
import {PluginInfosBehaviors}           from './components/plugin_infos_behaviors/plugin.infos.behaviors';
import {AdminBloc}                      from './components/admin_bloc/admin.bloc';
import {EventInfos}                     from './components/event_infos/event.infos';
import {SimpleEventInfos}               from './components/simple_event_infos/simple.event.infos';
import {Msg}                            from './components/msg/msg';
import {Bloc}                           from './components/display/bloc/bloc';
import {Image}                          from './components/display/image/image';
import {SvgComponent}                   from './components/charts/svg_component/svg.component';
import {SystemNotification}             from './components/system_notification/system.notification';
import {AlertingTab}                    from './components/alerting_tab/alerting.tab';
import {InputBloc}                      from './components/forms/input.bloc';
import {InputDaysSelector}              from './components/forms/input.days.selector';
import {InputSwitch}                    from './components/forms/input.switch';
import {InputTimeSlots}                 from './components/forms/input.time.slots';
import {DynamicLevels}                  from './components/forms/dynamic.levels';
import {Carousel}                       from './components/carousel/carousel';
import {PropertiesEditor}               from './components/properties_editor/properties.editor';
import {ClockComponent}                 from './components/clock/clock.component';
import {ServerStateComponent}           from './components/server_state/server.state.component';
import {BarChart}                       from './components/charts/bar_chart/bar.chart';
import {BubbleLegend}                   from './components/charts/bubble_legend/bubble_legend';
import {CurveChart}                     from './components/charts/curve_chart/curve.chart';
import {DoubleBubble}                   from './components/charts/double_bubble/double.bubble';
import {EvolutionValue}                 from './components/charts/evolution_value/evolution.value';
import {SimpleGraph}                    from './components/charts/simple_graph/simple_graph';
import {SimpleValue}                    from './components/charts/simple_value/simple.value';
import {TimeHandler}                    from './components/charts/time_handler/time.handler';
import {Value}                          from './components/charts/value/value';
import {ValueBloc}                      from './components/charts/value_bloc/value.bloc';
import {ValueChart}                     from './components/charts/value_chart/value.chart';
import {SvgGenericMap}                  from './components/charts/svg_generic_map/svg.generic.map';
import {JenkinsJobsComponent}           from './components/providers/jenkins_jobs/jenkins.jobs.component';
import {GitlabMergeRequestsComponent}   from './components/providers/gitlab_merge_requests/gitlab.merge.requests.component';
import {JiraIssuesComponent}            from './components/providers/jira_issues/jira.issues.components';
import {MainMenu}                       from './components/main_menu/main.menu';




// VIEWS -----------------------------------------------------------------------
import {HomeView}                       from './view/home.view';
import {HelpView}                       from './view/help.view';
import {AdminView}                      from './view/admin.view';
import {AdminViewAlerts}                from './view/admin/alerts/admin.view.alerts';
import {AdminViewAlertEdit}             from './view/admin/alerts/admin.view.alert.edit';
import {LoginView}                      from './view/login.view';

// SERVICES --------------------------------------------------------------------

import {PluginsService}                 from 'js/app/services/plugins.service';
import {AdminService}                   from 'js/app/services/admin.services';
import {SecurityServices}               from 'js/app/services/security.services';
import {HeaderServices}                 from 'js/app/services/header.services';
import {SoundServices}                  from 'js/app/services/sound.services';
import {HttpServices}                   from 'js/app/services/http/http.services';
import {GenericCrudServices}            from 'js/app/services/http/generic.crud.services';
import {AlertsCrudServices}             from 'js/app/services/http/alerts.crud.services';
import {AlertsDynamicCrudServices}      from 'js/app/services/http/alerts.dynamic.crud.services';

import {MainMenuService}                from 'js/app/components/main_menu/main.menu.service';


// MODULE ----------------------------------------------------------------------
//PrimeNgModule
@NgModule({
  imports: [CommonModule, BrowserModule, ReactiveFormsModule,
            FormsModule, HttpClientModule,RouterModule,BrowserAnimationsModule,
            PrimeNgModule],
  declarations: [
    HomeView,
    HelpView,
    AdminView,
    LoginView,
    AdminBloc,
    PluginInfos,
    PluginInfosBehaviors,
    EventInfos,
    SimpleEventInfos,

    // SubView
    AdminViewAlerts,
    AdminViewAlertEdit,

    // components
    Bloc,
    Image,
    SvgComponent,
    Msg,
    SystemNotification,
    AlertingTab,
    InputBloc,
    InputSwitch,
    InputTimeSlots,
    InputDaysSelector,
    DynamicLevels,
    Carousel,
    PropertiesEditor,
    ClockComponent,
    ServerStateComponent,
    BarChart,
    BubbleLegend,
    CurveChart,
    DoubleBubble,
    EvolutionValue,
    SimpleGraph,
    SimpleValue,
    TimeHandler,
    Value,
    ValueBloc,
    ValueChart,
    SvgGenericMap,
    JenkinsJobsComponent,
    GitlabMergeRequestsComponent,
    JiraIssuesComponent,
    MainMenu

  ],
  entryComponents: [],
  exports: [
    HomeView,
    // SubView
    AdminViewAlerts,
    AdminViewAlertEdit,


    // components
    Bloc,
    Image,
    SvgComponent,
    Msg,
    SystemNotification,
    AlertingTab,
    InputBloc,
    InputSwitch,
    InputTimeSlots,
    InputDaysSelector,
    DynamicLevels,
    Carousel,
    PropertiesEditor,
    ClockComponent,
    ServerStateComponent,
    BarChart,
    BubbleLegend,
    CurveChart,
    DoubleBubble,
    EvolutionValue,
    SimpleGraph,
    SimpleValue,
    TimeHandler,
    Value,
    ValueBloc,
    ValueChart,
    SvgGenericMap,
    JenkinsJobsComponent,
    GitlabMergeRequestsComponent,
    JiraIssuesComponent,
    MainMenu

  ],
  providers: [
    SessionScope,
    PluginsService,
    AdminService,
    SecurityServices,
    HeaderServices,
    SoundServices,
    HttpServices,
    AlertsCrudServices,
    AlertsDynamicCrudServices,
    MainMenuService,
    GenericCrudServices
  ],
  bootstrap: []
})
export class AppRootModule {
}





const  AppRootModuleRoutesConfig: Routes = [
    
    { path: 'help' , component: HelpView },
    { path: 'admin', component: AdminView },
    { path: 'login', component: LoginView },
    { path: ''     , component: HomeView , pathMatch:'full' }
];
export const AppRootModuleRoutes = RouterModule.forRoot(AppRootModuleRoutesConfig);

