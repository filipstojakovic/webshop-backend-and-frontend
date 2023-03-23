import {Component} from '@angular/core';
import {AppService} from '../app.service';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  currentUser: any;
  users: any[] = [];

  constructor(private app: AppService, private http: HttpClient) {
    http.get<any>('user/me').subscribe(res => this.currentUser = JSON.stringify(res));
    http.get<any[]>('users').subscribe(res => this.users = res);
  }

  authenticated() {
    return this.app.authenticated;
  }
}
