import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {GenericCrudService} from '../../service/GenericCrudService';
import {User} from '../../model/user';
import {AuthService} from '../../service/auth.service';
import {backendUrl} from '../../constants/backendUrl';
import tokenService from '../../service/TokenService';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  users: User[] = [];
  public userService: GenericCrudService<User>;
  public JSON: any;
  image!:string;

  constructor(private authService: AuthService, private http: HttpClient) {
    this.userService = new GenericCrudService<User>(backendUrl.USERS, http);
    this.JSON = JSON;
    this.userService.getAll().subscribe({
          next: (res) => {
            this.users = res;
          },
          error: (err) => {

          },
        },
    );
  }

  ngOnInit(): void {
    const id = tokenService.getIdFromToken();
    this.http.get(`users/${id}/image`,{ responseType: 'text' }).subscribe({
          next: (res) => {
            this.image = res;
          },
          error: (err) => {
            console.log("home.component.ts > error(): "+ JSON.stringify(err, null, 2));
          },
        },
    )
  }

  authenticated() {
    return this.authService.getToken() !== null;
  }

  getPath(user: User) {
    const path = user.avatar;
    return path.replace("\\", "\\");
  }


}
