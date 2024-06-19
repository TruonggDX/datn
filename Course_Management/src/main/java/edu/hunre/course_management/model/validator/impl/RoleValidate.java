package edu.hunre.course_management.model.validator.impl;

import edu.hunre.course_management.model.validator.RoleValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class RoleValidate implements ConstraintValidator<RoleValid, List<Long>> {

    @Override
    public void initialize(RoleValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<Long> listRoleId, ConstraintValidatorContext constraintValidatorContext) {
        return listRoleId != null && !listRoleId.isEmpty();
    }
}
