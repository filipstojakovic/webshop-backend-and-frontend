import {Routes} from '@angular/router';
import {paths} from './constants/paths';
import {LoginComponent} from './page/login/login.component';
import {RoleEnum} from './model/enums/role';
import {ActivateAccountComponent} from "./page/activate-account/activate-account.component";
import {RegistrationComponent} from './page/registration/registration.component';
import {ContactSupportComponent} from './page/contact-support/contact-support.component';
import {SellProductComponent} from './page/sell-product/sell-product.component';
import {ProductDetailsComponent} from './page/product/product-details/product-details.component';
import {ProductComponent} from './page/product/product.component';
import {AuthGuard} from './service/auth-guard';
import {ProfileComponent} from './page/profile/profile.component';
import {ChangePasswordComponent} from './page/change-password/change-password.component';
import {backendUrl} from './constants/backendUrl';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: paths.LOGIN,
  },
  {
    path: paths.LOGIN,
    pathMatch: 'full',
    component: LoginComponent,
  },
  {
    path: paths.REGISTER,
    pathMatch: 'full',
    component: RegistrationComponent,
  },
  {
    path: paths.PIN,
    pathMatch: 'full',
    component: ActivateAccountComponent,
  },
  {
    path: paths.PRODUCTS + "/:id",
    pathMatch: 'full',
    component: ProductDetailsComponent,
    canActivate: [AuthGuard],
    data: { isAllow: true },
  },
  {
    path: paths.PRODUCTS,
    pathMatch: 'full',
    component: ProductComponent,
    canActivate: [AuthGuard],
    data: { isAllow: true, url: backendUrl.PRODUCTS_SEARCH },
  },
  {
    path: paths.PURCHASE_HISTORY,
    pathMatch: 'full',
    component: ProductComponent,
    canActivate: [AuthGuard],
    data: { role: [RoleEnum.admin, RoleEnum.user], url: backendUrl.PRODUCT_PURCHASE_HISTORY_SEARCH },
  },
  {
    path: paths.USER_PRODUCTS,
    pathMatch: 'full',
    component: ProductComponent,
    canActivate: [AuthGuard],
    data: { role: [RoleEnum.admin, RoleEnum.user], url: backendUrl.PRODUCTS_USER_SEARCH },
  },
  {
    path: paths.SELL_PRODUCT,
    pathMatch: 'full',
    component: SellProductComponent,
    canActivate: [AuthGuard],
    data: { role: [RoleEnum.admin, RoleEnum.user] },
  },
  {
    path: paths.PROFILE,
    pathMatch: 'full',
    component: ProfileComponent,
    canActivate: [AuthGuard],
    data: { role: [RoleEnum.admin, RoleEnum.user] },
  },
  {
    path: paths.CHANGE_PASSWORD,
    pathMatch: 'full',
    component: ChangePasswordComponent,
    canActivate: [AuthGuard],
    data: { role: [RoleEnum.admin, RoleEnum.user] },
  },
  {
    path: paths.CONTACT_SUPPORT,
    pathMatch: 'full',
    component: ContactSupportComponent,
    canActivate: [AuthGuard],
    data: { role: [RoleEnum.admin, RoleEnum.user] },
  },
  { path: '**', redirectTo: paths.LOGIN },
];
