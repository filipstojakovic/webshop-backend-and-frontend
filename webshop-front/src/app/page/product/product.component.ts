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
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {ProductSearchRequest} from '../../model/request/ProductSearchRequest';
import {Attribute} from '../../model/Attribute';
import {AttributeNameValue} from '../../model/request/AttributeNameValue';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css'],
})
export class ProductComponent implements OnInit {

  searchForm: FormGroup;
  attributeForm: FormGroup;

  categoryService!: GenericCrudService<Category, Category>;

  totalNumber: number = 0;
  private currentPageNumber: number = 0;
  pageSize: number = 5;

  products: Product[] | undefined | null;
  attributes: Attribute[] | undefined | null;

  constructor(private productService: ProductService,
              private router: Router,
              private toastService: ToastService,
              private http: HttpClient,
              private fb: FormBuilder,
  ) {

    this.categoryService = new GenericCrudService<Category, Category>(backendUrl.CATEGORIES, http);

    const userId: number = tokenService.getIdFromToken();
    this.pageSize = Number.parseInt(sessionStorage.getItem(userId + "") ?? "5");

    this.searchForm = this.fb.group({
      nameSearch: "",
      categoryIdSearch: "",
      attributeNameValueSearches: this.fb.group({}),
    });

    this.attributeForm = this.fb.group({});
  }

  ngOnInit(): void {
    this.getProducts();

    this.searchForm.valueChanges
        .pipe(debounceTime(constant.DEBOUNCE_TIME))
        .subscribe(() => {
          console.table(this.searchForm.value);
          this.currentPageNumber = 0;


          let { attributeNameValueSearches } = this.searchForm.value;
          let attributeReq = Object.keys(attributeNameValueSearches).map(key => {
            return new AttributeNameValue(key, attributeNameValueSearches[key])
          })

          this.searchForm.value.attributeNameValueSearches = attributeReq;
          this.getProducts(this.searchForm.value);
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

  createAttributeForm(attributes: Attribute[]) {
    this.attributeForm = this.fb.group({});
    attributes.map(attribute => {
      this.attributeForm?.addControl(attribute.name, new FormControl(""))// new FormControl<Attribute | null>(null))
    });
    this.searchForm.setControl("attributeNameValueSearches", this.attributeForm);
  }

  categoryChangeEvent(category: any) {
    if (!category) {
      this.attributeForm = this.fb.group({});
      return;
    }

    const categoryId = category.id;
    this.http.get<Attribute[]>(backendUrl.CATEGORIES + `/${categoryId}/attributes`).subscribe({
          next: (attributes) => {
            this.attributes = attributes;
            console.log("product.component.ts > next(): " + JSON.stringify(attributes, null, 2));
            this.createAttributeForm(attributes);
          },
        },
    )
  }

  showAttributeFields() {
    return this.attributeForm != null && Object.keys(this.attributeForm.controls).length > 0;
  }
}
