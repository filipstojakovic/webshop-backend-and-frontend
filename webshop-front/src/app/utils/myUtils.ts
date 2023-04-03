import {FormGroup} from "@angular/forms";

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

export function clearFormErrors(formGroup: FormGroup) {
  Object.keys(formGroup.value).forEach(key => {
    formGroup.get(key)?.setErrors(null);
  });
}

const myUtils = {
  formGroupToFormDataConverter,
  clearFormErrors,
}

export default myUtils;

