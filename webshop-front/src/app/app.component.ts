import {Component} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AppService} from './app.service';
import {Router} from '@angular/router';
import {finalize} from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(private app: AppService, private http: HttpClient, private router: Router) {
    this.app.authenticate(undefined, undefined);
  }

  logout() {
    this.http.post('logout', {}).pipe(
        finalize(() => {
          this.app.authenticated = false;
          this.router.navigateByUrl('/login');
        })).subscribe();
  }
}
