package tcu.edu.webtech.scheduler.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String date;
    private Integer miles;
    private String time;
    private String occasion;
    private String status;

    @OneToOne
    private User customer;

    @OneToOne
    private User superfrog;

    public void addCustomer(User user) {
        this.customer = customer;
    }

    public void addSuperFrog(User user) {
        this.superfrog = superfrog;
    }

}
