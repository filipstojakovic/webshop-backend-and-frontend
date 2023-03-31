import {Component, OnInit} from '@angular/core';
import tokenService from '../../service/TokenService';
import {HttpClient} from '@angular/common/http';
import {AuthService} from '../../service/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  image: any;

  constructor(private http: HttpClient, private auth:AuthService) {
  }

  ngOnInit(): void {
    const id = tokenService.getIdFromToken();
    this.http.get(`users/${id}/image`, { responseType: 'text' }).subscribe({
          next: (res) => {
            this.image = res;
          },
          error: (err) => {
            console.log("home.component.ts > error(): " + JSON.stringify(err, null, 2));
          },
        },
    )
  }

  isLoggedIn(): boolean {
    const token = tokenService.getTokenFromStorage();
    return token != null && token != "";
  }

  logoutClick() {
    this.auth.logout();
  }
}
