package es.bxg.commonlib.adapter.exception;

import org.springframework.http.HttpStatus;

public class NoContentException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private HttpStatus errorCode;
  private String errorMessage;

  public NoContentException(HttpStatus errorCode, String errorMessage) {
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  public NoContentException() {
  }

  public HttpStatus getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(HttpStatus errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
