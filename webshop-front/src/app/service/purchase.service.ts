import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {PurchaseRequest} from '../model/request/PurchaseRequest';
import {backendUrl} from '../constants/backendUrl';

@Injectable({
  providedIn: 'root',
})
export class PurchaseService {

  constructor(private http: HttpClient) {
  }

  purchaseProduct(productId: number, purchaseRequest: PurchaseRequest) {
    const url = backendUrl.PRODUCTS + `/${productId}/` + backendUrl.PURCHASES;
    return this.http.post(url, purchaseRequest);
  }
}
