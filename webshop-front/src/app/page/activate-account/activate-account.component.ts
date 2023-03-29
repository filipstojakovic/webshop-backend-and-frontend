import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../service/auth.service";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {ToastService} from "angular-toastify";
import tokenService from "../../service/TokenService";
import {constant, tokenConstant} from "../../constants/constants";
import {paths} from "../../constants/paths";
import {backendUrl} from '../../constants/backendUrl';

@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrls: ['./activate-account.component.css'],
})
export class ActivateAccountComponent implements OnInit {

  form!: FormGroup;

  constructor(
      private fb: FormBuilder,
      private loginService: AuthService,
      private router: Router,
      private http: HttpClient,
      private toastService: ToastService,
  ) {
  }

  ngOnInit(): void {
    const username = tokenService.getFieldFromToken(tokenConstant.USERNAME);
    this.form = this.fb.group({
      username: [username, Validators.required],
      pin: ['', [
        Validators.required,
        Validators.minLength(constant.PIN_DIGIT_NUM),
        Validators.maxLength(constant.PIN_DIGIT_NUM),
        this.fourDigitValidator],
      ],
    })
    ;
  }

  fourDigitValidator(control: any) {
    const value = control.value;

    if (value === null || value === undefined || value === '') {
      // don't validate empty fields
      return null;
    }

    if (/^\d{4}$/.test(value)) {
      return null;
    } else {
      return { 'invalidFourDigit': true };
    }
  }

  onSubmit() {
    if (!this.form.valid) {
      this.toastService.error(JSON.stringify(this.form.errors));
      return;
    }

    this.http.post(backendUrl.PIN,this.form.value).subscribe({
        next: (res) => {
          //TODO: get new token becouse activate is still 0
          this.router.navigateByUrl(paths.HOME, { replaceUrl: true });
        },
        error: (err) => {
            console.log("activate-account.component.ts > error(): "+ JSON.stringify(err, null, 2));
        }
     }
           )
    //TODO: if pin valid navigate to home

    // if (this.form.valid) {
    //   try {
    //     this.loginService.login(this.form.value, () => {
    //       this.router.navigateByUrl(paths.HOME, { replaceUrl: true });
    //     });
    //   } catch (error) {
    //     this.toastService.error('Bad credentials');
    //   } finally {
    //   }
    // }
  }

  backToLogin() {
    this.router.navigateByUrl(paths.LOGIN, { replaceUrl: true });
  }

  onKeyDown(event: KeyboardEvent) {
    if (event?.key === 'Enter') {
      this.onSubmit();
    }
  }
}
