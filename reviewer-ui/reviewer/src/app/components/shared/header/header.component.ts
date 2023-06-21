import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ApplicationGeneralConstants } from 'src/app/core/constants/app-general-constants';
import { Account } from 'src/app/models/account/account';
import { AccountService } from 'src/app/services/account/account.service';
import {MatIconModule} from '@angular/material/icon';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy {

  account: Account | null = null;
  isUserLoggedIn: boolean = false;
  loggedInUsername: string = "";
  loggedInUserRoles: string[] = [];
  appName: string = ApplicationGeneralConstants.applicationName;

  constructor(private accountService: AccountService,
    private toastr: ToastrService, private router: Router) { }

  ngOnDestroy(): void {
  }

  ngOnInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
    });
  }

  logout() {
    const logoutRequestObservable = {
      next: () => {
        this.toastr.success("You have been successfully logged out from your account.", "Logout successful");
        this.router.navigateByUrl("");
      },
      error: () => {
        this.toastr.error("An unknown error occurred on the server while invalidating your session. However you were logged out in the browser from your account.", "Internal server error");
      }
    }
    this.accountService.logout().subscribe(logoutRequestObservable);
  }
}
