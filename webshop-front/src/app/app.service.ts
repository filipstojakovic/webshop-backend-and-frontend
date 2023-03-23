import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AppService {

  public authenticated = false;
  public token: string | null = null;

  constructor(private http: HttpClient) {
  }

  authenticate(credentials: any, callback: any) {
    this.http.post<any>('login', credentials, { observe: "response" })
        .subscribe((response) => {
          const authorizationValue = response.headers.get('Authorization');
          this.token = authorizationValue;
          console.log("app.service.ts > authenticate() token: " + authorizationValue);
          if (authorizationValue) {
            this.authenticated = true;
          } else {
            this.authenticated = false;
          }
          return callback && callback();
        });

  }
}
