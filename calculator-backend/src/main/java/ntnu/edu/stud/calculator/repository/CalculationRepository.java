package ntnu.edu.stud.calculator.repository;

import ntnu.edu.stud.calculator.model.Calculation;
import ntnu.edu.stud.calculator.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculationRepository extends JpaRepository<Calculation, Long> {
    Page<Calculation> findByUserOrderByTimestampDesc(User user, Pageable pageable);
}