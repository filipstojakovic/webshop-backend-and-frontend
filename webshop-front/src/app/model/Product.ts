import {Category} from './Category';
import {User} from './User';
import {ProductImage} from './ProductImage';

export class Product {
  id?: number;
  name: string = "";
  description: string = "";
  price: number = 0;
  location: string = "";
  isNew: boolean = true;
  productImages: ProductImage[] = [];
  seller: User | null = null;
  category: Category | null = null;

}
