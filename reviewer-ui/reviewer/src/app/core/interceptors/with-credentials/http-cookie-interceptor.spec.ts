import { TestBed } from '@angular/core/testing';

import { HttpCookieInterceptor } from './http-cookie-interceptor';

describe('HttpCookieInterceptor', () => {
  let service: HttpCookieInterceptor;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HttpCookieInterceptor);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
