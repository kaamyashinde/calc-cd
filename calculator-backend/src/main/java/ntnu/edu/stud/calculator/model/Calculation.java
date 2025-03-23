package ntnu.edu.stud.calculator.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDateTime;

@Entity
@Table(name = "calculations")
public class Calculation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String expression;
    private double result;

    private LocalDateTime timestamp;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Calculation(){}

    public Calculation(String expression, double result, User user){
        this.expression = expression;
        this.result = result;
        this.user = user;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId(){
        return id;
    }

    public String getExpression(){
        return expression;
    }

    public double getResult(){
        return result;}

    public User getUser(){
        return user;
    }

    public LocalDateTime getTimestamp(){
        return timestamp;
    }
}