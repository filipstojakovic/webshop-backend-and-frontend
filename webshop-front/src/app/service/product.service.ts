import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {backendUrl} from '../constants/backendUrl';

@Injectable({
  providedIn: 'root',
})
export class ProductService {

  constructor(private http: HttpClient) {
  }

  getProducts(currentPageIndex: number, pageSize: number, searchText = "") {
    console.log("product.service.ts > getProducts(): " + JSON.stringify(searchText, null, 2));
    const url = `${backendUrl.PRODUCTS}?page=${currentPageIndex}&size=${pageSize}&searchText=${searchText}`;
    return this.http.get(url);
  }

}
