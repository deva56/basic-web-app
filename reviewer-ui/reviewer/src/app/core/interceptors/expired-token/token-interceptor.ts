import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Observable, switchMap, catchError, throwError, EMPTY } from 'rxjs';
import { AuthService } from 'src/app/services/auth/auth.service';
import { BasicOperationResult } from 'src/app/models/shared/basic-operation-result';
import { AccountService } from 'src/app/services/account/account.service';
import { AuthStatesConstants } from '../../constants/auth-states-constants';

const authorizationHeader = "Authorization";
const bearerToken = "Bearer ";

@Injectable({
  providedIn: 'root'
})
export class TokenInterceptor implements HttpInterceptor {

  isTokenRefreshing = false;

  constructor(private router: Router,
    private toastr: ToastrService,
    private authService: AuthService,
    private accountService: AccountService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const jwtAccessToken = this.accountService.getJwtToken();
    if (jwtAccessToken) {
      return next.handle(this.addTokenToHeader(req, jwtAccessToken)).pipe(catchError(error => {
        if (error instanceof HttpErrorResponse && error.status === 403) {
          return this.handleAuthErrors(req, next);
        }
        else {
          return throwError(() => error)
        }
      }));
    } else {
      return next.handle(req);
    }
  }

  handleAuthErrors(req: HttpRequest<any>, next: HttpHandler)
    : Observable<HttpEvent<any>> {
    if (!this.isTokenRefreshing) {
      this.isTokenRefreshing = true;
      return this.authService.refreshToken().pipe(
        switchMap((result) => {
          this.isTokenRefreshing = false;
          return next.handle(this.addTokenToHeader(req, result.value));
        }), catchError((error: HttpErrorResponse) => {
          this.isTokenRefreshing = false;
          const errorResult: BasicOperationResult = error.error;
          if (errorResult !== null) {
            if (errorResult.statusCode === 401 &&
              errorResult.resultDescription.match(AuthStatesConstants.REFRESH_TOKEN_EXPIRED)) {
              this.accountService.authenticate(null);
              this.accountService.clearJwtAccessToken();
              this.toastr.error("Your user credentials have expired. Please log in again with your valid credentials.", "Credentials are expired")
              this.router.navigateByUrl("/login");
              return EMPTY;
            }
          }
          return throwError(() => error);
        })
      );
    } else {
      return next.handle(this.addTokenToHeader(req, this.accountService.getJwtToken()));
    }
  }

  addTokenToHeader(req: HttpRequest<any>, jwtAccessToken: any) {
    return req.clone({
      headers: req.headers.set(authorizationHeader, bearerToken + jwtAccessToken)
    });
  }
}
