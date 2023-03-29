import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment.development';
import {AuthService} from '../service/auth.service';

@Injectable({
  providedIn: 'root'
})
export class BaseUrlInterceptorService implements HttpInterceptor {
  private readonly baseUrl: string;

  constructor(private authService: AuthService) {
    this.baseUrl = environment.apiUrl
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let apiReq = null;
    if (this.authService.getToken())
      apiReq = request.clone({
        url: `${this.baseUrl}/${request.url}`,
        headers: request.headers.set('Authorization', this.authService.getToken()!)
      });
    else {
      apiReq = request.clone({
        url: `${this.baseUrl}/${request.url}`,
      });
    }
    return next.handle(apiReq);
  }
}
