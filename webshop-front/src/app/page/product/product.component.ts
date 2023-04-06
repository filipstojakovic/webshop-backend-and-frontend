import {Component, OnInit} from '@angular/core';
import {debounceTime, distinctUntilChanged, Subject} from 'rxjs';
import {constant} from '../../constants/constants';
import {PageEvent} from '@angular/material/paginator';
import {ProductService} from '../../service/product.service';
import {Product} from '../../model/Product';
import tokenService from '../../service/TokenService';
import {ToastService} from 'angular-toastify';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css'],
})
export class ProductComponent implements OnInit {
  public searchText: string = '';
  searchTextUpdate = new Subject<string>();
  //add selectedCategory field when ready

  totalNumber: number = 0;
  private currentPageNumber: number = 0;
  pageSize: number = 2; // TODO: go back to 10

  products: Product[] | undefined | null;

  constructor(private productService: ProductService, private toastService: ToastService) {
    const userId: number = tokenService.getIdFromToken();
    //TODO: uncomment next line
    // this.pageSize = Number.parseInt(sessionStorage.getItem(userId + "") ?? "10");
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
            this.toastService.error("Error showing products");
          },
        },
    )
  }

  onKeyDown(event: any) {
    if (event?.key === 'Enter') {
      //TODO: do something with enter?
    }
  }

  productCardClick(product: Product) {
    console.log("product.component.ts > productCardClick(): " + JSON.stringify(product, null, 2));
  }

  onPaginatorChange($event: PageEvent) {
    console.table($event);

    const {
      pageIndex,
      pageSize,
      length,
    } = $event;

    this.pageSize = pageSize;
    sessionStorage.setItem(tokenService.getIdFromToken() + "", pageSize + "");
    this.currentPageNumber = pageIndex;
    this.getProducts();
  }
}
