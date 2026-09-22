package com.example.marketplace.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * Runs jakarta.validation (@NotBlank, @Email, @Min, etc.) annotations placed
 * on entity classes and turns any violations into a ValidationException with
 * a field -> message map. Services call validate(entity) before persisting.
 */
@Component
public class ValidationUtil {

    private final Validator validator;

    public ValidationUtil(Validator validator) {
        this.validator = validator;
    }

    public <T> void validate(T entity) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new LinkedHashMap<>();
            for (ConstraintViolation<T> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new ValidationException(errors);
        }
    }

    /** Fails fast with a single field error, useful for business-rule checks. */
    public void fail(String field, String message) {
        throw new ValidationException(field, message);
    }
}
