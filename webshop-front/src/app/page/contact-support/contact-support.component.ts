import {Component, OnInit} from '@angular/core';
import {paths} from '../../constants/paths';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import tokenService from '../../service/TokenService';
import {AuthService} from '../../service/auth.service';
import {Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {ToastService} from 'angular-toastify';
import {GenericCrudService} from '../../service/GenericCrud.service';
import formUtils from '../../utils/formUtils';

@Component({
  selector: 'app-contact-support',
  templateUrl: './contact-support.component.html',
  styleUrls: ['./contact-support.component.css'],
})
export class ContactSupportComponent implements OnInit {
  protected readonly paths = paths;
  form!: FormGroup;
  contactSupportService!: GenericCrudService<any>

  constructor(
      private fb: FormBuilder,
      private http: HttpClient,
      private toastService: ToastService,
  ) {
    this.contactSupportService = new GenericCrudService<any>(paths.CONTACT_SUPPORT, http);
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      title: ['', Validators.required],
      message: ['', Validators.required],
    });
  }

  clearClick() {
    this.form.reset();
    formUtils.clearFormErrors(this.form);
  }

  displayErrors() {
    const { touched, errors } = this.form.controls['message'];
    return (touched && errors) || !this.form.controls['message'].valid;
  }

  onSubmit() {
    if (!this.form.valid) {
      this.form.markAllAsTouched();
      return;
    }

    this.contactSupportService.create(this.form.value).subscribe({
          next: (res) => {
            this.toastService.success("Message sent!");
            this.clearClick();
          },
          error: (err) => {
            this.toastService.error("Unable to send message");
          },
        },
    )

  }
}
