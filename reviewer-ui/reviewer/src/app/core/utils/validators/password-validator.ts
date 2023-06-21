import { AbstractControl, ValidatorFn } from "@angular/forms";

export class PasswordValidator {
    static passwordConfirmationValidator(controlName: string, confirmationControlName: string): ValidatorFn {
        return (controls: AbstractControl) => {
            const control = controls.get(controlName);
            const matchControl = controls.get(confirmationControlName);

            if (!matchControl?.errors && control?.value !== matchControl?.value) {
                matchControl?.setErrors({
                    passwordNotMatching: {
                        actualValue: matchControl?.value,
                        requiredValue: control?.value
                    }
                });
                return { passwordNotMatching: true };
            }
            return null;
        };
    }
}
