package com.evaluator.exceptions;

public class SyntaxError extends RuntimeException {
  public SyntaxError() {
    super();
  }

  public SyntaxError(String message) {
    super(message);
  }

  public SyntaxError(Throwable throwable) {
    super(throwable);
  }

  public SyntaxError(String message, Throwable throwable) {
    super(message, throwable);
  }
}
