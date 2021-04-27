package tcu.edu.webtech.scheduler.datainit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tcu.edu.webtech.scheduler.domain.Request;
import tcu.edu.webtech.scheduler.domain.User;
import tcu.edu.webtech.scheduler.service.RequestService;
import tcu.edu.webtech.scheduler.service.UserService;

@Component
public class DBDataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private RequestService requestService;

    @Override
    public void run(String... args) throws Exception {
        loadData();
    }

    private void loadData() {
        final User spiritDirector = new User();
        spiritDirector.setRole("DIRECTOR");
        spiritDirector.setEnabled(true);
        spiritDirector.setFirstname("Spirit");
        spiritDirector.setLastname("Director");
        spiritDirector.setEmail("SpritDirector@tcu.edu");
        spiritDirector.setAge(33);
        spiritDirector.setUsername("spirit");
        spiritDirector.setPassword("director");
        userService.save(spiritDirector);

        for (int i = 1; i <= 5; i++) {
            final User superFrog = new User();
            superFrog.setRole("SUPERFROG");
            superFrog.setEnabled(true);
            superFrog.setFirstname("Super" + i);
            superFrog.setLastname("Frog" + i);
            superFrog.setEmail("SuperFrog" + i + "@tcu.edu");
            superFrog.setAge(21);
            superFrog.setUsername("SuperFrog" + i);
            superFrog.setPassword("SuperFrog" + i);
            userService.save(superFrog);
        }

        for (int i = 1; i <= 5; i++) {
            final User customer = new User();
            customer.setRole("CUSTOMER");
            customer.setEnabled(true);
            customer.setFirstname("Paying" + i);
            customer.setLastname("Customer" + i);
            customer.setEmail("Customer" + i + "@tcu.edu");
            customer.setAge(21 + i);
            customer.setUsername("customer" + i);
            customer.setPassword("password" + i);
            userService.save(customer);

            final Request request1 = new Request();
            request1.setStatus("PENDING");
            request1.setCustomer(customer);
            request1.setDate("2021-04-29");
            request1.setTime("09:00");
            request1.setMiles(20);
            request1.setOccasion("Wedding");
            requestService.save(request1);

            final Request request2 = new Request();
            request2.setStatus("APPROVED");
            request2.setCustomer(customer);
            request2.setDate("2021-04-30");
            request2.setTime("12:00");
            request2.setMiles(20);
            request2.setOccasion("Birthday");
            requestService.save(request2);
        }
    }
}
