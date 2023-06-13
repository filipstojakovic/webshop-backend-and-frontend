import {formatDate} from '@angular/common';

function fileToBase64(file: File): Promise<string> {
  return new Promise<string>((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    // @ts-ignore
    reader.onload = () => resolve(reader.result.toString());
    reader.onerror = error => reject(error);
  })
}

const CUSTOM_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss"

function customDateFormat(date: Date) {
  return formatDate(date, CUSTOM_DATE_FORMAT, "en-UK")
}

const myUtils = {
  fileToBase64,
  customDateFormat
}

export default myUtils;
