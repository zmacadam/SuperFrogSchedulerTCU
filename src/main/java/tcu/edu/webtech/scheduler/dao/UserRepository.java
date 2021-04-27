package tcu.edu.webtech.scheduler.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import tcu.edu.webtech.scheduler.domain.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUsername(String username);
    public User findByEmail(String email);
    public List<User> findAllByRole(String role);
    public List<User> findAllByRoleAndEnabledTrue(String role);
}
