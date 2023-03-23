package org.etf.webshopbackend.exceptions;

import org.springframework.http.HttpStatus;

public class NotAuthenticatedException extends HttpException {

  public NotAuthenticatedException() {
    super(HttpStatus.UNAUTHORIZED, null);
  }

  public NotAuthenticatedException(Object data) {
    super(HttpStatus.UNAUTHORIZED, data);
  }

}
