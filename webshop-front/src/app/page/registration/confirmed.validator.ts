import {FormGroup} from '@angular/forms';

export function ConfirmedValidator(password: string, confirm_password: string) {

  return (formGroup: FormGroup) => {
    const control = formGroup.controls[password];
    const matchingControl = formGroup.controls[confirm_password];

    if (matchingControl.errors && !matchingControl.errors['confirmedValidator']) {
      return;
    }

    if (control.value !== matchingControl.value) {
      matchingControl.setErrors({ confirmedValidator: true });
    } else {
      matchingControl.setErrors(null);
    }
  }
}
