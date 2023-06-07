import {Component, OnInit} from '@angular/core';
import {debounceTime} from 'rxjs';
import {constant} from '../../constants/constants';
import {PageEvent} from '@angular/material/paginator';
import {ProductService} from '../../service/product.service';
import {Product} from '../../model/Product';
import tokenService from '../../service/TokenService';
import {ToastService} from 'angular-toastify';
import {Router} from '@angular/router';
import {paths} from '../../constants/paths';
import {Category} from '../../model/Category';
import {GenericCrudService} from '../../service/GenericCrud.service';
import {backendUrl} from '../../constants/backendUrl';
import {HttpClient} from '@angular/common/http';
import {FormBuilder, FormGroup} from '@angular/forms';
import {ProductSearchRequest} from '../../model/request/ProductSearchRequest';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css'],
})
export class ProductComponent implements OnInit {

  form!: FormGroup;

  categoryService!: GenericCrudService<Category>

  totalNumber: number = 0;
  private currentPageNumber: number = 0;
  pageSize: number = 5;

  products: Product[] | undefined | null;

  constructor(private productService: ProductService,
              private router: Router,
              private toastService: ToastService,
              private http: HttpClient,
              private fb: FormBuilder,
  ) {

    this.categoryService = new GenericCrudService<Category>(backendUrl.CATEGORIES, http);
    const userId: number = tokenService.getIdFromToken();
    this.pageSize = Number.parseInt(sessionStorage.getItem(userId + "") ?? "5");

    this.form = this.fb.group({
      nameSearch: "",
      attributeNameValueRequests: [],
      categoryIdSearch: "",
    });
  }

  ngOnInit(): void {
    this.getProducts();

    this.form.valueChanges
        .pipe(debounceTime(constant.DEBOUNCE_TIME))
        .subscribe(() => {
          this.currentPageNumber = 0;
          this.getProducts(this.form.value);
        });
  }

  getProducts(body?: ProductSearchRequest) {
    //TODO: use form to send attributes
    this.productService.searchProducts(this.currentPageNumber, this.pageSize, body).subscribe({
      next: (res: any) => {
        this.totalNumber = res.totalElements;
        this.products = res.content;
      },
      error: () => {
        this.products = [];
        this.toastService.error("Error showing products");
      },
    })
  }

  productCardClick(product: Product) {
    const productId = product ? product.id : null;
    console.log("product.component.ts > productCardClick(): " + JSON.stringify(productId, null, 2));
    this.router.navigateByUrl(paths.PRODUCTS + "/" + productId);
  }

  onPaginatorChange($event: PageEvent) {
    const {
      pageIndex,
      pageSize,
    } = $event;

    this.pageSize = pageSize;
    sessionStorage.setItem(tokenService.getIdFromToken() + "", pageSize + "");
    this.currentPageNumber = pageIndex;

    this.getProducts();
  }

  onKeyDown() {
    console.log("product.component.ts > onKeyDown(): " + JSON.stringify("enter clicked", null, 2));
    //   //TODO: do something with enter search box in focus?
  }
}
