package org.etf.webshopbackend.advice;

import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etf.webshopbackend.exceptions.HttpException;
import org.etf.webshopbackend.utils.LoggingUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public final ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e,
                                                                   HandlerMethod handlerMethod) {
    LoggingUtil.logException(e, getLog(handlerMethod));
    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpException.class)
  public final ResponseEntity<Object> handleHttpException(HttpException e, HandlerMethod handlerMethod) {
    Log log = getLog(handlerMethod);
    log.error(e);
    if (e.getStatus() == null) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>(e.getData(), e.getStatus());
  }

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<Object> handleException(Exception e, HandlerMethod handlerMethod) {
    LoggingUtil.logException(e, getLog(handlerMethod));
    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private Log getLog(HandlerMethod handlerMethod) {
    return LogFactory.getLog(handlerMethod.getMethod().getDeclaringClass());
  }
}
