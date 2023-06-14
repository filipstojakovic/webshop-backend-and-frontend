import {Component, OnInit} from '@angular/core';
import {User} from '../../model/User';
import {UserService} from '../../service/user.service';
import tokenService from '../../service/TokenService';
import {ToastService} from 'angular-toastify';
import {Location} from '@angular/common';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css'],
})
export class ChangePasswordComponent implements OnInit {

  currentUser: User;
  form: FormGroup;

  constructor(private userService: UserService,
              private toastService: ToastService,
              private location: Location,
              private fb: FormBuilder,
  ) {
  }

  ngOnInit(): void {
    const userId = tokenService.getIdFromToken();
    this.form = this.fb.group({
      username: ["", Validators.required],
      oldPassword: ["", Validators.required],
      newPassword: ["", Validators.required],
      newPassword2: ["", Validators.required],
    })
    this.userService.getById(userId).subscribe({
          next: (res) => {
            this.currentUser = res;
            this.form.controls['username'].setValue(res.username);
          },
          error: (err) => {
            this.toastService.error("Unable to fetch user");
          },
        },
    )
  }


  goBackClicked() {
    this.location.back();
  }

  onSubmit() {
    if (this.form.invalid) {
      this.toastService.error("Form is not valid!")
      return;
    }
    const { oldPassword, newPassword, newPassword2 } = this.form.value;
    if (newPassword !== newPassword2) {
      this.toastService.error("Confirm password does not match!")
      return;
    }
    this.userService.changePassword({ oldPassword, newPassword }).subscribe({
          next: (res) => {
            this.toastService.success("Password changed successfully");
            this.goBackClicked();
          },
          error: (err) => {
            this.toastService.error(err.error);
          },
        },
    )
  }

  protected readonly JSON = JSON;
}
