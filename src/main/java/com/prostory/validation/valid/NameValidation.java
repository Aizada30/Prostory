package com.prostory.validation.valid;

import com.prostory.validation.NameValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameValidation implements ConstraintValidator<NameValid, String> {
    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return name.matches("^[a-zA-Zа-яА-Я]+$") && name.length() >= 2 && name.length() <= 33;
    }
}
