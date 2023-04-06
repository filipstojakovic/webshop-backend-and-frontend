import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {paths} from '../../constants/paths';
import {ToastService} from 'angular-toastify';
import tokenService from '../../service/TokenService';
import {AuthService} from '../../service/auth.service';

type LoginData = {
  username: string;
  password: string;
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  protected readonly paths = paths;
  form!: FormGroup;

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
    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  onSubmit() {
    if (!this.form.valid) {
      this.form.markAllAsTouched();
      return;
    }
    try {
      this.authService.login(this.form.value as LoginData, () => {
        this.router.navigateByUrl(paths.PRODUCTS, { replaceUrl: true });
      });
    } catch (error) {
      this.toastService.error('Bad credentials');
    } finally {
    }
  }

  onKeyDown(event: KeyboardEvent) {
    if (event?.key === 'Enter') {
      this.onSubmit();
    }
  }

}
