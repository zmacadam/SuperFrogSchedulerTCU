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

            final Request request = new Request();
            request.setStatus("PENDING");
            request.setCustomer(customer);
            request.setDate("2021-04-29");
            request.setTime("09:00");
            request.setMiles(20);
            request.setOccasion("Wedding");
            requestService.save(request);
        }
    }
}
