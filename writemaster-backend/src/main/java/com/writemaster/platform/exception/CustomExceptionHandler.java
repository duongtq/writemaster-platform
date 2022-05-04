package com.writemaster.platform.exception;

import com.writemaster.platform.error.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorDTO> handleNotFound(NotFoundException ex, HttpServletRequest request) {
    HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    ErrorDTO notFound = new ErrorDTO(httpStatus.toString(), ex.getMessage(), request.getRequestURI());
    return new ResponseEntity<>(notFound, httpStatus);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorDTO> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    ErrorDTO badRequest = new ErrorDTO(httpStatus.toString(), ex.getMessage(), request.getRequestURI());
    return new ResponseEntity<>(badRequest, httpStatus);
  }

  @ExceptionHandler(InvalidDataFormatException.class)
  public ResponseEntity<ErrorDTO> handleIllegalArgument(InvalidDataFormatException ex, HttpServletRequest request) {
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    ErrorDTO badRequest = new ErrorDTO(httpStatus.toString(), ex.getMessage(), request.getRequestURI());
    return new ResponseEntity<>(badRequest, httpStatus);
  }

  @ExceptionHandler(DuplicateDataException.class)
  public ResponseEntity<ErrorDTO> handleDuplicateDate(DuplicateDataException ex, HttpServletRequest request) {
    HttpStatus httpStatus = HttpStatus.CONFLICT;
    ErrorDTO duplicateData = new ErrorDTO(httpStatus.toString(), ex.getMessage(), request.getRequestURI());
    return new ResponseEntity<>(duplicateData, httpStatus);
  }
  
  @ExceptionHandler(PostAlreadyLikedException.class)
  public ResponseEntity<ErrorDTO> handleConflict(PostAlreadyLikedException ex, HttpServletRequest request) {
  	HttpStatus httpStatus = HttpStatus.CONFLICT;
  	ErrorDTO conflict = new ErrorDTO(httpStatus.toString(), ex.getMessage(), request.getRequestURI());
  	return new ResponseEntity<>(conflict, httpStatus);
  }
}
