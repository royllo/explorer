package org.royllo.explorer.web.util.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * Access forbidden exception.
 */
@ResponseStatus(value = FORBIDDEN, reason = "Access is forbidden")
public class AccessForbiddenException extends RuntimeException {
}
