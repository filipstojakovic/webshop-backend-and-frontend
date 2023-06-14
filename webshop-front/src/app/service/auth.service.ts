import {Injectable} from '@angular/core';
import tokenService from './TokenService';
import {HttpClient} from '@angular/common/http';
import {ToastService} from 'angular-toastify';
import {Router} from '@angular/router';
import {LoginEmitterService} from './login-emitter.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private token: string | null = null;

  constructor(private http: HttpClient,
              private toastService: ToastService,
              private router: Router,
              private loginEmiterService: LoginEmitterService
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
                this.loginEmiterService.loggedInEvent.emit();
                return callback && callback();
              },
              error: () => {
                this.toastService.error("Error signing in");
              },
            },
        );
  }

  logout() {
    tokenService.removeTokenFromStorage();
    this.router.navigateByUrl('/login');
  }

}
