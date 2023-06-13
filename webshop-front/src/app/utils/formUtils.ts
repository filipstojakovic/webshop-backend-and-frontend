import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";

function createObjectData(formData: FormData, parentKey: string, value: Object) {
  for (const key in value) {
    const newKey = parentKey + '.' + key;
    // @ts-ignore
    formData.append(newKey, value[key].toString())
  }
}

export function formGroupToFormDataConverter(formGroup: FormGroup) {
  const formData = new FormData();
  Object.keys(formGroup.value).forEach(key => {
    const value = formGroup.get(key)?.value;
    if (value !== null && value !== undefined) {
      if (value instanceof Object)
        createObjectData(formData, key, value);
      // formData.append(key, JSON.stringify(value));
      else
        formData.append(key, value);
    }
  });
  return formData;
}

export function createFormFromObject(fb:FormBuilder, object:any){
  const form = fb.group({});
  Object.keys(object).forEach(key => {
    const control = new FormControl(object[key]);
    form.addControl(key, control);
  });

  return form;
}

export function clearFormErrors(formGroup: FormGroup) {
  Object.keys(formGroup.value).forEach(key => {
    formGroup.get(key)?.setErrors(null);
  });
}

const formUtils = {
  formGroupToFormDataConverter,
  clearFormErrors,
  createFormFromObject
}

export default formUtils;

