import jwt_decode from 'jwt-decode';
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

const myUtils = {
  formGroupToFormDataConverter
}

export default myUtils;

