package com.writemaster.platform.exception;

public class DuplicateAuthorException extends DuplicateDataException {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateAuthorException(String message) {
    super(message);
  }
}
