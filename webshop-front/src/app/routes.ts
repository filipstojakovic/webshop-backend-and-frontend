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
import {PurchaseHistoryComponent} from './page/purchase-history/purchase-history.component';
import {AuthGuard} from './service/auth-guard';
import {ProfileComponent} from './page/profile/profile.component';
import {ChangePasswordComponent} from './page/change-password/change-password.component';

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
    // canActivate: [AuthGuard],
    data: { role: [RoleEnum.admin, RoleEnum.user] },
  },
  {
    path: paths.PRODUCTS,
    pathMatch: 'full',
    component: ProductComponent,
    canActivate: [AuthGuard],
    data: { role: [RoleEnum.admin, RoleEnum.user] },
  },
  {
    path: paths.PRODUCTS + "/:id",
    pathMatch: 'full',
    component: ProductDetailsComponent,
  },
  {
    path: paths.SELL_PRODUCT,
    pathMatch: 'full',
    component: SellProductComponent,
    // canActivate: [AuthGuard],
    data: { role: [RoleEnum.admin, RoleEnum.user] },
  },
  {
    path: paths.PROFILE, //TODO: maybe disable click if not logged in
    pathMatch: 'full',
    component: ProfileComponent,
    // canActivate: [AuthGuard],
    data: { role: [RoleEnum.admin, RoleEnum.user] },
  },
  {
    path: paths.CHANGE_PASSWORD, //TODO: maybe disable click if not logged in
    pathMatch: 'full',
    component: ChangePasswordComponent,
    // canActivate: [AuthGuard],
    data: { role: [RoleEnum.admin, RoleEnum.user] },
  },
  {
    path: paths.PURCHASE_HISTORY,
    pathMatch: 'full',
    component: PurchaseHistoryComponent,
    // canActivate: [AuthGuard],
    data: { role: [RoleEnum.admin, RoleEnum.user] },
  },
  {
    path: paths.CONTACT_SUPPORT,
    pathMatch: 'full',
    component: ContactSupportComponent,
    // canActivate: [AuthGuard],
    data: { role: [RoleEnum.admin, RoleEnum.user] },
  },
  { path: '**', redirectTo: paths.LOGIN },
];
