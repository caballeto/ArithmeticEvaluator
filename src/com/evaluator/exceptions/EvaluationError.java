package com.evaluator.exceptions;

public class EvaluationError extends RuntimeException {
  public EvaluationError() {
    super();
  }

  public EvaluationError(String message) {
    super(message);
  }

  public EvaluationError(Throwable throwable) {
    super(throwable);
  }

  public EvaluationError(String message, Throwable throwable) {
    super(message, throwable);
  }
}
