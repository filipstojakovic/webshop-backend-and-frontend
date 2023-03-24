import {Component, OnInit} from '@angular/core';
import {AppService} from '../app.service';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {paths} from '../constants/paths';
import {ToastService} from 'angular-toastify';
import tokenService from '../service/TokenService';

type LoginData = {
  username: string;
  password: string;
}

@Component({
  selector: 'appService-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{

  form!: FormGroup;

  constructor(
      private fb: FormBuilder,
      private authService: AppService,
      private router: Router,
      private appService: AppService,
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

  async onSubmit() {
    if (this.form.valid) {
      try {
        await this.authService.authenticate(this.form.value as LoginData, () => {
          this.router.navigateByUrl(paths.HOME, { replaceUrl: true });
        });
      } catch (error) {
        this.toastService.error('Bad credentials');
      } finally {
      }
    }
  }

  onKeyDown(event: KeyboardEvent) {
    if (event.key === 'Enter') {
      this.onSubmit();
    }
  }
}
