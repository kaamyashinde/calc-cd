package ntnu.edu.stud.calculator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import ntnu.edu.stud.calculator.model.Calculation;
import ntnu.edu.stud.calculator.model.User;
import ntnu.edu.stud.calculator.repository.CalculationRepository;

@Service
public class CalculationService {
    @Autowired
    private CalculationRepository calculationRepository;

    //Save a new calculation for a given user
    public Calculation saveCalculation(String expression, double result, User user) {
        Calculation calculation = new Calculation(expression, result, user);
        return calculationRepository.save(calculation);
    }

    //Retrieve the latest calculations for an user
    public Page<Calculation> getCalculationsForUser(User user, int page, int size) {
        return calculationRepository.findByUserOrderByTimestampDesc(user,PageRequest.of(page, size));
    }
}
