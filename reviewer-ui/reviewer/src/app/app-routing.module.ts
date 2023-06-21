import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ActivateAccountComponent } from './components/auth/activate-account/activate-account/activate-account.component';
import { ForgotPasswordComponent } from './components/auth/forgot-password/forgot-password/forgot-password.component';
import { LoginComponent } from './components/auth/login/login.component';
import { NotFoundComponent } from './components/shared/not-found/not-found.component';
import { SignupComponent } from './components/auth/signup/signup.component';
import { MainComponent } from './components/home/main/main.component';
import { AuthGuard } from './core/guards/auth/auth.guard';
import { ProfileComponent } from './components/user/profile/profile.component';
import { UserRouteAccessGuard } from './core/guards/account/user-route-access-guard';
import { AccessDeniedComponent } from './components/shared/access-denied/access-denied.component';
import { ManageUsersMainComponent } from './components/administration/manage-users/manage-users-main/manage-users-main.component';
import { AdministrationMainComponent } from './components/administration/administration-main/administration-main.component';
import { SwaggerUiComponent } from './components/administration/api-documentation/swagger-ui/swagger-ui.component';

const routes: Routes = [
  { path: "", component: MainComponent },
  { path: "signup", component: SignupComponent, canActivate: [AuthGuard] },
  { path: "login", component: LoginComponent, canActivate: [AuthGuard] },
  { path: "activate-account", component: ActivateAccountComponent, canActivate: [AuthGuard] },
  { path: "activate-account/:token", component: ActivateAccountComponent, canActivate: [AuthGuard] },
  { path: "forgot-password", component: ForgotPasswordComponent, canActivate: [AuthGuard] },
  { path: "forgot-password/:token", component: ForgotPasswordComponent, canActivate: [AuthGuard] },
  {
    path: "admin/manage-users", component: ManageUsersMainComponent, canActivate: [UserRouteAccessGuard], data: {
      authorities: ["ROLE_ADMIN"]
    }
  },
  {
    path: "admin/main-panel", component: AdministrationMainComponent, canActivate: [UserRouteAccessGuard], data: {
      authorities: ["ROLE_ADMIN"]
    }
  },
  {
    path: "admin/api-info/swagger-ui", component: SwaggerUiComponent, canActivate: [UserRouteAccessGuard], data: {
      authorities: ["ROLE_ADMIN"]
    }
  },
  { path: "account/profile", component: ProfileComponent, canActivate: [UserRouteAccessGuard] },
  { path: "access-denied", component: AccessDeniedComponent, canActivate: [UserRouteAccessGuard] },
  { path: "not-found", component: NotFoundComponent },
  { path: "**", redirectTo: '/not-found' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
