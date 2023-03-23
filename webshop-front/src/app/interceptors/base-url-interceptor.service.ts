import {Inject, Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment.development';
import {AppService} from '../app.service';

@Injectable({
  providedIn: 'root'
})
export class BaseUrlInterceptorService implements HttpInterceptor {
  private readonly baseUrl: string;

  constructor(private appService: AppService) {
    this.baseUrl = environment.apiUrl
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let apiReq = null;
    if (this.appService.token)
      apiReq = request.clone({
        url: `${this.baseUrl}/${request.url}`,
        headers: request.headers.set('Authorization', this.appService.token!)
      });
    else {
      apiReq = request.clone({
        url: `${this.baseUrl}/${request.url}`,
      });
    }
    return next.handle(apiReq);
  }
}
