package ntnu.edu.stud.calculator.model;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;

@Entity
@Table(name = "app_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Calculation> calculations;

    public User(){  }

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public Long getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public List<Calculation> getCalculations(){
        return calculations;
    }


}
