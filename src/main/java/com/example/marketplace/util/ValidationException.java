package com.example.marketplace.util;

import java.util.Map;

/**
 * Thrown when an incoming entity fails bean-validation or a business rule
 * check (e.g. duplicate email, unknown foreign key). Carries a field -> message
 * map so the API can return precise, per-field errors to the client.
 */
public class ValidationException extends RuntimeException {

    private final Map<String, String> errors;

    public ValidationException(Map<String, String> errors) {
        super(buildMessage(errors));
        this.errors = errors;
    }

    public ValidationException(String field, String message) {
        this(Map.of(field, message));
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    private static String buildMessage(Map<String, String> errors) {
        StringBuilder sb = new StringBuilder("Validation failed");
        if (errors != null && !errors.isEmpty()) {
            sb.append(": ");
            errors.forEach((field, msg) -> sb.append(field).append(" ").append(msg).append("; "));
        }
        return sb.toString().trim();
    }
}
