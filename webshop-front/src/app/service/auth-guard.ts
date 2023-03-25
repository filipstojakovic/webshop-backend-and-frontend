import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {paths} from '../constants/paths';
import {LoginService} from '../page/login/login.service';
import tokenService from './TokenService';
import {RoleEnum} from '../model/role';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private loginService: LoginService, private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot,
              state: RouterStateSnapshot): any {

    const routeRoles: RoleEnum[] = route.data['role'];
    const token = tokenService.getTokenFromStorage();
    //TODO: send request to check if token is valid;

    // Check if user is authenticated
    if (token) {
      //TODO: better token lo
      return true;
    }

    console.log("auth-guard.ts > canActivate(): "+ "cant activate");
    // Redirect to login page
    this.router.navigateByUrl(paths.LOGIN)
    return false;
  }
}
