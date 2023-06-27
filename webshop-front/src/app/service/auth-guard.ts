import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {paths} from '../constants/paths';
import {AuthService} from './auth.service';
import tokenService from './TokenService';
import {RoleEnum} from '../model/enums/role';
import {HttpClient} from '@angular/common/http';
import {backendUrl} from '../constants/backendUrl';
import {tokenConstant} from "../constants/constants";

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router, private http: HttpClient) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): any {

    const token = tokenService.getTokenFromStorage();
    if (token) {
      this.checkIsTokenValid();

      const isActive: boolean = tokenService.getFieldFromToken(tokenConstant.IS_ACTIVE);
      //if not activated redirect to activation page
      if (!isActive && state.url !== `/${paths.PIN}`){
        this.router.navigateByUrl(paths.PIN)
        return false;
      }
    }

    const isAllow: boolean|undefined = route.data['isAllow'];
    if(isAllow){
      return true;
    }

    const routeRoles: RoleEnum[] = route.data['role'];
    const tokenRole = tokenService.getFieldFromToken(tokenConstant.ROLE);
    const hasRole = routeRoles.some(role => role === tokenRole);

    if (!hasRole) {
      this.router.navigateByUrl(paths.LOGIN)
      return false;
    }

    return true;
  }

  checkIsTokenValid() {
    const token = tokenService.getTokenFromStorage();
    if (token)
      this.http.get(backendUrl.WHOAMI, { observe: 'response' })
          .subscribe({
                next: (res) => {

                },
                error: (err) => {
                  console.log("auth-guard.ts > error(): " + JSON.stringify(err, null, 2));
                  this.authService.logout();
                },
              },
          )
  }

}
