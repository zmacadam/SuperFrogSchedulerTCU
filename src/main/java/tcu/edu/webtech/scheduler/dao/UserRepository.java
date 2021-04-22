package tcu.edu.webtech.scheduler.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import tcu.edu.webtech.scheduler.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUsername(String username);
    public User findByPhoneNumber(String phonenumber);
}
