package tcu.edu.webtech.scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tcu.edu.webtech.scheduler.dao.RequestRepository;
import tcu.edu.webtech.scheduler.domain.Request;
import tcu.edu.webtech.scheduler.domain.User;

import java.util.List;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    public Request save(Request request) {
        return requestRepository.save(request);
    }

    public List<Request> findByCustomerID(User user) {
        return requestRepository.findAllByCustomer(user);
    }

    public List<Request> findAll() {
        return requestRepository.findAll();
    }

    public Request findById(Integer id) { return requestRepository.findById(id).get(); }

    public void deleteById(Integer id) { requestRepository.deleteById(id); }
}
