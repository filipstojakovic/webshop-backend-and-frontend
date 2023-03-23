import {Component} from '@angular/core';
import {AppService} from '../app.service';
import {HttpClient} from '@angular/common/http';
import {GenericCrudService} from '../service/GenericCrudService';
import {User} from '../model/user';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  currentUser: any;
  users: any[] = [];
  public userService: GenericCrudService<User>;
  public JSON:any;

  constructor(private app: AppService, private http: HttpClient) {
    this.userService = new GenericCrudService<User>("users", http);
    this.JSON = JSON;
    http.get<any>('user/me').subscribe(res => this.currentUser = JSON.stringify(res));
    this.userService.getAll().subscribe(res => this.users = res);
  }

  authenticated() {
    return this.app.authenticated;
  }
}
