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
import tcu.edu.webtech.scheduler.domain.User;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll(Sort.by("firstname").ascending());
    }

    public User findById(Integer id) {
        return userRepository.findById(id).get();
    }

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

    public User findByPhonenumber(String phoneNumber) { return userRepository.findByPhoneNumber(phoneNumber); }



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
