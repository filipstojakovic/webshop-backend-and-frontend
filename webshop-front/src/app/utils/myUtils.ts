import {FormGroup} from "@angular/forms";

export function formGroupToFormDataConverter(formGroup: FormGroup) {
  const formData = new FormData();
  Object.keys(formGroup.value).forEach(key => {
    const value = formGroup.get(key)?.value;
    if (value !== null && value !== undefined) {
      formData.append(key, value);
    }
  });
  return formData;
}

export function clearFormErrors(formGroup: FormGroup) {
  Object.keys(formGroup.value).forEach(key => {
    formGroup.get(key)?.setErrors(null);
  });
}

const myUtils = {
  formGroupToFormDataConverter,
  clearFormErrors
}

export default myUtils;

