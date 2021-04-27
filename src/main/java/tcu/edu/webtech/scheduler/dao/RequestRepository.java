package tcu.edu.webtech.scheduler.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import tcu.edu.webtech.scheduler.domain.Request;
import tcu.edu.webtech.scheduler.domain.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findAllByCustomer(User user);
    List<Request> findAllByStatus(String status);
    List<Request> findAllBySuperfrog(User user);
    List<Request> findAllByStatusNot(String status);
    List<Request> findAllByDateLessThanEqualAndDateGreaterThanEqual(String from, String to);

}
