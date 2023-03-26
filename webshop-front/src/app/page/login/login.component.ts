import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {paths} from '../../constants/paths';
import {ToastService} from 'angular-toastify';
import tokenService from '../../service/TokenService';
import {LoginService} from './login.service';

type LoginData = {
  username: string;
  password: string;
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form!: FormGroup;

  constructor(
      private fb: FormBuilder,
      private loginService:LoginService,
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
    if (this.form.valid) {
      try {
        this.loginService.login(this.form.value as LoginData, () => {
          this.router.navigateByUrl(paths.HOME, { replaceUrl: true });
        });
      } catch (error) {
        this.toastService.error('Bad credentials');
      } finally {
      }
    }
  }

  onKeyDown(event: KeyboardEvent) {
    if (event?.key === 'Enter') {
      this.onSubmit();
    }
  }
}
