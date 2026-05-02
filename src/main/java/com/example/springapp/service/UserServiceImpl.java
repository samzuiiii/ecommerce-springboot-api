package com.example.springapp.service;

import com.example.springapp.model.User;
import com.example.springapp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public User addUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public User updateUser(Long id, User user) {
        User existing = userRepo.findById(id).orElse(null);
        if (existing == null) return null;
        existing.setName(user.getName());
        existing.setEmail(user.getEmail());
        existing.setPassword(user.getPassword());
        existing.setRole(user.getRole());
        return userRepo.save(existing);
    }

    @Override
    public boolean deleteUser(Long id) {
        User existing = userRepo.findById(id).orElse(null);
        if (existing == null) return false;
        userRepo.deleteById(id);
        return true;
    }
}