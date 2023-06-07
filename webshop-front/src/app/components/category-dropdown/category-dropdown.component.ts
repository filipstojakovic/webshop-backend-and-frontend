import {Component, Input, OnInit} from '@angular/core';
import {FormControl} from '@angular/forms';
import {Category} from '../../model/Category';
import {HttpClient} from '@angular/common/http';
import {GenericCrudService} from '../../service/GenericCrud.service';
import {backendUrl} from '../../constants/backendUrl';

@Component({
  selector: 'app-category-dropdown',
  templateUrl: './category-dropdown.component.html',
  styleUrls: ['./category-dropdown.component.css'],
})
export class CategoryDropdownComponent implements OnInit {
  @Input() selectedCategoryIdControl: FormControl;

  categories: Category[] = []
  categoryService: GenericCrudService<Category>;

  constructor(private http: HttpClient) {
    this.categoryService = new GenericCrudService<Category>(backendUrl.CATEGORIES, http);
  }

  ngOnInit(): void {
    this.categoryService.getAll().subscribe({
          next: (res) => {
            this.categories = res;
          },
        },
    )
  }
}
