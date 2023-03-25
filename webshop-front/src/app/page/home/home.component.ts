import {Component} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {GenericCrudService} from '../../service/GenericCrudService';
import {User} from '../../model/user';
import {LoginService} from '../login/login.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  currentUser: any;
  users: any[] = [];
  public userService: GenericCrudService<User>;
  public JSON: any;

  constructor(private loginService: LoginService, private http: HttpClient) {
    this.userService = new GenericCrudService<User>("users", http);
    this.JSON = JSON;
    http.get<any>('user/me').subscribe(res => this.currentUser = JSON.stringify(res));
    this.userService.getAll().subscribe(res => this.users = res);
  }

  authenticated() {
    return this.loginService.getToken() !== null;
  }
}
