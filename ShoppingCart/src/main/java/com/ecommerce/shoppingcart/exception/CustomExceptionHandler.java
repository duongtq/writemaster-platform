package com.ecommerce.shoppingcart.exception;

import com.ecommerce.shoppingcart.dto.ErrorDTO;
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

  @ExceptionHandler(InvalidDataFormatException.class)
  public ResponseEntity<ErrorDTO> handleInvalidDataFormat(InvalidDataFormatException ex, HttpServletRequest request) {
    HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    ErrorDTO invalidDataFormat = new ErrorDTO(httpStatus.toString(), ex.getMessage(), request.getRequestURI());
    return new ResponseEntity<>(invalidDataFormat, httpStatus);
  }
}
