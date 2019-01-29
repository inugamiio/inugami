import {Component,Input}    from '@angular/core';


@Component({
  selector      : 'admin-bloc',
  templateUrl   : 'js/app/components/admin_bloc/admin.bloc.html',
  directives    : [ ],
})
export class AdminBloc{

  /*****************************************************************************
  * ATTRIBUTES
  *****************************************************************************/
  @Input() title                      : string;
  @Input() styleClass                 : string;
  @Input() style                      : string;
}
