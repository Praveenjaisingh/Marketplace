package com.example.marketplace.util;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.ServerResponse;

/**
 * Central place that turns a caught exception into a consistent JSON error
 * response, so every controller's catch block can just do:
 *
 *     catch (Exception e) { return ResponseUtil.error(e); }
 *
 * - ValidationException  -> 422 with a per-field "errors" map
 * - "...not found" messages -> 404
 * - anything else -> 400
 */
public final class ResponseUtil {

    private ResponseUtil() {}

    public static ServerResponse error(Exception e) {
        try {
            if (e instanceof ValidationException ve) {
                Map<String, Object> body = new LinkedHashMap<>();
                body.put("status", false);
                body.put("message", "Validation failed");
                body.put("errors", ve.getErrors());
                return ServerResponse.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
            }

            String message = e.getMessage() != null ? e.getMessage() : "Something went wrong";
            HttpStatus status = message.toLowerCase().contains("not found")
                    ? HttpStatus.NOT_FOUND
                    : HttpStatus.BAD_REQUEST;

            return ServerResponse.status(status).body(Map.of(
                    "status", false,
                    "message", message
            ));
        } catch (Exception fallback) {
            // Should never happen, but never let error handling itself blow up the request.
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
