package com.evaluator.exceptions;

public class ParseError extends RuntimeException {
  public ParseError() {
    super();
  }

  public ParseError(String message) {
    super(message);
  }

  public ParseError(Throwable throwable) {
    super(throwable);
  }

  public ParseError(String message, Throwable throwable) {
    super(message, throwable);
  }
}
