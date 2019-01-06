package com.evaluator.core;

import com.evaluator.exceptions.ParseError;
import com.evaluator.representation.Expr;
import com.evaluator.representation.Token;
import com.evaluator.representation.TokenType;

import java.util.ArrayList;
import java.util.List;

import static com.evaluator.representation.TokenType.*;

public class Parser {
  private final List<Token> tokens;
  private int current;
  private boolean hadError = false;

  public Parser(List<Token> tokens) {
    this.tokens = tokens;
  }

  public Expr parse() {
    return expression();
  }

  private Expr expression() {
    Expr expr = multiplication();

    while (match(MINUS, PLUS)) {
      Token operator = previous();
      Expr right = multiplication();
      expr = new Expr.Binary(expr, operator, right);
    }

    return expr;
  }

  private Expr multiplication() {
    Expr expr = exponentiation();

    while (match(SLASH, STAR, REMAINDER)) {
      Token operator = previous();
      Expr right = exponentiation();
      expr = new Expr.Binary(expr, operator, right);
    }

    return expr;
  }

  private Expr exponentiation() {
    Expr expr = unary();

    while (match(EXPONENT)) {
      Token operator = previous();
      Expr right = unary();
      expr = new Expr.Binary(expr, operator, right);
    }

    return expr;
  }

  private Expr unary() {
    if (match(MINUS)) {
      Token operator = previous();
      Expr right = unary();
      return new Expr.Unary(operator, right);
    }

    return primary();
  }

  private Expr primary() {
    if (match(NUMBER)) {
      return new Expr.Literal(previous().literal);
    }

    if (match(IDENTIFIER)) {
      Token identifier = previous();
      consume(LEFT_PAREN, "Error: Expected '(' after function call.");
      List<Expr> arguments = new ArrayList<>();

      if (!check(RIGHT_PAREN)) {
        do {
          arguments.add(expression());
        } while (match(COMMA));
      }

      consume(RIGHT_PAREN, "Error: Expected ')' after expression.");
      return new Expr.Identifier(identifier.literal, arguments);
    }

    if (match(LEFT_PAREN)) {
      Expr expr = expression();
      consume(RIGHT_PAREN, "Error: Expected ')' after expression.");
      return new Expr.Grouping(expr);
    }

    throw error("Error: Expression expected.");
  }

  public boolean hadError() {
    return hadError;
  }

  private boolean match(TokenType... types) {
    for (TokenType type : types) {
      if (check(type)) {
        advance();
        return true;
      }
    }

    return false;
  }

  private ParseError error(String message) {
    hadError = true;
    return new ParseError(message);
  }

  private Token consume(TokenType type, String message) {
    if (check(type)) return advance();

    throw error(message);
  }

  private boolean check(TokenType type) {
    if (isAtEnd()) return false;
    return peek().type == type;
  }

  private Token previous() {
    return tokens.get(current - 1);
  }

  private Token peek() {
    return tokens.get(current);
  }

  private boolean isAtEnd() {
    return peek().type == EOF;
  }

  private Token advance() {
    if (!isAtEnd()) current++;
    return previous();
  }
}
