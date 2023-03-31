import {Component} from '@angular/core';

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.css'],
})
export class ProductCardComponent {

  likeClick($event: MouseEvent) {

    $event.stopPropagation();

    console.log("product-card.component.ts > likeClick(): " + "like");
  }
}
