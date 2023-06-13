import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {RouterModule} from '@angular/router';
import {AppComponent} from './app.component';
import {LoginComponent} from './page/login/login.component';
import {BaseUrlInterceptorService} from './interceptor/base-url-interceptor.service';
import {AngularToastifyModule, ToastService} from 'angular-toastify';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MaterialModule} from '../material.module';
import {ButtonComponent} from './components/button/button.component';
import {InputComponent} from './components/input/input.component';
import {HeaderComponent} from './components/header/header.component';
import {AuthService} from './service/auth.service';
import {AuthGuard} from './service/auth-guard';
import {routes} from './routes';
import {RegistrationComponent} from './page/registration/registration.component';
import {ActivateAccountComponent} from './page/activate-account/activate-account.component';
import {NgxDropzoneModule} from 'ngx-dropzone';
import {NgOptimizedImage} from '@angular/common';
import {AvatarButtonComponent} from './components/avatar-button/avatar-button.component';
import {ProductComponent} from './page/product/product.component';
import {ProductCardComponent} from './page/product/product-card/product-card.component';
import {ContactSupportComponent} from './page/contact-support/contact-support.component';
import {SellProductComponent} from './page/sell-product/sell-product.component';
import {ProductDetailsComponent} from './page/product/product-details/product-details.component';
import {CommentListComponent} from './page/product/product-details/comment-list/comment-list.component';
import {PurchaseHistoryComponent} from './page/purchase-history/purchase-history.component';
import {IvyCarouselModule} from 'carousel-angular';
import {PaymentModalComponent} from './components/payment-modal/payment-modal.component';
import {ProfileComponent} from './page/profile/profile.component';
import {CategoryDropdownComponent} from './components/category-dropdown/category-dropdown.component';
import {ChangePasswordComponent} from './page/change-password/change-password.component';
import {UserService} from './service/user.service';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ButtonComponent,
    InputComponent,
    HeaderComponent,
    RegistrationComponent,
    ActivateAccountComponent,
    AvatarButtonComponent,
    ProductComponent,
    ProductCardComponent,
    ContactSupportComponent,
    SellProductComponent,
    ProductDetailsComponent,
    CommentListComponent,
    PurchaseHistoryComponent,
    PaymentModalComponent,
    ProfileComponent,
    CategoryDropdownComponent,
    ChangePasswordComponent,
  ],
  imports: [
    RouterModule.forRoot(routes),
    BrowserModule,
    IvyCarouselModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    AngularToastifyModule,
    NgxDropzoneModule,
    NgOptimizedImage,
  ],
  providers: [
    AuthGuard,
    UserService,
    AuthService,
    ToastService,
    { provide: HTTP_INTERCEPTORS, useClass: BaseUrlInterceptorService, multi: true },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {
}
