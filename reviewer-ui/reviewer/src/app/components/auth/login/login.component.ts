import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthStatesConstants } from 'src/app/core/constants/auth-states-constants';
import { LoginRequest } from 'src/app/models/auth/login/login-request';
import { BasicOperationResult } from 'src/app/models/shared/basic-operation-result';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  loginForm: FormGroup;
  loginRequest: LoginRequest = new LoginRequest;
  loginError = "";

  constructor(private authService: AuthService, private toastr: ToastrService, private router: Router) {
    this.loginForm = new FormGroup({
      usernameEmail: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required),
      rememberMe: new FormControl('')
    })
  }

  login() {
    this.loginRequest.usernameEmail = this.loginForm.get('usernameEmail')?.value;
    this.loginRequest.password = this.loginForm.get('password')?.value;
    this.loginRequest.rememberMe = this.loginForm.get('rememberMe')?.value;

    const loginRequestObserver = {
      next: () => {
        this.toastr.success("You have been successfully logged in to your account.", "Login successfull")
        this.loginForm.reset();
        this.router.navigateByUrl("");
      },
      error: (error: HttpErrorResponse) => {
        const result: BasicOperationResult = error.error;
        if (result !== null) {
          if (result.statusCode === 404) {
            this.loginError = "User with the given credentials was not found. Please enter existing username or email."
          }
          else if (result.statusCode === 401) {
            if (result.resultDescription.match(AuthStatesConstants.ACCOUNT_NOT_ACTIVATED)) {
              this.loginError = "Account is not activated. Activate it before logging in.";
            } else {
              this.loginError = "Entered password is incorrect. Please enter a valid password.";
            }
          }
          else {
            this.toastr.error("Unexpected error occured. Please try again or contact us if error persists.", "Internal server error")
          }
        }
        else {
          this.toastr.error("Unexpected error occured. Please try again or contact us if error persists.", "Internal server error")
        }
      }
    }

    this.authService.login(this.loginRequest).subscribe(loginRequestObserver);
  }
}
