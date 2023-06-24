import {Category} from './Category';
import {User} from './User';
import {ProductImage} from './ProductImage';
import {AttributeNameValue} from './request/AttributeNameValue';

export class Product {
  id?: number;
  name: string = "";
  description: string = "";
  price: number = 0;
  location: string = "";
  isNew: boolean = true;
  isPurchased: boolean = false;
  productImages: ProductImage[] = [];
  seller: User | null = null;
  category: Category | null = null;
  productAttributes: AttributeNameValue[]
}
