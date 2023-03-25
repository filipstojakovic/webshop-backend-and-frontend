import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment.development';
import {LoginService} from '../page/login/login.service';

@Injectable({
  providedIn: 'root'
})
export class BaseUrlInterceptorService implements HttpInterceptor {
  private readonly baseUrl: string;

  constructor(private loginService: LoginService) {
    this.baseUrl = environment.apiUrl
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let apiReq = null;
    if (this.loginService.getToken())
      apiReq = request.clone({
        url: `${this.baseUrl}/${request.url}`,
        headers: request.headers.set('Authorization', this.loginService.getToken()!)
      });
    else {
      apiReq = request.clone({
        url: `${this.baseUrl}/${request.url}`,
      });
    }
    return next.handle(apiReq);
  }
}
