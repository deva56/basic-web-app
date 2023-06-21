import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from '../core/config/application-config.service';
import { BasicOperationValueResult } from 'src/app/models/shared/basic-operation-value-result';
import { Observable } from 'rxjs/internal/Observable';
import { ApiRoutesConstants } from 'src/app/core/constants/api-routes-constants';
import { BasicOperationResult } from 'src/app/models/shared/basic-operation-result';
import { BehaviorSubject, catchError, map, of, tap, throwError } from 'rxjs';
import { Account } from 'src/app/models/account/account';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LocalStorageService } from 'ngx-webstorage';

const accessToken = "access-token";

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  userIdentity: Account | null = null;
  accountCache: Observable<Account | null> | null = null;
  authenticationState = new BehaviorSubject<Account | null>(null);

  accountApi: string = ApiRoutesConstants.accountApi;

  constructor(private httpClient: HttpClient,
    private applicationConfigService: ApplicationConfigService,
    private localStorageService: LocalStorageService,
    private router: Router,
    private toastr: ToastrService) {
  }

  getAuthenticationState(): Observable<Account | null> {
    return this.authenticationState.asObservable();
  }

  authenticate(identity: Account | null) {
    this.userIdentity = identity;
    this.authenticationState.next(this.userIdentity);
    if (identity === null) {
      this.accountCache = null;
    }
  }

  hasAnyAuthority(authorities: string[] | string): boolean {
    if (this.userIdentity === null) {
      return false;
    }
    if (!Array.isArray(authorities)) {
      authorities = [authorities];
    }
    return this.userIdentity.authorities.some((authority: string) => authorities.includes(authority));
  }

  isAuthenticated(): boolean {
    return this.userIdentity !== null;
  }

  fetchUserDetails(force?: boolean): Observable<Account | null> {
    if (this.getJwtToken()) {
      if (this.accountCache === null || force === true) {
        this.accountCache = this.getUserAccountDetails().pipe(
          map(result => {
            this.authenticate(result.value);
            return result.value;
          })
        );
      } else {
        this.accountCache = of(this.userIdentity);
      }
      return this.accountCache.pipe(catchError(() => of(null)));
    } else {
      if (this.userIdentity !== null) {
        this.authenticate(null);
        this.toastr.error("Your user credentials have expired. Please log in again with your valid credentials.", "Credentials are expired")
        this.router.navigateByUrl("auth/login");
        return of(null);
      }
      return of(null);
    }
  }

  getUserAccountDetails(): Observable<BasicOperationValueResult<Account>> {
    return this.httpClient.get<BasicOperationValueResult<Account>>(
      this.applicationConfigService.getEndpointFor(`${this.accountApi}/user-account-details`));
  }

  logout(): Observable<BasicOperationResult> {
    return this.httpClient.post<BasicOperationResult>(this.applicationConfigService.getEndpointFor(`${this.accountApi}/logout-from-current-session`), null).pipe(
      tap(() => {
        this.clearJwtAccessToken();
        this.authenticate(null);
      }), catchError((err) => {
        this.clearJwtAccessToken();
        this.authenticate(null);
        return throwError(() => err);
      }
      )
    );
  }

  saveJwtAccessToken(token: string) {
    this.localStorageService.store(accessToken, token);
  }

  clearJwtAccessToken() {
    this.localStorageService.clear(accessToken);
  }

  getJwtToken(): string {
    return this.localStorageService.retrieve(accessToken);
  }
}
