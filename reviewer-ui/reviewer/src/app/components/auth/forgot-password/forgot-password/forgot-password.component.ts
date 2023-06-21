import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ForgotPasswordFormStates } from 'src/app/core/constants/forgot-password-form-states';
import { PasswordValidator } from 'src/app/core/utils/validators/password-validator';
import { ForgotPasswordRequest } from 'src/app/models/auth/forgot-password/forgot-password-request';
import { BasicValueRequest } from 'src/app/models/shared/basic-value-request';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit, OnDestroy {

  forgotPasswordEmailForm: FormGroup;
  forgotPasswordNewPasswordForm: FormGroup;
  resultText: string = "";
  token: string | null = null;
  verifyTokenRequest: BasicValueRequest<string> = new BasicValueRequest;
  setNewPasswordRequest: ForgotPasswordRequest = new ForgotPasswordRequest;
  sendPasswordResetEmailRequest: BasicValueRequest<string> = new BasicValueRequest;
  @Output() showFormEmitter: EventEmitter<string> = new EventEmitter();
  showForm: string = ForgotPasswordFormStates.FORGOT_PASSWORD_EMAIL_FORM;
  isTokenValid: boolean = false;

  constructor(private authService: AuthService, private route: ActivatedRoute, private toastr: ToastrService,
    private router: Router) {
    this.forgotPasswordEmailForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email])
    });
    this.forgotPasswordNewPasswordForm = new FormGroup({
      newPassword: new FormControl('', [Validators.required]),
      newPasswordConfirmation: new FormControl('', [Validators.required])
    }, { validators: PasswordValidator.passwordConfirmationValidator("newPassword", "newPasswordConfirmation") });
  }
  ngOnDestroy(): void {
    this.showFormEmitter.unsubscribe();
  }

  ngOnInit(): void {
    this.showFormEmitter.subscribe((data: string) => this.showForm = data);

    this.token = this.route.snapshot.paramMap.get("token");
    if (this.token === null) {
      this.showForm = ForgotPasswordFormStates.FORGOT_PASSWORD_EMAIL_FORM;
      return;
    }
    else {
      this.verifyTokenRequest.value = this.token;
      const verifyForgotPasswordCodeObservable = {
        next: () => {
          this.showForm = ForgotPasswordFormStates.FORGOT_NEW_PASSWORD_FORM;
        },
        error: (error: HttpErrorResponse) => {
          this.showForm = ForgotPasswordFormStates.FORGOT_PASSWORD_TOKEN_STATUS;
          if (error.status === 401) {
            this.resultText = "Token is expired, please request a new password reset token!"
          } else if (error.status === 404) {
            this.resultText = "Token is invalid and couldn't be found!"
          } else {
            this.toastr.error("An unknown error occured while processing your request. Please try again.", "Internal Server Error");
            this.resultText = "Error occured while verifying forgot password token. Please try again."
          }
        }
      }
      this.authService.verifyForgotPasswordCode(this.verifyTokenRequest).subscribe(verifyForgotPasswordCodeObservable);
    }
  }

  setNewPassword() {
    this.setNewPasswordRequest.token = this.token!;
    this.setNewPasswordRequest.newPassword = this.forgotPasswordNewPasswordForm.get('newPassword')?.value;
    const setNewPasswordObservable = {
      next: () => {
        this.toastr.success("New password successfully set. Please log in to your account with the new password.", "Password reset successful.")
        this.forgotPasswordNewPasswordForm.reset();
        this.router.navigateByUrl("/login");
      },
      error: (error: HttpErrorResponse) => {
        this.showFormEmitter.emit(ForgotPasswordFormStates.FORGOT_PASSWORD_TOKEN_STATUS);
        if (error.status === 401) {
          this.resultText = "Token is expired, please request a new password reset token!"
        } else if (error.status === 404) {
          this.resultText = "Token is invalid and couldn't be found!"
        } else {
          this.toastr.error("An unknown error occured while processing your request. Please try again.", "Internal Server Error");
          this.resultText = "Error occured while setting new password. Please try again."
        }
      }
    }
    this.authService.setNewPassword(this.setNewPasswordRequest).subscribe(setNewPasswordObservable);
  }

  requestPasswordResetCode() {
    this.sendPasswordResetEmailRequest.value = this.forgotPasswordEmailForm.get('email')?.value;
    const requestPasswordResetCodeObservable = {
      next: () => {
        this.toastr.success("Reset password email was sent to your email address.", "Email successfully sent.")
        this.showFormEmitter.emit(ForgotPasswordFormStates.FORGOT_PASSWORD_TOKEN_STATUS)
        this.resultText = "Please reset your password by clicking in the link provided in your email."
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 404) {
          this.resultText = "User with the given email address was not found. Please provide a valid email address of a registered user."
        } else {
          this.toastr.error("An unknown error occured while processing your request. Please try again.", "Internal Server Error");
          this.resultText = "Error occured while sending password reset email. Please try again."
        }
      }
    }
    this.authService.requestForgotPasswordCode(this.sendPasswordResetEmailRequest).subscribe(requestPasswordResetCodeObservable);
  }

  requestNewPasswordResetCode() {
    this.router.navigateByUrl("/forgot-password");
  }
}
