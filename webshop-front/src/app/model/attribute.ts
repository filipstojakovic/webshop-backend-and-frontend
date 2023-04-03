export type Attribute = {
  id: number;
  name: string;
}

export type AttributeValue = Attribute & {
  value: string;
}
