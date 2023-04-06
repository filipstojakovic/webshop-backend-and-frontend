import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, ActivatedRouteSnapshot, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {GenericCrudService} from '../../../service/GenericCrudService';
import {Product} from '../../../model/Product';
import {backendUrl} from '../../../constants/backendUrl';
import {ToastService} from 'angular-toastify';
import {paths} from '../../../constants/paths';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css'],
})
export class ProductDetailsComponent implements OnInit {
  productService!: GenericCrudService<Product>
  product: Product | null = null;


  constructor(private activeRoute: ActivatedRoute,
              private http: HttpClient,
              private toastService: ToastService,
              private router: Router,
  ) {
    this.productService = new GenericCrudService<Product>(backendUrl.PRODUCTS, http);
  }

  ngOnInit(): void {
    const id = this.activeRoute.snapshot.paramMap.get('id');
    const productId: number = parseInt(id || "");
    this.productService.getById(productId).subscribe({
          next: (res) => {
            this.product = res;
          },
          error: (err) => {
            this.toastService.error("Error geting the product details");
            this.router.navigateByUrl(paths.PRODUCTS);
          },
        },
    )
  }


}
