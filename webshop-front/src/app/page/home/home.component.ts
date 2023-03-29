import {Component} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {GenericCrudService} from '../../service/GenericCrudService';
import {User} from '../../model/user';
import {AuthService} from '../../service/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  currentUser: any;
  users: User[] = [];
  public userService: GenericCrudService<User>;
  public JSON: any;
  filePath: string = "C:\\Users\\filip\\IdeaProjects\\webshop-backend\\images\\avatars\\a-maja.jpg";

  constructor(private loginService: AuthService, private http: HttpClient) {
    this.userService = new GenericCrudService<User>("users", http);
    this.JSON = JSON;
    http.get<any>('user/me').subscribe(res => this.currentUser = JSON.stringify(res));
    this.userService.getAll().subscribe(res => this.users = res);
  }

  authenticated() {
    return this.loginService.getToken() !== null;
  }

  getPath(user: User) {
    const path = user.avatar;
    return path.replace("\\","\\");
  }
}
