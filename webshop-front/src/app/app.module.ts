import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {RouterModule} from '@angular/router';
import {AppComponent} from './app.component';
import {HomeComponent} from './page/home/home.component';
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

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    ButtonComponent,
    InputComponent,
    HeaderComponent,
    RegistrationComponent,
    ActivateAccountComponent,
  ],
  imports: [
    RouterModule.forRoot(routes),
    BrowserModule,
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
    AuthService,
    ToastService,
    { provide: HTTP_INTERCEPTORS, useClass: BaseUrlInterceptorService, multi: true },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {
}
