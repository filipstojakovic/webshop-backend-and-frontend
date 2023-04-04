import {Attribute} from './attribute';

export class Category {
  id: number;
  name: string;
  attributes: Attribute[]; // replace 'any' with the actual type of the attributes object

  constructor(data: any) {
    this.id = data.id;
    this.name = data.name;
    this.attributes = data.attributes;
  }

  toString(): string {
    return this.name;
  }

}
