import {Routes} from '@angular/router';
import {paths} from './constants/paths';
import {HomeComponent} from './page/home/home.component';
import {LoginComponent} from './page/login/login.component';
import {AuthGuard} from './service/auth-guard';
import {RoleEnum} from './model/role';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: paths.LOGIN
  },
  {
    path: paths.LOGIN,
    pathMatch: 'full',
    component: LoginComponent
  },
  {
    path: paths.HOME,
    pathMatch: 'full',
    component: HomeComponent,
    canActivate: [AuthGuard],
    data: { role: [RoleEnum.admin, RoleEnum.user] }
  },
  { path: '**', redirectTo: paths.LOGIN }
];
