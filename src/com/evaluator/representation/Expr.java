package com.evaluator.representation;

import java.util.List;

public abstract class Expr {
  public interface Visitor<E> {
    E visit(Binary expr);
    E visit(Grouping expr);
    E visit(Literal expr);
    E visit(Identifier expr);
    E visit(Unary expr);
  }

  public abstract <E> E accept(Visitor<E> visitor);

  public static class Binary extends Expr {
    public final Expr left;
    public final Token operator;
    public final Expr right;

    public Binary(Expr left, Token operator, Expr right) {
      this.left = left;
      this.operator = operator;
      this.right = right;
    }

    @Override
    public <E> E accept(Visitor<E> visitor) {
      return visitor.visit(this);
    }
  }

  public static class Grouping extends Expr {
    public final Expr expression;

    public Grouping(Expr expression) {
      this.expression = expression;
    }

    @Override
    public <E> E accept(Visitor<E> visitor) {
      return visitor.visit(this);
    }
  }

  public static class Literal extends Expr {
    public final Object value;

    public Literal(Object value) {
      this.value = value;
    }

    @Override
    public <E> E accept(Visitor<E> visitor) {
      return visitor.visit(this);
    }
  }

  public static class Unary extends Expr {
    public final Token operator;
    public final Expr right;

    public Unary(Token operator, Expr right) {
      this.operator = operator;
      this.right = right;
    }

    @Override
    public <E> E accept(Visitor<E> visitor) {
      return visitor.visit(this);
    }
  }

  public static class Identifier extends Expr {
    public final Object value;
    public final List<Expr> arguments;

    public Identifier(Object value, List<Expr> arguments) {
      this.value = value;
      this.arguments = arguments;
    }

    @Override
    public <E> E accept(Visitor<E> visitor) {
      return visitor.visit(this);
    }
  }
}
