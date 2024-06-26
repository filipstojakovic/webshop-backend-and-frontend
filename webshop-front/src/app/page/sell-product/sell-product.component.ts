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
import {paths} from '../../constants/paths';

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

  categoryService!: GenericCrudService<Category, Category>

  constructor(private fb: FormBuilder,
              private authService: AuthService,
              private router: Router,
              private http: HttpClient,
              private toastService: ToastService) {
    this.categoryService = new GenericCrudService<Category, Category>(backendUrl.CATEGORIES, http);
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      category: [new FormControl<Category | null>(null), Validators.required],
      attributes: [],
      name: ["", Validators.required],
      description: [""],
      price: [null, Validators.required],
      location: ["", Validators.required],
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

    this.http.post(backendUrl.PRODUCTS, request).subscribe({
          next: (res) => {
            this.form.reset();
            this.toastService.success("Product created successfully!");
            this.router.navigateByUrl(paths.PRODUCTS);
          },
          error: (err) => {
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
