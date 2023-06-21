import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
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
  @Output() categoryChangeEvent = new EventEmitter();


  categories: Category[] = []
  categoryService: GenericCrudService<Category, Category>;

  constructor(private http: HttpClient) {
    this.categoryService = new GenericCrudService<Category, Category>(backendUrl.CATEGORIES, http);
  }

  ngOnInit(): void {
    this.categoryService.getAll().subscribe({
          next: (res) => {
            this.categories = res;
          },
        },
    )
  }

  onSelectionChange(event: any) {
    const categoryId = event.value;
    const category = this.categories.find(x => x.id === categoryId);
    this.categoryChangeEvent.emit(category);

  }
}
