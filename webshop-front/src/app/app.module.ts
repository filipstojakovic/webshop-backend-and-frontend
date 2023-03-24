import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {RouterModule, Routes} from '@angular/router';
import {AppService} from './app.service';
import {AppComponent} from './app.component';
import {HomeComponent} from './home/home.component';
import {LoginComponent} from './login/login.component';
import {BaseUrlInterceptorService} from './interceptors/base-url-interceptor.service';
import {AngularToastifyModule, ToastService} from 'angular-toastify';
import {paths} from './constants/paths';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MaterialModule} from '../material.module';
import { ButtonComponent } from './components/button/button.component';
import { InputComponent } from './components/input/input.component';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: paths.LOGIN },
  { path: paths.HOME, pathMatch: 'full', component: HomeComponent },
  { path: paths.LOGIN, pathMatch: 'full', component: LoginComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    ButtonComponent,
    InputComponent,
  ],
  imports: [
    RouterModule.forRoot(routes),
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MaterialModule,
    FormsModule,
    AngularToastifyModule,
    ReactiveFormsModule,
  ],
  providers: [
    AppService,
    ToastService,
    { provide: HTTP_INTERCEPTORS, useClass: BaseUrlInterceptorService, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
