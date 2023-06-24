import {Component, Input} from '@angular/core';
import {Product} from '../../../model/Product';
import {baseUrl} from '../../../constants/backendUrl';

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.css'],
})
export class ProductCardComponent {

  @Input() product!: Product;

  constructor() {
  }

  protected readonly baseUrl = baseUrl;
}
