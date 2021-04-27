package tcu.edu.webtech.scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tcu.edu.webtech.scheduler.dao.UserRepository;
import tcu.edu.webtech.scheduler.domain.MyUserPrincipal;
import tcu.edu.webtech.scheduler.domain.Request;
import tcu.edu.webtech.scheduler.domain.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestService requestService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll(Sort.by("firstname").ascending());
    }

    public List<User> findByRole(String role) {return userRepository.findAllByRole(role); }

    public User findById(Integer id) {
        return userRepository.findById(id).get();
    }

    public User findByEmail(String email) { return userRepository.findByEmail(email); }

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public User findByUsername(String username) {   return userRepository.findByUsername(username); }

    public List<User> findByRoleAndEnabled(String role) { return userRepository.findAllByRoleAndEnabledTrue(role); }

    public List<User> checkDate(String date, List<User> users) {
        List<Request> requests = requestService.findAll();
        Iterator<User> iter = users.iterator();
        while (iter.hasNext()) {
            User user = iter.next();
            for (Request request : requests) {
                if (request.getSuperfrog() != null) {
                    if (user.getId() == request.getSuperfrog().getId()) {
                        String date2 = request.getDate();
                        LocalDate from = LocalDate.parse(date);
                        LocalDate to = LocalDate.parse(date2);

                        long weeks = ChronoUnit.WEEKS.between(from, to);
                        if (weeks == 0) {
                            iter.remove();
                        }
                    }
                }
            }
        }
        return users;
    }

    public boolean checkSingleDate(String date, User user) {
        List<Request> requests = requestService.findAll();
        for (Request request : requests) {
            if (request.getSuperfrog() != null) {
                if (user.getId() == request.getSuperfrog().getId()) {
                    String date2 = request.getDate();
                    LocalDate from = LocalDate.parse(date);
                    LocalDate to = LocalDate.parse(date2);

                    long weeks = ChronoUnit.WEEKS.between(from, to);
                    if (weeks == 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user != null){
            MyUserPrincipal principal = new MyUserPrincipal(user);
            return principal;
        }else{
            throw new UsernameNotFoundException(username);
        }

    }
}
