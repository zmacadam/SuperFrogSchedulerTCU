package tcu.edu.webtech.scheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tcu.edu.webtech.scheduler.domain.Request;
import tcu.edu.webtech.scheduler.domain.User;
import tcu.edu.webtech.scheduler.service.RequestService;
import tcu.edu.webtech.scheduler.service.UserService;

import java.util.List;

@RestController
public class DataController {

    @Autowired
    private UserService userService;

    @Autowired
    private RequestService requestService;

    @PutMapping("/updateCustomer/{id}")
    public void updateCustomer(@PathVariable("id")Integer id, @RequestParam("firstName")String firstName, @RequestParam("lastName") String lastName,
                               @RequestParam("userName") String userName, @RequestParam("email") String email) {
        User user = userService.findById(id);
        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setUsername(userName);
        user.setEmail(email);
        userService.save(user);
    }

    @PutMapping("/updateRequest/{id}")
    public void updateRequest(@PathVariable("id") Integer id, @RequestParam("date")String date, @RequestParam("time") String time,
                               @RequestParam("occasion") String occasion, @RequestParam("distance") Integer distance) {
        Request request = requestService.findById(id);
        request.setDate(date);
        request.setTime(time);
        request.setOccasion(occasion);
        request.setMiles(distance);
        requestService.save(request);
    }


    @DeleteMapping("/deleteCustomer/{id}")
    public void deleteCustomer(@PathVariable("id") Integer id) {
        List<Request> requests = requestService.findByCustomerID(userService.findById(id));
        for (Request request : requests) {
            requestService.deleteById(request.getId());
        }
        userService.deleteById(id);
    }
}
