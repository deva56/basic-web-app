import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { MatTableModule } from '@angular/material/table';
import { MatListModule } from '@angular/material/list';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { HeaderComponent } from './components/shared/header/header.component';
import { FooterComponent } from './components/shared/footer/footer.component';
import { MainComponent } from './components/home/main/main.component';
import { SignupComponent } from './components/auth/signup/signup.component';
import { LoginComponent } from './components/auth/login/login.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http'
import { NgHttpLoaderModule } from 'ng-http-loader';
import { ToastrModule } from 'ngx-toastr';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { HttpCookieInterceptor } from './core/interceptors/with-credentials/http-cookie-interceptor';
import { TokenInterceptor } from './core/interceptors/expired-token/token-interceptor';
import { ActivateAccountComponent } from './components/auth/activate-account/activate-account/activate-account.component';
import { ForgotPasswordComponent } from './components/auth/forgot-password/forgot-password/forgot-password.component';
import { NotFoundComponent } from './components/shared/not-found/not-found.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AdministrationMainComponent } from './components/administration/administration-main/administration-main.component';
import { ProfileComponent } from './components/user/profile/profile.component';
import { AccessDeniedComponent } from './components/shared/access-denied/access-denied.component';
import { ManageUsersMainComponent } from './components/administration/manage-users/manage-users-main/manage-users-main.component';
import { SwaggerUiComponent } from './components/administration/api-documentation/swagger-ui/swagger-ui.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    MainComponent,
    SignupComponent,
    LoginComponent,
    ActivateAccountComponent,
    ForgotPasswordComponent,
    NotFoundComponent,
    AdministrationMainComponent,
    ProfileComponent,
    AccessDeniedComponent,
    ManageUsersMainComponent,
    SwaggerUiComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatPaginatorModule,
    MatTableModule,
    MatListModule,
    MatSidenavModule,
    MatIconModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgHttpLoaderModule.forRoot(),
    ToastrModule.forRoot(),
    NgxWebstorageModule.forRoot(),
    FontAwesomeModule,
  ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: HttpCookieInterceptor, multi: true },
  { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }
