export class Attribute {
  id: number;
  name: string;

  constructor(id: number, name: string) {
    this.id = id;
    this.name = name;
  }
}

export class AttributeValue extends Attribute {
  value: string;

  constructor(id: number, name: string, value: string) {
    super(id, name);
    this.value = value;
  }

  override toString(): string {
    return this.value;
  }
}
