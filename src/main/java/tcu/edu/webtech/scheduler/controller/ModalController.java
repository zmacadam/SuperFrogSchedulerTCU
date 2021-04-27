package tcu.edu.webtech.scheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import tcu.edu.webtech.scheduler.domain.Request;
import tcu.edu.webtech.scheduler.domain.User;
import tcu.edu.webtech.scheduler.service.RequestService;
import tcu.edu.webtech.scheduler.service.UserService;

import java.util.List;

@Controller
@RequestMapping("modals")
public class ModalController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserService userService;

    @GetMapping("addFrog")
    public ModelAndView addFrog() {
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("addFrog");
        return modelAndView;
    }

    @GetMapping("addCustomer")
    public ModelAndView addCustomer() {
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("addCustomer");
        return modelAndView;
    }

    @GetMapping("editRequest/{id}")
    public ModelAndView editRequest(@PathVariable("id") Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        Request request = requestService.findById(id);
        modelAndView.addObject("request", request);
        modelAndView.setViewName("editRequest");
        return modelAndView;
    }

    @GetMapping("assignSuper/{id}")
    public ModelAndView assignSuper(@PathVariable("id") Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        Request request = requestService.findById(id);
        List<User> supers = userService.findByRoleAndEnabled("SUPERFROG");
        supers = userService.checkDate(request.getDate(), supers);
        modelAndView.addObject("request", request);
        modelAndView.addObject("supers", supers);
        modelAndView.setViewName("assignSuper");
        return modelAndView;
    }

    @GetMapping("selfAssign/{requestId}/{superId}")
    public ModelAndView selfAssign(@PathVariable("requestId") Integer requestId, @PathVariable("superId") Integer superFrogId) {
        ModelAndView modelAndView = new ModelAndView();
        Request request = requestService.findById(requestId);
        User superFrog = userService.findById(superFrogId);
        boolean valid = userService.checkSingleDate(request.getDate(), superFrog);
        if (valid) {
            modelAndView.addObject("request", request);
            modelAndView.addObject("superFrog", superFrog);
        } else {
            modelAndView.addObject("error", "You already have an assignment this week.");
        }
        modelAndView.setViewName("selfAssign");
        return modelAndView;
    }

    @GetMapping("completeAppearance/{requestId}")
    public ModelAndView completeAppearance(@PathVariable("requestId") Integer requestId) {
        ModelAndView modelAndView = new ModelAndView();
        Request request = requestService.findById(requestId);
        modelAndView.addObject("request", request);
        modelAndView.setViewName("completeAppearance");
        return modelAndView;
    }

    @GetMapping("downloadReport")
    public ModelAndView downloadReport() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("downloadReport");
        return modelAndView;
    }
}
