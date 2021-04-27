package tcu.edu.webtech.scheduler.controller;

import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tcu.edu.webtech.scheduler.domain.Request;
import tcu.edu.webtech.scheduler.domain.User;
import tcu.edu.webtech.scheduler.service.RequestService;
import tcu.edu.webtech.scheduler.service.UserService;
import tcu.edu.webtech.scheduler.utils.UserExcelExporter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private RequestService requestService;

    @RequestMapping("/default")
    public ModelAndView defaultAfterLogin(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Collection<? extends GrantedAuthority> authorities;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        authorities = auth.getAuthorities();
        String role = authorities.toArray()[0].toString();
        ModelAndView modelAndView = new ModelAndView();
        if (role.equals("DIRECTOR")) {
            modelAndView.setViewName("director/home");
            List<User> customers = userService.findByRole("CUSTOMER");
            List<User> superfrogs = userService.findByRole("SUPERFROG");
            List<Request> requests = requestService.findUnfinishedRequests();
            modelAndView.addObject("customers", customers);
            modelAndView.addObject("superfrogs", superfrogs);
            modelAndView.addObject("requests", requests);
            modelAndView.addObject("welcome", "Welcome Back Spirit Director!");
        } else if (role.equals("SUPERFROG")) {
            modelAndView.setViewName("superfrog/home");
            List<Request> requests = requestService.findByAssignedAndSuperFrog(user);
            modelAndView.addObject("requests", requests);
            modelAndView.addObject("user", user);
            modelAndView.addObject("welcome", "Welcome Back " + user.getFullName() + "!");
        } else {
            modelAndView.setViewName("customer/home");
            modelAndView.addObject("request", new Request());
            List<Request> requests = requestService.findByCustomerID(user);
            modelAndView.addObject("requests", requests);
            modelAndView.addObject("user", user);
            modelAndView.addObject("welcome", "Welcome Back " + user.getFullName() + "!");
        }
        return modelAndView;
    }

    @GetMapping(value={"/", "/login"})
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping(value="/terms")
    public ModelAndView terms() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/customer/terms-condition");
        return modelAndView;
    }


    @GetMapping(value="/registration")
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PostMapping(value = "/registration")
    public ModelAndView createNewUser(User user, BindingResult bindingResult, Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView();
        String errors = checkIfUserExists(user, bindingResult);
        if (errors != null) {
            modelAndView = defaultAfterLogin(authentication);
            modelAndView.addObject("error", errors);
            return modelAndView;
        }
        user.setEnabled(true);
        user.setRole("CUSTOMER");
        userService.save(user);
        modelAndView.addObject("successMessage", "User registration success!");
        modelAndView.addObject("user", user);
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @PostMapping("/newRequest")
    public ModelAndView newRequest(Request request, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        request.setCustomer(user);
        request.setStatus("PENDING");
        requestService.save(request);
        return defaultAfterLogin(authentication);
    }


    @PostMapping(value="/updateUser/{id}")
    public ModelAndView updateUser(@PathVariable("id") Integer id, User user, Authentication authentication) {
        User oldUser = userService.findById(id);
        oldUser.setFirstname(user.getFirstname());
        oldUser.setLastname(user.getLastname());
        oldUser.setEmail(user.getEmail());
        oldUser.setAge(user.getAge());
        userService.save(oldUser);
        return defaultAfterLogin(authentication);
    }

    @GetMapping("/downloadReport")
    public void exportToExcel(@RequestParam("from") String from, @RequestParam("to") String to, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Requests_From_" + from + "_To_" + to + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Request> listRequest = requestService.findBetween(from, to);

        UserExcelExporter excelExporter = new UserExcelExporter(listRequest);

        excelExporter.export(response);
    }

    @PostMapping("/toggleSuperFrog/{id}")
    public ModelAndView toggleSuperFrog(@PathVariable("id") Integer id, Authentication authentication) {
        User user = userService.findById(id);
        user.setEnabled(!user.isEnabled());
        userService.save(user);
        ModelAndView modelAndView = defaultAfterLogin(authentication);
        return modelAndView;
    }

    @PostMapping("/addFrog")
    public ModelAndView addFrog(User user, BindingResult bindingResult, Authentication authentication) {
        checkIfUserExists(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return defaultAfterLogin(authentication);
        } else {
            user.setEnabled(true);
            user.setRole("SUPERFROG");
            userService.save(user);
        }
        return defaultAfterLogin(authentication);
    }

    @PostMapping("/addCustomer")
    public ModelAndView addCustomer(User user, BindingResult bindingResult, Authentication authentication) {
        checkIfUserExists(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return defaultAfterLogin(authentication);
        } else {
            user.setEnabled(true);
            user.setRole("CUSTOMER");
            userService.save(user);
        }
        return defaultAfterLogin(authentication);
    }

    @PostMapping("/approveRequest/{id}")
    public ModelAndView approve(@PathVariable("id") Integer id, Authentication authentication) {
        Request request = requestService.findById(id);
        request.setStatus("APPROVED");
        requestService.save(request);
        return defaultAfterLogin(authentication);
    }

    @PostMapping("/denyRequest/{id}")
    public ModelAndView deny(@PathVariable("id") Integer id, Authentication authentication) {
        Request request = requestService.findById(id);
        request.setStatus("REJECTED");
        requestService.save(request);
        return defaultAfterLogin(authentication);
    }

    @PostMapping("/addSuper/{id}")
    public ModelAndView addSuper(@PathVariable("id") Integer id, @RequestParam("super") String username, Authentication authentication) {
        Request request = requestService.findById(id);
        User superFrog = userService.findByUsername(username);
        request.setSuperfrog(superFrog);
        request.setStatus("ASSIGNED");
        requestService.save(request);
        return defaultAfterLogin(authentication);
    }

    @PostMapping("/selfSignUp/{requestId}/{superFrogId}")
    public ModelAndView selfSignUp(@PathVariable("requestId") Integer reqId, @PathVariable("superFrogId") Integer superId, Authentication authentication) {
        Request request = requestService.findById(reqId);
        User superFrog = userService.findById(superId);
        request.setSuperfrog(superFrog);
        request.setStatus("ASSIGNED");
        requestService.save(request);
        return defaultAfterLogin(authentication);
    }

    @PostMapping("/completeAppearance/{requestId}")
    public ModelAndView completeAppearance(@PathVariable("requestId") Integer reqId, Authentication authentication) {
        Request request = requestService.findById(reqId);
        request.setStatus("FINISHED");
        requestService.save(request);
        return defaultAfterLogin(authentication);
    }

    private String checkIfUserExists(User user, BindingResult bindingResult) {
        User userExists = userService.findByUsername(user.getUsername());
        if (userExists != null) {
            return "There is already a user registered with the user name provided!";
        }
        userExists = userService.findByEmail(user.getEmail());
        if (userExists != null) {
            return "There is already a user registered with the email provided!";
        }
        return null;
    }

}
