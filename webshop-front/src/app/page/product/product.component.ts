import { Component } from '@angular/core';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent {
  searchText: string='test';

  onKeyDown(event:any) {
    if (event?.key === 'Enter') {
    }
  }

  productCardClick(i: number) {
    console.log("product.component.ts > productCardClick(): "+ i);
  }
}
