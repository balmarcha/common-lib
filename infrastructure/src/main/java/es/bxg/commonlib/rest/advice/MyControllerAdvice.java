package es.bxg.commonlib.rest.advice;

import es.bxg.commonlib.adapter.exception.NoContentException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyControllerAdvice {

  @ExceptionHandler(NoContentException.class)
  public ResponseEntity<String> handle(NoContentException exception){
    return new ResponseEntity<>(exception.getErrorMessage(), exception.getErrorCode());
  }

}
