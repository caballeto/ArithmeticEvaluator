package com.evaluator.representation;

public class Token {
  public final Object literal;
  public final String lexeme;
  public final TokenType type;

  public Token(Object literal, String lexeme, TokenType type) {
    this.literal = literal;
    this.lexeme = lexeme;
    this.type = type;
  }
}
