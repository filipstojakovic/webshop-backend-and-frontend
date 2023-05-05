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


    return true; //TODO: remove me ////////////////////////



    const routeRoles: RoleEnum[] = route.data['role'];
    const token = tokenService.getTokenFromStorage();

    const tokenRole = tokenService.getFieldFromToken(tokenConstant.ROLE);
    const hasRole = routeRoles.some(role => role === tokenRole);

    if (!hasRole) {
      this.router.navigateByUrl(paths.ERROR);
      return false;
    }

    // Check if user is authenticated
    if (token) {
      //TODO: logout if not valid or unauthrorized.
      this.checkIsTokenValid();

      const isActive: boolean = tokenService.getFieldFromToken(tokenConstant.IS_ACTIVE);
      //if not activated redirect to activation page
      if (!isActive && state.url !== `/${paths.PIN}`)
        this.router.navigateByUrl(paths.PIN)

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
      this.http.get(backendUrl.WHOAMI, { observe: 'response' })
          .subscribe({
                next: (res) => {
                  console.log("auth-guard.ts > next(): " + res.status);

                },
                error: (err) => {
                  console.log("auth-guard.ts > error(): " + JSON.stringify(err, null, 2));
                  this.authService.logout();
                },
              },
          )
  }

}
