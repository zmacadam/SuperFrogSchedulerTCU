package tcu.edu.webtech.scheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import tcu.edu.webtech.scheduler.domain.MyUserPrincipal;
import tcu.edu.webtech.scheduler.domain.User;
import tcu.edu.webtech.scheduler.service.UserService;
import tcu.edu.webtech.scheduler.utils.UserExcelExporter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    UserService userService;


    @GetMapping(value={"/", "/login"})
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
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
    public ModelAndView createNewUser(User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findByUsername(user.getUsername());
        if (userExists != null) {
            bindingResult
                    .rejectValue("userName", "error.user",
                            "There is already a user registered with the user name provided");
        }
        userExists = userService.findByPhonenumber("+1" + user.getPhoneNumber().replaceAll("\\(", "")
                .replaceAll("\\)", "").replaceAll(" ", "").replaceAll("-", ""));
        if (userExists != null) {
            bindingResult
                    .rejectValue("phoneNumber", "error.user",
                            "There is already a user registered with the phone number provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            user.setPhoneNumber("+1" + user.getPhoneNumber().replaceAll("\\(", "")
                    .replaceAll("\\)", "").replaceAll(" ", "").replaceAll("-", ""));
            user.setEnabled(true);
            user.setRoles("ADMIN");
            userService.save(user);
            modelAndView.addObject("successMessage", "User registration success!");
            modelAndView.addObject("user", user);
            modelAndView.setViewName("login");
            //SEND TEXT VERIFICATION HERE
        }
        return modelAndView;
    }

    @GetMapping("/exportUsers")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<User> listUsers = userService.findAll();

        UserExcelExporter excelExporter = new UserExcelExporter(listUsers);

        excelExporter.export(response);
    }


}
