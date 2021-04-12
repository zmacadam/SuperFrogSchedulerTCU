package edu.tcu.webtech.superfrogscheduler.dao;

import edu.tcu.webtech.superfrogscheduler.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public class UserRepository extends JpaRepository<User,Integer>{
    public User findByUsername(String username);
}
