import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {PaymentMethod} from '../model/PaymentMethod';
import {backendUrl} from '../constants/backendUrl';

@Injectable({
  providedIn: 'root',
})
export class PaymentService {

  constructor(private http: HttpClient) {
  }

  public getPaymentMethods() {
    return this.http.get<PaymentMethod[]>(backendUrl.PAYMENT_METHODS);
  }
}
