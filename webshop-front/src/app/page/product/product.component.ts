import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {debounceTime, distinctUntilChanged, Subject} from 'rxjs';
import {constant} from '../../constants/constants';
import {HttpClient} from '@angular/common/http';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {ProductService} from '../../service/product.service';
import {Product} from '../../model/Product';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css'],
})
export class ProductComponent implements OnInit {
  public searchText: string = '';
  searchTextUpdate = new Subject<string>();
  //selectedCategory
  totalNumber: number = 0;
  private currentPageNumber: number = 0;
  pageSize: number = 2; // TODO: go back to 10

  products: Product[] = [];

  constructor(private productService: ProductService) {
  }

  ngOnInit(): void {
    this.getProducts();

    this.searchTextUpdate.pipe(
        debounceTime(constant.DEBOUNCE_TIME),
        distinctUntilChanged())
        .subscribe(value => {
          console.log("product.component.ts > (): " + value);
          //TODO: send seach request.
          // Text + Selected category
        });
  }

  getProducts() {
    this.productService.getProducts(this.currentPageNumber, this.pageSize).subscribe({
          next: (res: any) => {
            this.totalNumber = res.totalElements;
            this.products = res.content;
            console.log("product.component.ts > next(): " + JSON.stringify(this.products, null, 2));
          },
          error: (err) => {

          },
        },
    )
  }

  onKeyDown(event: any) {
    if (event?.key === 'Enter') {
    }
  }

  productCardClick(product: Product) {
    console.log("product.component.ts > productCardClick(): "+ JSON.stringify(product, null, 2));
  }

  onPaginatorChange($event: PageEvent) {
    console.table($event);

    const {
      pageIndex,
      pageSize,
      length,
    } = $event;

    this.pageSize = pageSize;
    this.currentPageNumber = pageIndex;
    this.getProducts();
  }
}
