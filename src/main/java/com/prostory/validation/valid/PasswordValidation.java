package com.prostory.validation.valid;

import com.prostory.validation.PasswordValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidation implements ConstraintValidator<PasswordValid, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) return false;
        System.out.println("PASSWORD VALUE: " + s);
        boolean result = s.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$");
        System.out.println("PASSWORD VALID: " + result);
        return result;
    }
}
