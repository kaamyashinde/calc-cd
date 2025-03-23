package ntnu.edu.stud.calculator.repository;

import ntnu.edu.stud.calculator.model.Calculation;
import ntnu.edu.stud.calculator.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CalculationRepositoryTest {

    @Autowired
    private CalculationRepository calculationRepository;

    @Test
    void testSaveCalculation() {
        Calculation calc = new Calculation("2+2", 4, new User("testuser", "password"));
        Calculation saved = calculationRepository.save(calc);
        assertNotNull(saved.getId());
    }
}
