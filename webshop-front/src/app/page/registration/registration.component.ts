import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {LoginService} from '../login/login.service';
import {Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {ToastService} from 'angular-toastify';
import tokenService from '../../service/TokenService';
import {paths} from '../../constants/paths';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  form!: FormGroup;

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
      username: ['', Validators.required],
      password: ['', Validators.required],
      rePassword: ['', Validators.required],
    });
  }

  onSubmit() {
    if (this.form.valid) {
      try {
      } catch (error) {
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
