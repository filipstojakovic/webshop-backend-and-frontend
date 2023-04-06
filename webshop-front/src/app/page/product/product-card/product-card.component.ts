import {Component, Inject, Input} from '@angular/core';
import {Product} from '../../../model/Product';

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.css'],
})
export class ProductCardComponent {

  @Input() product!: Product;

  constructor() {
  }
}
