package com.evaluator.core;

import com.evaluator.exceptions.EvaluationError;
import com.evaluator.representation.Expr;
import com.evaluator.representation.TokenType;

import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Evaluator implements Expr.Visitor<Object> {
  private final static HashMap<String, Function<Double, Object>> oneArg = new HashMap<>();
  private final static HashMap<String, BiFunction<Double, Double, Object>> twoArg = new HashMap<>();

  static {
    twoArg.put("max", Math::max);
    twoArg.put("min", Math::min);
    twoArg.put("pow", Math::pow);
    oneArg.put("sqrt", Math::sqrt);
    oneArg.put("sin", Math::sin);
    oneArg.put("cos", Math::cos);
    oneArg.put("tan", Math::tan);
    oneArg.put("signum", Math::signum);
    oneArg.put("abs", Math::abs);
    oneArg.put("exp", Math::exp);
    oneArg.put("log", Math::log);
    oneArg.put("log10", Math::log10);
    oneArg.put("floor", Math::floor);
    oneArg.put("ceil", Math::ceil);
  }

  public Object evaluate(Expr expr) {
    return expr.accept(this);
  }

  @Override
  public Object visit(Expr.Literal expr) {
    return expr.value;
  }

  @Override
  public Object visit(Expr.Binary expr) {
    Object left = evaluate(expr.left);
    Object right = evaluate(expr.right);

    switch (expr.operator.type) {
      case MINUS:
        checkOperands(left, right);
        return (double) left - (double) right;
      case PLUS:
        checkOperands(left, right);
        return (double) left + (double) right;
      case SLASH:
        checkOperands(left, right);
        if (Double.compare((double) right, 0.0) == 0) throw new EvaluationError("Division by zero.");
        return (double) left / (double) right;
      case STAR:
        checkOperands(left, right);
        return (double) left * (double) right;
      case EXPONENT:
        checkOperands(left, right);
        return Math.pow((double) left, (double) right);
    }

    return null;
  }

  @Override
  public Object visit(Expr.Unary expr) {
    Object right = evaluate(expr.right);

    if (!(right instanceof Double))
      throw new EvaluationError("Error: Operand must be a number.");

    if (expr.operator.type == TokenType.MINUS)
      return - (double) right;

    return null;
  }

  @Override
  public Object visit(Expr.Grouping expr) {
    return evaluate(expr.expression);
  }

  @Override
  public Object visit(Expr.Identifier expr) {
    String name = (String) expr.value;

    if (oneArg.containsKey(name)) {
      if (expr.arguments.size() != 1) throw new EvaluationError("Error: Illegal number of arguments.");
      double arg = (double) evaluate(expr.arguments.get(0));
      return oneArg.get(name).apply(arg);
    } else if (twoArg.containsKey(name)) {
      if (expr.arguments.size() != 2) throw new EvaluationError("Error: Illegal number of arguments.");
      double arg1 = (double) evaluate(expr.arguments.get(0)), arg2 = (double) evaluate(expr.arguments.get(1));
      return twoArg.get(name).apply(arg1, arg2);
    } else {
      throw new EvaluationError("Error: Unexpected function name.");
    }
  }

  private void checkOperands(Object left, Object right) {
    if (left instanceof Double && right instanceof Double) return;
    throw new EvaluationError("Error: Operands must be numbers.");
  }
}
