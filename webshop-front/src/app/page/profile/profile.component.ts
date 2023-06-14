import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from '../../model/User';
import tokenService from '../../service/TokenService';
import {ToastService} from 'angular-toastify';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {paths} from '../../constants/paths';
import {Router} from '@angular/router';
import {UserService} from '../../service/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
})
export class ProfileComponent implements OnInit {

  currentUser: User;
  form!: FormGroup;
  isReadOnly = true;
  tmpFormValue: any;

  protected readonly paths = paths;

  constructor(private http: HttpClient,
              private toastService: ToastService,
              private userService: UserService,
              private fb: FormBuilder,
              private router: Router,
  ) {
  }

  ngOnInit(): void {
    const userId = tokenService.getIdFromToken();
    this.userService.getById(userId).subscribe({
          next: (res) => {
            this.currentUser = res;
            this.form = this.fb.group({
              username: this.currentUser.username,
              email: [this.currentUser.email, [Validators.required, Validators.email]],
              firstName: [this.currentUser.firstName, Validators.required],
              lastName: [this.currentUser.lastName, Validators.required],
              city: [this.currentUser.city, Validators.required],
            });
            this.tmpFormValue = this.form.value;
            console.log("profile.component.ts > next(): " + JSON.stringify(this.currentUser, null, 2));
          },
          error: (err) => {
            this.toastService.error("Unable to find user");
          },
        },
    )
  }

  changeEditMode() {

    this.isReadOnly = !this.isReadOnly;
    if (!this.isReadOnly) {
      this.tmpFormValue = this.form.value;
    } else {
      this.form.patchValue(this.tmpFormValue);
    }
  }


  changePasswordClick() {
    this.router.navigateByUrl(paths.CHANGE_PASSWORD);
  }

  saveChanges() {
    if (this.form.invalid)
      return;

    const newUserRequest = this.form.value;
    const userId = tokenService.getIdFromToken();
    this.userService.update(userId, newUserRequest).subscribe({
          next: (res) => {
            this.toastService.success("User update successfully!")
          },
          error: (err) => {
            this.toastService.error("Error user update!")
          },
        },
    )

    this.isReadOnly = true;
  }
}
