package ntnu.edu.stud.calculator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CalculatorService {

  private static final Logger logger = LoggerFactory.getLogger(CalculatorService.class);

  public String performCalculation(String expression) {
    try {
      if (expression == null || expression.trim().isEmpty()) {
        logger.error("Error: Empty expression");
        return "Error: Empty expression";
      }

      String sanitizedExpression = expression.replaceAll("--", "+");

      if (sanitizedExpression.matches(".*[+\\-*/]$")) {
        logger.error("Error: Invalid expression format: {}", sanitizedExpression);
        return "Error: Invalid expression format: " + sanitizedExpression;
      }

      // Check for division by zero
      Pattern pattern = Pattern.compile("(.*?)/(0(?!\\.))");
      Matcher matcher = pattern.matcher(sanitizedExpression);
      if (matcher.find()) {
        String numerator = matcher.group(1);
        if (!numerator.trim().isEmpty()) {
          // If there's a valid numerator, return Infinity or -Infinity based on its sign
          ScriptEngineManager manager = new ScriptEngineManager();
          ScriptEngine engine = manager.getEngineByName("JavaScript");
          Object numValue = engine.eval(numerator);
          double value = Double.parseDouble(String.valueOf(numValue));
          return value >= 0 ? "Infinity" : "-Infinity";
        }
        return "Infinity";
      }

      logger.info("Evaluating: {}", sanitizedExpression);

      ScriptEngineManager manager = new ScriptEngineManager();
      ScriptEngine engine = manager.getEngineByName("JavaScript");

      if (engine == null) {
        logger.error("Error: JavaScript engine not available");
        return "Error: JavaScript engine not available";
      }

      Object resultObj = engine.eval(sanitizedExpression);
      String result = String.valueOf(resultObj);

      return result;

    } catch (Exception e) {
      logger.error("Calculation error: {}", e.getMessage(), e);
      return "Error: " + e.getMessage();
    }
  }
}