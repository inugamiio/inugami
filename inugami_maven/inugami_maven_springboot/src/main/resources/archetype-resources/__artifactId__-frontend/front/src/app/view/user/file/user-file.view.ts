import {Component, inject, signal} from '@angular/core';

import {InuErrorService} from 'inugami-ng/services'
import {InuButton} from 'inugami-ng/components/inu-button'
import {UserService} from '../../../service/user/user.service'
import {ActivatedRoute, Router} from '@angular/router'
import {InuInputText} from 'inugami-ng/components/inu-input-text'
import {form, FormField, required} from '@angular/forms/signals';
import {ReactiveFormsModule} from '@angular/forms'
import {InuFormsUtils} from 'inugami-ng/utils'
import {UserAPI} from '../../../model/user.model'
import {InuIcon} from 'inugami-icons'
import {map} from 'rxjs'
import {takeUntilDestroyed} from '@angular/core/rxjs-interop'
import {InuToastServices} from 'inugami-ng/components/inu-toast'
import {untracked} from '@angular/core/primitives/signals'

interface UserForm {
  firstName: string;
  lastName: string;
  email: string;
}


@Component({
             templateUrl: './user-file.view.html',
             styleUrls  : ['./user-file.view.scss'],
             imports    : [
               ReactiveFormsModule,
               InuInputText,
               FormField,
               InuButton,
               InuIcon
             ]
           })
export class UserFileView {

  // ===================================================================================================================
  // ATTRIBUTES
  // ===================================================================================================================
  activatedRoute  = inject(ActivatedRoute);
  inuErrorService = inject(InuErrorService);
  userService     = inject(UserService);
  router          = inject(Router);
  toastServices   = inject(InuToastServices);
  uid             = signal<string | undefined>(undefined);
  userModel       = signal<UserForm>({
                                       firstName: '',
                                       lastName : '',
                                       email    : ''
                                     });
  initValue       = signal<string>('');
  loading         = signal<boolean>(true);
  hasChanged      = signal<boolean>(false);
  userForm        = form(this.userModel, (path) => {
    required(path.email);
  });

  saving = signal<boolean>(false);
  valid  = signal<boolean>(false);
  // ===================================================================================================================
  // INIT
  // ===================================================================================================================

  constructor() {
    this.activatedRoute.paramMap.pipe(map(params => params.get('uid')!),
                                      takeUntilDestroyed())
      .subscribe(uid => {
        if (uid == 'new') {
          this.initUser({});
        } else {
          this.uid.set(uid);
          this.loadUser(uid);
        }
      })

    InuFormsUtils.onChanged(this.userModel)
      .subscribe(value => {
        if (this.loading()) return;
        this.onChanged(value)
      });

  }

  private loadUser(uid: string) {
    this.userService.getByUid(uid)
      .subscribe({
                   next : res => this.initUser(res),
                   error: err => this.inuErrorService.handlerError(err)
                 });
  }

  private initUser(param: UserAPI) {
    untracked(() => {
      this.userModel.set({
                           email    : param.email ?? '',
                           firstName: param.firstName ?? '',
                           lastName : param.lastName ?? '',
                         });
    });
    this.initValue.set(JSON.stringify(this.userModel()));
    this.loading.set(false);
    this.hasChanged.set(false);
  }

  // ===================================================================================================================
  // EVENT
  // ===================================================================================================================
  private onChanged(value: UserForm) {
    this.hasChanged.set(this.initValue() != JSON.stringify(this.userModel()));
    this.valid.set(InuFormsUtils.isValid(this.userForm).valid);
  }

  // ===================================================================================================================
  // ACTIONS
  // ===================================================================================================================

  save() {
    const user = this.extractUser();

    if (this.uid()) {
      this.userService.update([user])
        .subscribe({
                     next : res => {
                       this.toastServices.addMessage({
                                                       title  : 'Successful user updated',
                                                       message: 'The user habe been correctly updated',
                                                       level  : "info",
                                                       icon   : 'approval'
                                                     });
                       this.hasChanged.set(false);
                       this.goToFile(this.uid()!);
                     },
                     error: err => this.inuErrorService.handlerError(err)
                   });
    } else {
      this.userService.create([user])
        .subscribe({
                     next : res => {
                       this.toastServices.addMessage({
                                                       title  : 'Successful user created',
                                                       message: 'The user habe been correctly created',
                                                       level  : "info",
                                                       icon   : 'approval'
                                                     });
                       this.hasChanged.set(false);
                       this.goToFile(res[0]?.uid!);
                     },
                     error: err => this.inuErrorService.handlerError(err)
                   });
    }


  }

  private extractUser(): UserAPI {
    const user = this.userModel();
    return {
      uid      : this.uid(),
      lastName : user.lastName.trim() == '' ? undefined : user.lastName.trim(),
      firstName: user.firstName.trim() == '' ? undefined : user.firstName.trim(),
      email    : user.email.trim() == '' ? undefined : user.email.trim(),
    }
  }


  cancel() {
    if (this.uid() && this.hasChanged()) {
      this.router.navigate([`/user/file/${this.uid()!}`]);
    } else {
      this.router.navigate(['/user']);
    }
  }


  private goToFile(uid: string) {
    this.router.navigate([`/user/file/${uid}`]);
  }


}
