package ntnu.edu.stud.calculator.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorServiceTest {

    private final CalculatorService calculatorService = new CalculatorService();

    @Test
    void testAddition() {
        String result = calculatorService.performCalculation("2+2");
        assertEquals("4", result);
    }

    @Test
    void testSubtraction() {
        String result = calculatorService.performCalculation("5-3");
        assertEquals("2", result);
    }

    @Test
    void testDivisionByZero() {
        String result = calculatorService.performCalculation("10/0");
        assertEquals("Infinity", result);
    }

    @Test
    void testInvalidExpression() {
        String result = calculatorService.performCalculation("5+");
        assertTrue(result.contains("Error"));
    }
}
