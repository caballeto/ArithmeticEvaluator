package com.evaluator.core;

import com.evaluator.exceptions.SyntaxError;
import com.evaluator.representation.Token;
import com.evaluator.representation.TokenType;

import java.util.ArrayList;
import java.util.List;

import static com.evaluator.representation.TokenType.*;

public class Scanner {
  private int current = 0;
  private int start = 0;
  private final String source;
  private final List<Token> tokens = new ArrayList<>();

  public Scanner(String source) {
    this.source = source;
  }

  public List<Token> tokens() {
    while (!isAtEnd()) {
      start = current;
      scanToken();
    }

    tokens.add(new Token(null, "", TokenType.EOF));
    return tokens;
  }

  private void scanToken() {
    char c = advance();
    switch (c) {
      case '(': addToken(LEFT_PAREN); break;
      case ')': addToken(RIGHT_PAREN); break;
      case '+': addToken(PLUS); break;
      case '-': addToken(MINUS); break;
      case '*': addToken(STAR); break;
      case '/': addToken(SLASH); break;
      case '^': addToken(EXPONENT); break;
      case ',': addToken(COMMA); break;
      case '\t':
      case '\r':
      case ' ':
        break;

      default:
        if (isDigit(c)) {
          number();
        } else if (isAlpha(c)) {
          identifier();
        } else {
          throw new SyntaxError("Error: Unexpected character '" + c + "'");
        }

        break;
    }
  }

  private void identifier() {
    while (isAlphaNumeric(peek())) advance();
    addToken(IDENTIFIER, source.substring(start, current));
  }

  private void number() {
    while (isDigit(peek())) advance();

    if (peek() == '.' && isDigit(peekNext())) {
      advance();

      while (isDigit(peek())) advance();
    }

    addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
  }

  private boolean isAlphaNumeric(char c) {
    return isAlpha(c) || isDigit(c);
  }

  private boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  }

  private boolean isAlpha(char c) {
    return c >= 'a' && c <= 'z';
  }

  private void addToken(TokenType type) {
    addToken(type, null);
  }

  private void addToken(TokenType type, Object literal) {
    String lexeme = source.substring(start, current);
    tokens.add(new Token(literal, lexeme, type));
  }

  private boolean isAtEnd() {
    return current >= source.length();
  }

  private char advance() {
    current++;
    return source.charAt(current - 1);
  }

  private char peekNext() {
    if (current + 1 >= source.length()) return '\0';
    return source.charAt(current + 1);
  }

  private char peek() {
    if (isAtEnd()) return '\0';
    return source.charAt(current);
  }
}
