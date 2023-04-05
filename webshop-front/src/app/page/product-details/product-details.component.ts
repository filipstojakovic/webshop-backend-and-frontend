import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, ActivatedRouteSnapshot} from '@angular/router';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css'],
})
export class ProductDetailsComponent implements OnInit {
  productId: number | null;

  constructor(private activeRoute: ActivatedRoute) {
    this.productId = 0;
  }

  ngOnInit(): void {
    const id = this.activeRoute.snapshot.paramMap.get('id');
    this.productId = parseInt(id || "");
    console.log("product-details.component.ts > ngOnInit(): " + JSON.stringify(this.productId, null, 2));
  }


}
