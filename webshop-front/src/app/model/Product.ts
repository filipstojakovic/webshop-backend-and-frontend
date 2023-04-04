import {Category} from './category';
import {User} from './user';

export class Product {
  id: number;
  name: string;
  description: string;
  price: number;
  location: string;
  isNew: boolean;
  image: string | null;
  seller: User;
  category: Category;
  purchase: any | null; // replace 'any' with the actual type of the purchase object

  constructor(data: any) {
    this.id = data.id;
    this.name = data.name;
    this.description = data.description;
    this.price = data.price;
    this.location = data.location;
    this.isNew = data.isNew;
    this.image = data.image;
    this.seller = new User(data.seller);
    this.category = new Category(data.category);
    this.purchase = data.purchase;
  }
}
