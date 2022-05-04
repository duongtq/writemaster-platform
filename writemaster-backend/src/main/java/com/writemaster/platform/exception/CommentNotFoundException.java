package com.writemaster.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends NotFoundException {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommentNotFoundException(String message) {
    super(message);
  }
}
