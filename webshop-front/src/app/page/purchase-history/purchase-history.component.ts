import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {backendUrl} from '../../constants/backendUrl';
import {ToastService} from 'angular-toastify';
import {Purchase} from '../../model/Purchase';
import {Product} from '../../model/Product';
import {paths} from '../../constants/paths';
import {Router} from '@angular/router';

@Component({
  selector: 'app-purchase-history',
  templateUrl: './purchase-history.component.html',
  styleUrls: ['./purchase-history.component.css'],
})
export class PurchaseHistoryComponent implements OnInit {
  purcases: Purchase[] = [];

  constructor(private http: HttpClient, private toast: ToastService, private router: Router) {
  }

  ngOnInit() {
    this.getPurchases()
  }

  getPurchases() {
    this.http.get<Purchase[]>(backendUrl.PURCHASES).subscribe({
          next: (res) => {
            this.purcases = res;
          },
          error: () => {
            this.toast.error("Unable to get purchase history")
          },
        },
    )
  }


  protected readonly JSON = JSON;

  productCardClick(product: Product) {
    const productId = product ? product.id : null;
    console.log("product.component.ts > productCardClick(): " + JSON.stringify(productId, null, 2));
    this.router.navigateByUrl(paths.PRODUCTS + "/" + productId);
  }
}
