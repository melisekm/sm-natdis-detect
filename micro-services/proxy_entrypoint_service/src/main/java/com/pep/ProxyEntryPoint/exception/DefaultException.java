package com.pep.ProxyEntryPoint.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class DefaultException extends RuntimeException{

    final HttpStatus status;
    final String field;

    public DefaultException(HttpStatus status, String message, String field, Exception cause) {
        super(message, cause);
        this.status = status;
        this.field = field;
    }

    public DefaultException(HttpStatus status, String message) {
        this(status, message, null, null);
    }

    public DefaultException(HttpStatus status, Exception cause) {
        this(status, cause.getMessage(), null, cause);
    }

    public DefaultException(HttpStatus status, String message, Exception cause) {
        this(status, message, null, cause);
    }
}
