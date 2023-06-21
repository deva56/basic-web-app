import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthStatesConstants } from 'src/app/core/constants/auth-states-constants';
import { PasswordValidator } from 'src/app/core/utils/validators/password-validator';
import { RegisterRequest } from 'src/app/models/auth/register/register-request';
import { BasicOperationResult } from 'src/app/models/shared/basic-operation-result';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {

  signupForm: FormGroup;
  registerRequest: RegisterRequest = new RegisterRequest;
  signupError: string = "";

  constructor(private authService: AuthService, private toastr: ToastrService, private router: Router) {
    this.signupForm = new FormGroup({
      username: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', Validators.required),
      confirmPassword: new FormControl('', Validators.required)
    }, { validators: PasswordValidator.passwordConfirmationValidator("password", "confirmPassword") });
  }

  signup() {

    const signupRequestObservable = {
      next: () => {
        this.toastr.success("Please activate your account before logging in by clicking on the provided link in your email.", "Your account has been successfuly created.")
        this.signupForm.reset();
        this.router.navigateByUrl("");
      }, error: (error: HttpErrorResponse) => {
        const result: BasicOperationResult = error.error;
        if (result !== null) {
          if (result.statusCode === 409) {
            if (result.resultDescription.match(AuthStatesConstants.USER_WITH_SAME_USERNAME_EXISTS)) {
              this.signupError = "User with same username already exists."
            }
            else {
              this.signupError = "User with same email already exists."
            }
          }
          else if (result.statusCode === 403) {
            this.toastr.error("Unexpected error occured while sending account activation email.", "Internal server error");
            this.signupError = "Unexpected error occured while sending account activation email."
          }
          else {
            this.toastr.error("Unexpected error occured. Please try again or contact us if error persists.", "Internal server error");
          }
        } else {
          this.toastr.error("Unexpected error occured. Please try again or contact us if error persists.", "Internal server error");
        }
      }
    }

    this.registerRequest.email = this.signupForm.get('email')?.value;
    this.registerRequest.username = this.signupForm.get('username')?.value;
    this.registerRequest.password = this.signupForm.get('password')?.value;
    this.registerRequest.role = "USER";

    this.authService.signup(this.registerRequest).subscribe(signupRequestObservable);
  }
}