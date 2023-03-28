import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {LoginService} from '../login/login.service';
import {Router} from '@angular/router';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {ToastService} from 'angular-toastify';
import tokenService from '../../service/TokenService';
import {paths} from '../../constants/paths';
import {SignupRequest} from '../../model/request/signupRequest';
import {ConfirmedValidator} from './confirmed.validator';
import {backendUrl} from '../../constants/backendUrl';

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
      private loginService: LoginService,
      private router: Router,
      private http: HttpClient,
      private toastService: ToastService,
  ) {
  }

  ngOnInit(): void {
    tokenService.removeTokenFromStorage();
    this.form = this.fb.group({
      username: ['a', Validators.required],
      password: ['a', Validators.required],
      confirm_password: ['a', Validators.required],

      firstName: ['a', Validators.required],
      lastName: ['a', Validators.required],
      email: ['a@a', Validators.email],
      city: ['a', Validators.required],
      avatar: null,
    }, {
      validators: ConfirmedValidator('password', 'confirm_password'),
    });
  }

  onSubmit() {
    if (!this.form.valid) {
      this.toastService.error("Form not valid")
    }

    if (this.form.get("password") === this.form.get("repassword")) {
      this.toastService.error("Passwords do not match");
    }

    const formData: FormData = new FormData();
    formData.append("file", this.imageFile!);

    const signUpRequest: SignupRequest = this.form.value;
    console.log("registration.component.ts > onSubmit(): " + JSON.stringify(signUpRequest, null, 2));
    this.http.post(backendUrl.REGISTER, formData).subscribe({
        next: (res) => {
            console.log("registration.component.ts > next(): "+ JSON.stringify(res, null, 2));
        },
        error: (err) => {
          console.log("registration.component.ts > error(): "+ JSON.stringify(err, null, 2));
        }
     }
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
