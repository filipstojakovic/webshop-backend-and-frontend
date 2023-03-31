import {Component, OnInit} from '@angular/core';
import {debounceTime, distinctUntilChanged, Subject} from 'rxjs';
import {constant} from '../../constants/constants';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css'],
})
export class ProductComponent implements OnInit {
  public searchText: string = '';
  searchTextUpdate = new Subject<string>();
  //selectedCategory


  onKeyDown(event: any) {
    if (event?.key === 'Enter') {
    }
  }

  productCardClick(i: number) {
    console.log("product.component.ts > productCardClick(): " + i);

  }

  ngOnInit(): void {
    this.searchTextUpdate.pipe(
        debounceTime(constant.DEBOUNCE_TIME),
        distinctUntilChanged())
        .subscribe(value => {
          console.log("product.component.ts > (): " + value);
          //TODO: send seach request.
          // Text + Selected category
        });
  }
}
