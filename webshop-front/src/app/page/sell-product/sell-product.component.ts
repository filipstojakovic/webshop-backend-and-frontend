import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {GenericCrudService} from '../../service/GenericCrudService';
import {Category} from '../../model/category';
import {backendUrl} from '../../constants/backendUrl';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../service/auth.service';
import {Router} from '@angular/router';
import {ToastService} from 'angular-toastify';
import formUtils from '../../utils/formUtils';
import {map, Observable, startWith} from 'rxjs';
import {MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';
import {Attribute} from '../../model/attribute';

@Component({
  selector: 'app-sell-product',
  templateUrl: './sell-product.component.html',
  styleUrls: ['./sell-product.component.css'],
})
export class SellProductComponent implements OnInit {

  form!: FormGroup;
  imageFile: File | null = null;

  categories: Category[] = [];
  filteredOptions!: Observable<Category[]>;

  categoryService!: GenericCrudService<Category>

  constructor(private fb: FormBuilder,
              private authService: AuthService,
              private router: Router,
              private http: HttpClient,
              private toastService: ToastService) {
    this.categoryService = new GenericCrudService<Category>(backendUrl.CATEGORIES, http);
  }

  ngOnInit(): void {
    var defaultFieldValue = "a";
    this.form = this.fb.group({
      category: [null, Validators.required],
      name: [defaultFieldValue, Validators.required],
      description: [defaultFieldValue],
      price: [null, Validators.required],
      location: [defaultFieldValue, Validators.required],
      isNew: true,
      image: null,
    });
    this.categoryService.getAll().subscribe({
          next: (res) => {
            this.categories = res;
            console.log("sell-product.component.ts > next(): "+ JSON.stringify(res, null, 2));
            this.filteredOptions = this.form.controls['category'].valueChanges.pipe(
                startWith(''),
                map(value => this.filterCategories(value?.name || '')),
            );
          },
          error: (err) => {
            this.toastService.error("Unable to get categories");
          },
        },
    )
  }

  onSubmit() {
    if (!this.form.valid) {
      this.toastService.error("Form not valid")
      return;
    }

    //MISSING ATTRIBUTES VALUE
    this.http.post(backendUrl.PRODUCTS, this.form.value).subscribe({
          next: (res) => {
            this.toastService.success("Product created successfully!");


            //TODO: clear form
            // this.clearForm();
          },
          error: (err) => {
            console.log("registration.component.ts > error(): " + JSON.stringify(err, null, 2));
            this.toastService.error("There was an error with form submit");
          },
        },
    );
  }

  displayCategoryName(category: any): string {
    return category ? category.name : '';
  }


  filterCategories(value: string): Category[] {
    const filterValue = value.toLowerCase();
    return this.categories.filter(category => category.name.toLowerCase().includes(filterValue));
  }

  optionSelected(event: MatAutocompleteSelectedEvent): void {
    const selectedCategory = event.option.value;
    this.form.patchValue({ category: selectedCategory });
  }

  onSelect(event: any) {
    this.imageFile = event.addedFiles[0];
    const reader = new FileReader();
    reader.readAsDataURL(this.imageFile!)
    reader.onload = (event: any) => {
      const imageText = event.target.result;
      this.form.controls['image'].setValue(imageText);
    }
  }

  onRemove() {
    this.imageFile = null;
  }

  clearForm() {
    this.form.reset();
    formUtils.clearFormErrors(this.form);
  }

}
