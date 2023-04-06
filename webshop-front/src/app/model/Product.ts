import {Category} from './Category';
import {User} from './User';

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
  }
}
