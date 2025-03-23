package ntnu.edu.stud.calculator.model;

public class CalculationRequest {
  private final String expression;
  private final String username;
  private final String password;

  public CalculationRequest(String expression, String username, String password) {
    this.expression = expression;
    this.username = username;
    this.password = password;
  }

  public String getExpression() {
    return expression;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }   
}
