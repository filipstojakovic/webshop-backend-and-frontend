import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {GenericCrudService} from '../../../service/GenericCrud.service';
import {Product} from '../../../model/Product';
import {backendUrl, baseUrl} from '../../../constants/backendUrl';
import {ToastService} from 'angular-toastify';
import {paths} from '../../../constants/paths';
import {FormBuilder} from '@angular/forms';
import {MatDialog} from '@angular/material/dialog';
import {PaymentModalComponent} from '../../../components/payment-modal/payment-modal.component';
import {PurchaseService} from '../../../service/purchase.service';
import {PurchaseRequest} from '../../../model/request/PurchaseRequest';
import tokenService from '../../../service/TokenService';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css'],
})
export class ProductDetailsComponent implements OnInit {
  productService!: GenericCrudService<Product, Product>
  product: Product | null = null;

  constructor(private activeRoute: ActivatedRoute,
              private http: HttpClient,
              private toastService: ToastService,
              private router: Router,
              private fb: FormBuilder,
              private dialog: MatDialog,
              private purchaseService: PurchaseService,
  ) {
    this.productService = new GenericCrudService<Product, Product>(backendUrl.PRODUCTS, http);
  }

  ngOnInit(): void {
    const id = this.activeRoute.snapshot.paramMap.get('id');
    const productId: number = parseInt(id || "-1");
    this.productService.getById(productId).subscribe({
          next: (res) => {
            this.product = res;
          },
          error: (err) => {
            this.toastService.error("Error getting the product details");
            this.router.navigateByUrl(paths.PRODUCTS);
          },
        },
    )
  }

  purchaseButtonClicked() {
    const dialogRef = this.dialog.open(
        PaymentModalComponent,
        {
          data: { dialogData: this.product },
        });

    dialogRef.afterClosed().subscribe((result: PurchaseRequest) => {
      if (!result) {
        return;
      }

      this.purchaseService.purchaseProduct(this.product!.id!, result).subscribe({
            next: (res) => {
              this.toastService.success("Product purchased!");
              this.router.navigateByUrl(paths.PURCHASE_HISTORY);
            },
            error: (err) => {
              this.toastService.error(err.error);
            },
          },
      )
    });
  }

  deleteButtonClicked() {
    this.productService.delete(this.product?.id!).subscribe({
          next: (res) => {
            this.toastService.success("Product deleted!")
            this.router.navigateByUrl(paths.USER_PRODUCTS);
          },
          error: (err) => {
            this.toastService.error(err.error)
          },
        },
    )
  }

  shouldShowDeleteButton(): boolean {
    const userId = tokenService.getIdFromToken();
    if (!userId)
      return false;

    return this.product?.seller?.id === userId;
  }

  protected readonly baseUrl = baseUrl;
}
