package com.writemaster.platform.exception;

public class DuplicateEmailException extends DuplicateDataException {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateEmailException(String message) {
    super(message);
  }
}
