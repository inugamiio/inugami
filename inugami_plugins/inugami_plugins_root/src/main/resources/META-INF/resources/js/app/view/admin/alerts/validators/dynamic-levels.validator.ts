import { AbstractControl,ValidatorFn } from '@angular/forms';
export function dynamicLevelsValidator(): ValidatorFn {
  return (control: AbstractControl): {[key: string]: any} | null => {
    if(control.value != null){
      return control.value.length > 0 ? {'forbidden': {value: control.value}} : null;
    }
  };
}