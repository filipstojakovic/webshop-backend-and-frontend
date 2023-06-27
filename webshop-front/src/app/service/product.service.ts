import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ProductSearchRequest} from '../model/request/ProductSearchRequest';

@Injectable({
  providedIn: 'root',
})
export class ProductService {

  constructor(private http: HttpClient) {
  }

  searchProducts(currentPageIndex: number, pageSize: number, searchUrl:string, body?: ProductSearchRequest) {
    const url = `${searchUrl}?page=${currentPageIndex}&size=${pageSize}`;
    return this.http.post(url, body);
  }

}
