import {Component, OnInit} from '@angular/core';
import tokenService from '../../service/TokenService';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  image: any;

  constructor(private http: HttpClient) {
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

}
