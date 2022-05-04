package com.writemaster.platform.error;

public class ErrorDTO {
  String uri;
  String code;
  String message;

  public ErrorDTO() {
  }

  public ErrorDTO(String code, String message, String uri) {
    this.code = code;
    this.message = message;
    this.uri = uri;
  }

  public String getUri() {
    return uri;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
