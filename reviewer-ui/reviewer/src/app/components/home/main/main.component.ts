import { Component, OnInit } from '@angular/core';
import { ApplicationGeneralConstants } from 'src/app/core/constants/app-general-constants';
import { AccountService } from 'src/app/services/account/account.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  appName: string = ApplicationGeneralConstants.applicationName;

  constructor(private accountService: AccountService) { }

  ngOnInit(): void {
    this.accountService.fetchUserDetails().subscribe();
  }

}
