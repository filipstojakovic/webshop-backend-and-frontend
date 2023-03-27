import {Injectable} from '@angular/core';
import tokenService from '../../service/TokenService';
import {HttpClient} from '@angular/common/http';
import {ToastService} from 'angular-toastify';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private token: string | null = null;

  constructor(private http: HttpClient,
              private toastService: ToastService,
              private router: Router
  ) {
    this.token = this.getToken();
  }

  getToken() {
    return tokenService.getTokenFromStorage();
  }

  login(credentials: any, callback: any) {
    this.http.post<any>('login', credentials, { observe: "response" })
        .subscribe({
              next: (res) => {
                this.token = res.headers.get('Authorization');
                if (this.token == null || this.token === "")
                  this.logout();

                tokenService.setTokenInStorage(this.token!);
                return callback && callback();
              },
              error: () => {
                this.toastService.error("Error signing in");
              }
            }
        );
  }

  logout() {
    tokenService.removeTokenFromStorage();
    this.router.navigateByUrl('/login');
  }

}
