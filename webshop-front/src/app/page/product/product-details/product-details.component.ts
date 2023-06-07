import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {GenericCrudService} from '../../../service/GenericCrud.service';
import {Product} from '../../../model/Product';
import {backendUrl} from '../../../constants/backendUrl';
import {ToastService} from 'angular-toastify';
import {paths} from '../../../constants/paths';
import {FormBuilder, FormGroup} from '@angular/forms';
import {MatDialog} from '@angular/material/dialog';
import {PaymentModalComponent} from '../../../components/payment-modal/payment-modal.component';
import {PurchaseRequest} from '../../../model/request/PurchaseRequest';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css'],
})
export class ProductDetailsComponent implements OnInit {
  productService!: GenericCrudService<Product>
  product: Product | null = null;
  form: FormGroup;


  constructor(private activeRoute: ActivatedRoute,
              private http: HttpClient,
              private toastService: ToastService,
              private router: Router,
              private fb: FormBuilder,
              private dialog: MatDialog,
  ) {
    this.productService = new GenericCrudService<Product>(backendUrl.PRODUCTS, http);
  }

  ngOnInit(): void {
    const id = this.activeRoute.snapshot.paramMap.get('id');
    const productId: number = parseInt(id || "");
    this.productService.getById(productId).subscribe({
          next: (res) => {
            this.product = res;
            this.form = this.fb.group({
              categorySearch: this.product.category?.name,
              attributes: [], /*TODO: do something with attributes*/
              name: this.product.name,
              description: this.product.description,
              price: this.product.price,
              location: this.product.location,
              isNew: this.product.isNew,
              images: [], /*TODO: do something with images*/
            });
          },
          error: (err) => {
            this.toastService.error("Error geting the product details");
            this.router.navigateByUrl(paths.PRODUCTS);
          },
        },
    )
  }

  purchaseButtonClicked() {

    console.log("product-details.component.ts > purchaseButtonClicked(): " + "purchuse btn clicked");
    const dialogRef = this.dialog.open(
        PaymentModalComponent,
        {
          data: { dialogData: this.product },
        });

    dialogRef.afterClosed().subscribe((result: PurchaseRequest) => {
      if (!result) {
        console.log("header.component.ts > headerBtnClicked(): " + "result undefined");
        return;
      }

      console.log("product-details.component.ts > (): " + JSON.stringify(result, null, 2));
      // TODO: call service to purchase product
    });
  }
}
