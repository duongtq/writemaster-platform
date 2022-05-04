package com.writemaster.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDataFormatException extends RuntimeException {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String message;

  public InvalidDataFormatException(String message) {
    super();
    this.message = message;
  }

  public String getMessage() {
    return this.message;
  }
}
