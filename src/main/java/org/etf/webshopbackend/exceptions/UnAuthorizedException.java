package org.etf.webshopbackend.exceptions;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends HttpException {

  public UnAuthorizedException() {
    super(HttpStatus.UNAUTHORIZED, null);
  }

  public UnAuthorizedException(Object data) {
    super(HttpStatus.UNAUTHORIZED, data);
  }

}
