package com.writemaster.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public abstract class DuplicateDataException extends RuntimeException {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String message;

  public DuplicateDataException(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

}
