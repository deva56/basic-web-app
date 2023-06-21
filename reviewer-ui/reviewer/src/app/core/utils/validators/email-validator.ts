import { AbstractControl, ValidatorFn } from "@angular/forms";

export class EmailValidator {
    static emailConfirmationValidator(controlName: string, confirmationControlName: string): ValidatorFn {
        return (controls: AbstractControl) => {
            const control = controls.get(controlName);
            const matchControl = controls.get(confirmationControlName);

            if (!matchControl?.errors && control?.value !== matchControl?.value) {
                matchControl?.setErrors({
                    emailNotMatching: {
                        actualValue: matchControl?.value,
                        requiredValue: control?.value
                    }
                });
                return { emailNotMatching: true };
            }
            return null;
        };
    }
}
