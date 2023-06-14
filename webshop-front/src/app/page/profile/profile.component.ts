import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {GenericCrudService} from '../../service/GenericCrud.service';
import {User} from '../../model/User';
import {backendUrl} from '../../constants/backendUrl';
import tokenService from '../../service/TokenService';
import {ToastService} from 'angular-toastify';
import {FormBuilder, FormGroup} from '@angular/forms';
import {UserRequest} from '../../model/UserRequest';
import {paths} from '../../constants/paths';
import {Router} from '@angular/router';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
})
export class ProfileComponent implements OnInit {

  currentUser: User;
  form!: FormGroup;
  isReadOnly = true;

  userService: GenericCrudService<UserRequest, User>

  constructor(private http: HttpClient, private toastService: ToastService, private fb: FormBuilder, private router: Router) {
    this.userService = new GenericCrudService<UserRequest, User>(backendUrl.USERS, http);
  }

  ngOnInit(): void {
    const userId = tokenService.getIdFromToken();
    this.userService.getById(userId).subscribe({
          next: (res) => {
            this.currentUser = res;
            this.form = this.fb.group({
              username: this.currentUser.username,
              email: this.currentUser.email,
              firstName: this.currentUser.firstName,
              lastName: this.currentUser.lastName,
              city: this.currentUser.city,
            });
            console.log("profile.component.ts > next(): " + JSON.stringify(this.currentUser, null, 2));
          },
          error: (err) => {
            this.toastService.error("Unable to find user");
          },
        },
    )
  }

  editMode() {
    this.isReadOnly = false;
  }

  protected readonly paths = paths;

  changePasswordClick() {
    this.router.navigateByUrl(paths.CHANGE_PASSWORD);
  }
}
