package org.etf.webshopbackend.exceptions;

import org.springframework.http.HttpStatus;

public class BadCredentialsException extends HttpException {

  public BadCredentialsException() {
    super(HttpStatus.BAD_REQUEST, null);
  }

  public BadCredentialsException(Object data) {
    super(HttpStatus.BAD_REQUEST, data);
  }
}
