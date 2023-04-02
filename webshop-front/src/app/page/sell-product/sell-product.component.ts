import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {GenericCrudService} from '../../service/GenericCrudService';
import {Category} from '../../model/category';
import {backendUrl} from '../../constants/backendUrl';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../service/auth.service';
import {Router} from '@angular/router';
import {ToastService} from 'angular-toastify';
import myUtils from '../../utils/myUtils';
import {map, Observable, startWith} from 'rxjs';
import {MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';

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
      username: [defaultFieldValue, Validators.required],
      category: [null],
      avatar: null,
    });
    this.categoryService.getAll().subscribe({
          next: (res) => {
            console.log("sell-product.component.ts > next(): " + JSON.stringify(res, null, 2));
            this.categories = res;
            this.filteredOptions = this.form.controls['category'].valueChanges.pipe(
                startWith(''),
                map(value => this.filterCategories(value.name || '')),
            );
          },
          error: (err) => {

          },
        },
    )
  }

  displayCategoryName(category: any): string {
    console.log("sell-product.component.ts > displayCategoryName(): "+ JSON.stringify(category, null, 2));
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

  onSubmit() {
    console.log("sell-product.component.ts > onSubmit(): " + JSON.stringify(this.form.value, null, 2));
    if (!this.form.valid) {
      this.toastService.error("Form not valid")
      return;
    }

    const formData = myUtils.formGroupToFormDataConverter(this.form);
    formData.append("avatar", this.imageFile!);

    this.http.post(backendUrl.REGISTER, formData).subscribe({
          next: (res) => {
            console.log("registration.component.ts > next(): " + JSON.stringify(res, null, 2));
            this.toastService.success("Account created successfully!");

            //clear form
          },
          error: (err) => {
            console.log("registration.component.ts > error(): " + JSON.stringify(err, null, 2));
            this.toastService.error("There was an error with form submit");
          },
        },
    );
  }

  onSelect(event: any) {
    this.imageFile = event.addedFiles[0];
    const reader = new FileReader();
    reader.readAsDataURL(this.imageFile!)
    reader.onload = (event: any) => {
      const imageText = event.target.result;
      this.form.controls['avatar'].setValue(imageText);
    }
  }

  onRemove() {
    this.imageFile = null;
  }


  clearForm() {
    this.form.reset();
    myUtils.clearFormErrors(this.form);
  }

}
