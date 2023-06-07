import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {GenericCrudService} from '../../service/GenericCrud.service';
import {Category} from '../../model/Category';
import {backendUrl} from '../../constants/backendUrl';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../service/auth.service';
import {Router} from '@angular/router';
import {ToastService} from 'angular-toastify';
import {map, Observable, startWith} from 'rxjs';
import {MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';
import {Attribute} from '../../model/Attribute';
import myUtils from '../../utils/myUtils';
import {AttributeNameValue} from '../../model/request/AttributeNameValue';

@Component({
  selector: 'app-sell-product',
  templateUrl: './sell-product.component.html',
  styleUrls: ['./sell-product.component.css'],
})
export class SellProductComponent implements OnInit {

  form!: FormGroup;
  attributeForm!: FormGroup | null;
  files: File[] = [];

  categories: Category[] = [];
  filteredOptions!: Observable<Category[]>;
  attributesValue: AttributeNameValue[] = [];

  categoryService!: GenericCrudService<Category>

  constructor(private fb: FormBuilder,
              private authService: AuthService,
              private router: Router,
              private http: HttpClient,
              private toastService: ToastService) {
    this.categoryService = new GenericCrudService<Category>(backendUrl.CATEGORIES, http);
  }

  ngOnInit(): void {
    var defaultFieldValue = "a"; // TODO: remove when not needed
    this.form = this.fb.group({
      categorySearch: [new FormControl<Category | null>(null), Validators.required],
      attributes: [],
      name: [defaultFieldValue, Validators.required],
      description: [defaultFieldValue],
      price: [null, Validators.required],
      location: [defaultFieldValue, Validators.required],
      isNew: true,
      images: [],
    });
    this.categoryService.getAll().subscribe({
          next: (res) => {
            this.categories = res;
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

  ///////////////////////////// ON SUBMIT ////////////////////////////////////////////////////////////////////////////////
  async onSubmit() {
    if (!this.form.valid) {
      this.toastService.error("Form not valid")
      return;
    }

    const request = this.form.value;

    request.category.attributes = Object.keys(this.attributeForm!.controls)
        .map(key => {
          const value = this.attributeForm!.controls[key].value;
          return new AttributeNameValue(key, value);
        });

    const imagePromises = this.files.map(f => myUtils.fileToBase64(f));
    await Promise.all(imagePromises).then((values) => request.images = values);

    console.log("sell-product.component.ts > onSubmit() request: " + JSON.stringify(request, null, 2));
    this.http.post(backendUrl.PRODUCTS, request).subscribe({
          next: (res) => {
            this.toastService.success("Product created successfully!");

            //TODO: clear form or navigate home
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
    const selectedCategory: Category = event.option.value;
    this.form.patchValue({ category: selectedCategory });
    this.attributesValue = selectedCategory.attributes.map(attribute => ({
      ...attribute,
      value: '',
    }));
    this.createAttributeForm(this.attributesValue);
  }

  createAttributeForm(attributesValue: AttributeNameValue[]) {
    this.attributeForm = this.fb.group({});
    attributesValue.map(attributeValue => {
      this.attributeForm?.addControl(attributeValue.name, new FormControl<Attribute | null>(null))
    })
  }

  onImageSelect(event: any) {
    this.files.push(...event.addedFiles);
  }

  onImageRemove(event: any) {
    this.files.splice(this.files.indexOf(event), 1);
  }

}
