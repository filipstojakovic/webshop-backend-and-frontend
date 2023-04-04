import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {backendUrl} from '../constants/backendUrl';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http:HttpClient) { }

  getProducts(currentPageIndex:number, pageSize:number){
    const url = `${backendUrl.PRODUCTS}?page=${currentPageIndex}&size=${pageSize}`;
    return this.http.get(url);
  }


}
