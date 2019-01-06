import com.evaluator.core.Evaluator;
import com.evaluator.core.Parser;
import com.evaluator.core.Scanner;
import com.evaluator.exceptions.EvaluationError;
import com.evaluator.exceptions.ParseError;
import com.evaluator.exceptions.SyntaxError;
import com.evaluator.representation.Expr;
import com.evaluator.representation.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Runner implements Runnable {
  private String evaluate(String expression) {
    try {
      Scanner scanner = new Scanner(expression);
      List<Token> tokens = scanner.tokens();

      Parser parser = new Parser(tokens);
      Expr expr = parser.parse();

      Evaluator evaluator = new Evaluator();
      String s = evaluator.evaluate(expr).toString();
      return s.endsWith(".0") ? s.substring(0, s.length() - 2): s;
    } catch (SyntaxError | ParseError | EvaluationError e) {
      System.out.println(e.getMessage());
      return "";
    }
  }

  @Override
  public void run() {
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);
    String expression;

    System.out.println("Start inputting expressions. ['exit' to finish ].");
    System.out.print("Input query: ");
    while (true) {
      try {
        expression = reader.readLine();
        if (expression.equals("exit")) break;
        String result = evaluate(expression);
        if (result.length() != 0) System.out.println(result);
        System.out.print("Input query: ");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) {
    new Runner().run();
  }
}
