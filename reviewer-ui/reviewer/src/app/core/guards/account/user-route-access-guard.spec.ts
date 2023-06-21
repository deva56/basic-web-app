import { TestBed } from '@angular/core/testing';
import { UserRouteAccessGuard } from './user-route-access-guard';
import { AccountService } from 'src/app/services/account/account.service';
import { Router } from '@angular/router';

describe('UserRouteAccessGuard', () => {
  let service: UserRouteAccessGuard;
  let accountService: AccountService;
  let router: Router

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [{ provide: AccountService, useValue: accountService }, {
        provide: Router, useValue: router
      }]
    });
    service = TestBed.inject(UserRouteAccessGuard);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
