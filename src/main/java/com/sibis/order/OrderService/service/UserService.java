package com.sibis.order.OrderService.service;

import com.sibis.order.OrderService.entity.User;
import com.sibis.order.OrderService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAllUsers(){
        return this.userRepository.findAll();
    }

    @Transactional
    public User createOrUpdate(User u) {
        return this.userRepository.save(u);
    }

    @Transactional
    public void delete(Long userID){
        this.userRepository.deleteById(userID);
    }
}
