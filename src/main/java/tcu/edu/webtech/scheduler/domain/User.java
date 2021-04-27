package tcu.edu.webtech.scheduler.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String email;

    private String firstname;
    private String lastname;
    private boolean enabled;
    private String role;
    private Integer age;

    public String getFullName() {
        return this.firstname + " " + this.lastname;
    }

}
