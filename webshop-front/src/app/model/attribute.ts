export class Attribute {
  id: number;
  name: string;

  constructor(id: number, name: string) {
    this.id = id;
    this.name = name;
  }
}

export class AttributeNameValue {
  name: string;
  value: string|null;


  constructor(name: string, value: string | null) {
    this.name = name;
    this.value = value;
  }
}
