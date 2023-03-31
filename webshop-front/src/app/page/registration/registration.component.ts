import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../service/auth.service';
import {Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {ToastService} from 'angular-toastify';
import tokenService from '../../service/TokenService';
import {paths} from '../../constants/paths';
import {ConfirmedValidator} from './confirmed.validator';
import {backendUrl} from '../../constants/backendUrl';
import myUtils from "../../utils/myUtils";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css'],
})
export class RegistrationComponent implements OnInit {

  form!: FormGroup;
  imageFile: File | null = null;

  constructor(
      private fb: FormBuilder,
      private authService: AuthService,
      private router: Router,
      private http: HttpClient,
      private toastService: ToastService,
  ) {
  }

  ngOnInit(): void {
    tokenService.removeTokenFromStorage();
    const defaultFieldValue = "a"
    this.form = this.fb.group({
      username: [defaultFieldValue, Validators.required],
      password: [defaultFieldValue, Validators.required],
      confirm_password: [defaultFieldValue, Validators.required],

      firstName: [defaultFieldValue, Validators.required],
      lastName: [defaultFieldValue, Validators.required],
      email: ['filip.stojakovic1@gmail.com', Validators.email],
      city: [defaultFieldValue, Validators.required],
      avatar: null,
    }, {
      validators: ConfirmedValidator('password', 'confirm_password'),
    });
  }

  onSubmit() {
    if (!this.form.valid) {
      this.toastService.error("Form not valid")
      return;
    }

    if (this.form.get("password") === this.form.get("repassword")) {
      this.toastService.error("Passwords do not match");
      return;
    }

    const formData = myUtils.formGroupToFormDataConverter(this.form);
    formData.append("avatar", this.imageFile!);

    this.http.post(backendUrl.REGISTER, formData).subscribe({
          next: (res) => {
            console.log("registration.component.ts > next(): " + JSON.stringify(res, null, 2));
            this.navigateLogin(true);

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

  navigateLogin(replace = false) {
    this.router.navigateByUrl(paths.LOGIN, { replaceUrl: replace });
  }
}
