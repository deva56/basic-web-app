import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { BasicValueRequest } from 'src/app/models/shared/basic-value-request';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrls: ['./activate-account.component.css']
})
export class ActivateAccountComponent implements OnInit {

  activateAccountForm: FormGroup;
  resultText: string = "";
  token: string | null = null;
  basicValueRequest: BasicValueRequest<string> = new BasicValueRequest;
  hideForm: boolean = false;

  constructor(private authService: AuthService, private route: ActivatedRoute, private toastr: ToastrService,
    private router: Router) {
    this.activateAccountForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email])
    })
  }

  ngOnInit(): void {
    this.token = this.route.snapshot.paramMap.get("token");
    if (this.token === null) {
      this.hideForm = false;
      return;
    }
    else {
      this.hideForm = true;
      this.basicValueRequest.value = this.token;

      const verifyValidationCodeObservable = {
        next: () => {
          this.toastr.success("You account was sucessfully activated. Please log in.", "Account activation sucessfull");
          this.router.navigateByUrl("/login");
        }, error: (error: HttpErrorResponse) => {
          if (error.status === 401) {
            this.resultText = "Token is expired, please request a new verification token!"
          } else if (error.status === 404) {
            this.resultText = "Token is invalid and couldn't be found!"
          } else {
            this.toastr.error("An unknown error occured while processing your request. Please try again.", "Internal Server Error");
            this.resultText = "Error occured while verifying your account. Please try again."
          }
        }
      }
      this.authService.verifyValidationCode(this.basicValueRequest).subscribe(verifyValidationCodeObservable);
    }
  }

  requestValidationCode() {
    this.basicValueRequest.value = this.activateAccountForm.get('email')?.value;

    const requestValidationCodeObservable = {
      next: () => {
        this.toastr.success("Verification email was sent to your email address.", "Email successfully sent.")
        this.hideForm = true;
        this.resultText = "Please activate your account by clicking in the link provided in your email."
      }, error: (error: HttpErrorResponse) => {
        if (error.status === 404) {
          this.resultText = "User with the given email address was not found. Please provide a valid email address of a registered user."
        } else {
          this.toastr.error("An unknown error occured while processing your request. Please try again.", "Internal Server Error");
          this.resultText = "Error occured while sending verification email. Please try again."
        }
      }
    }
    this.authService.requestValidationCode(this.basicValueRequest).subscribe(requestValidationCodeObservable);
  }

  requestNewValidationCode() {
    this.router.navigateByUrl("/activate-account");
  }
}
