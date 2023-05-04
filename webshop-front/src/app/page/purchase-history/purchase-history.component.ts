import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Product} from '../../model/Product';

@Component({
  selector: 'app-purchase-history',
  templateUrl: './purchase-history.component.html',
  styleUrls: ['./purchase-history.component.css'],
})
export class PurchaseHistoryComponent implements OnInit{

  ngOnInit() {
  }

  constructor(private http:HttpClient) {
  }

  products:Product[]=[];

  getProducts(){
    this.http.get<Product[]>("/products").subscribe({
        next: (res) => {
            this.products=res;
        },
        error: (err) => {

        }
     }
           )
  }


}
