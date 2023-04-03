function fileToBase64(file: File): Promise<string> {
  return new Promise<string>((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    // @ts-ignore
    reader.onload = () => resolve(reader.result.toString());
    reader.onerror = error => reject(error);
  })
}

const myUtils = {
  fileToBase64,
}

export default myUtils;
