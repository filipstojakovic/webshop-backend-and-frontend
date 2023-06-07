export class AttributeNameValue {
  name: string;
  value: string|null;

  constructor(name: string, value: string | null) {
    this.name = name;
    this.value = value;
  }
}
