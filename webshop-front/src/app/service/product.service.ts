import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {backendUrl} from '../constants/backendUrl';
import {ProductSearchRequest} from '../model/request/ProductSearchRequest';

@Injectable({
  providedIn: 'root',
})
export class ProductService {

  constructor(private http: HttpClient) {
  }

  searchProducts(currentPageIndex: number, pageSize: number, searchUrl:string, body?: ProductSearchRequest) {
    // console.log("product.service.ts > getProducts(): BODY" + JSON.stringify(body, null, 2));
    const url = `${searchUrl}?page=${currentPageIndex}&size=${pageSize}`;
    return this.http.post(url, body);
  }

}
