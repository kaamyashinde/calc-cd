package ntnu.edu.stud.calculator.model;

public class CalculationResponse {
  private final String result;
  private final String expression;

  public CalculationResponse(String result, String expression) {
    this.result = result;
    this.expression = expression;
  }

  public String getResult() {
    return result;
  }

  public String getExpression() {
    return expression;
  }
}
