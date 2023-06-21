import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { ForgotPasswordRequest } from 'src/app/models/auth/forgot-password/forgot-password-request';
import { LoginRequest } from 'src/app/models/auth/login/login-request';
import { RegisterRequest } from 'src/app/models/auth/register/register-request';
import { BasicValueRequest } from 'src/app/models/shared/basic-value-request';
import { BasicOperationResult } from 'src/app/models/shared/basic-operation-result';
import { ApplicationConfigService } from '../core/config/application-config.service';
import { tap } from 'rxjs';
import { AccountService } from '../account/account.service';
import { ApiRoutesConstants } from 'src/app/core/constants/api-routes-constants';
import { BasicOperationValueResult } from 'src/app/models/shared/basic-operation-value-result';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  authApi: string = ApiRoutesConstants.authApi;
  accountApi: string = ApiRoutesConstants.accountApi;

  constructor(private httpClient: HttpClient,
    private applicationConfigService: ApplicationConfigService,
    private accountService: AccountService) {
  }

  signup(registerRequest: RegisterRequest): Observable<BasicOperationResult> {
    return this.httpClient.post<BasicOperationResult>(this.applicationConfigService.getEndpointFor(`${this.authApi}/register`), registerRequest);
  }

  login(loginRequest: LoginRequest): Observable<BasicOperationValueResult<string>> {
    return this.httpClient.post<BasicOperationValueResult<string>>(this.applicationConfigService.getEndpointFor(`${this.authApi}/authenticate`), loginRequest).pipe(
      tap((result) => {
        this.accountService.saveJwtAccessToken(result.value);
        this.accountService.fetchUserDetails(true).subscribe();
      })
    );
  }

  refreshToken(): Observable<BasicOperationValueResult<string>> {
    return this.httpClient.post<BasicOperationValueResult<string>>(this.applicationConfigService.getEndpointFor(`${this.authApi}/refresh-token`), null).pipe(
      tap((result) => {
        this.accountService.saveJwtAccessToken(result.value);
      })
    );
  }

  requestValidationCode(basicValueRequest: BasicValueRequest<string>): Observable<BasicOperationResult> {
    return this.httpClient.post<BasicOperationResult>(this.applicationConfigService.getEndpointFor(`${this.authApi}/send-validation-email`), basicValueRequest);
  }

  verifyValidationCode(basicValueRequest: BasicValueRequest<string>): Observable<BasicOperationResult> {
    return this.httpClient.post<BasicOperationResult>(this.applicationConfigService.getEndpointFor(`${this.authApi}/verify-validation-code`), basicValueRequest);
  }

  requestForgotPasswordCode(basicValueRequest: BasicValueRequest<string>): Observable<BasicOperationResult> {
    return this.httpClient.post<BasicOperationResult>(this.applicationConfigService.getEndpointFor(`${this.authApi}/send-forgot-password-email`), basicValueRequest);
  }

  verifyForgotPasswordCode(basicValueRequest: BasicValueRequest<string>): Observable<BasicOperationResult> {
    return this.httpClient.post<BasicOperationResult>(this.applicationConfigService.getEndpointFor(`${this.authApi}/verify-forgot-password-code`), basicValueRequest);
  }

  setNewPassword(forgotPasswordRequest: ForgotPasswordRequest): Observable<BasicOperationResult> {
    return this.httpClient.post<BasicOperationResult>(this.applicationConfigService.getEndpointFor(`${this.authApi}/set-new-password`), forgotPasswordRequest);
  }
}
