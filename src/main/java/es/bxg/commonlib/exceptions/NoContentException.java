package es.bxg.commonlib.exceptions;

public class NoContentException extends Exception {
  public NoContentException() {
    super();
  }

  public NoContentException(String message) {
    super(message);
  }

  public NoContentException(String message, Throwable cause) {
    super(message, cause);
  }
}
