import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import jwt_decode from "jwt-decode";
import {ToastService} from 'angular-toastify';

@Injectable({
  providedIn: 'root'
})
export class AppService {

  public authenticated = false;
  public token: string | null = null;

  constructor(private http: HttpClient, private toastService: ToastService) {
  }

  authenticate(credentials: any, callback: any) {
    this.http.post<any>('login', credentials, { observe: "response" })
        .subscribe({
              next: (res) => {
                const authorizationValue = res.headers.get('Authorization');
                this.token = authorizationValue;
                console.log("app.service.ts > authenticate() token: " + authorizationValue);
                if (authorizationValue) {
                  const decoded = jwt_decode(authorizationValue);
                  console.log("app.service.ts > decode token(): " + JSON.stringify(decoded, null, 2));
                  this.authenticated = true;
                } else {
                  this.authenticated = false;
                }
                return callback && callback();
              },
              error: (err) => {
                this.toastService.error("Error signing in");
              }
            }
        );
  }
}
