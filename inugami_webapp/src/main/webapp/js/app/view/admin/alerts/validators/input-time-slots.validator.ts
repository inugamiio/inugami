import { AbstractControl,ValidatorFn } from '@angular/forms';
export function inputTimeSlotsValidator(): ValidatorFn {
    return (control: AbstractControl): {[key: string]: any} | null => {
        let forbidden = false;
        for(let timeSlot of control.value){
            if(timeSlot.from == "" || timeSlot.to == ""){
                forbidden = true;
            }
        }
      return forbidden ? {'forbidden': {value: control.value}} : null;
    };
  }