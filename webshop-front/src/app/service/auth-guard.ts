import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {paths} from '../constants/paths';
import {LoginService} from '../page/login/login.service';
import tokenService from './TokenService';
import {RoleEnum} from '../model/role';
import {HttpClient} from '@angular/common/http';
import {backendUrl} from '../constants/backendUrl';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private loginService: LoginService, private router: Router, private http: HttpClient) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): any {
    const routeRoles: RoleEnum[] = route.data['role'];
    const token = tokenService.getTokenFromStorage();
    //TODO: send request to check if token is valid;

    // Check if user is authenticated
    if (token) {
      this.checkIsTokenValid();
      return true;
    }

    console.log("auth-guard.ts > canActivate(): " + "cant activate");
    // Redirect to login page
    this.router.navigateByUrl(paths.LOGIN)
    return false;
  }

  checkIsTokenValid() {
    const token = tokenService.getTokenFromStorage();
    if (token)
      this.http.get(backendUrl.WHOAMI, {observe: 'response'})
          .subscribe({
                next: (res) => {
                  console.log("auth-guard.ts > next(): " + JSON.stringify(res, null, 2));
                  console.log(res.status)

                },
                error: (err) => {
                  console.log("auth-guard.ts > error(): " + JSON.stringify(err, null, 2));
                  this.loginService.logout();
                }
              }
          )
  }

}
