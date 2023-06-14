import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
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

    this.http.post(backendUrl.PIN, this.form.value, { observe: "response" }).subscribe({
          next: (res) => {
            //TODO: maybe check res.statuscode
            const token = res.headers.get('Authorization');
            if (token == null || token === "") {
              this.toastService.info("New pin send via email");
              return;
            }

            tokenService.setTokenInStorage(token!);
            this.router.navigateByUrl(paths.PRODUCTS, { replaceUrl: true });
          },
          error: (err) => {
            console.log("activate-account.component.ts > error(): " + JSON.stringify(err, null, 2));
            this.toastService.error("Incorrect pin! Sending new pin")
          },
        },
    )
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
