package com.example.springapp.service;

import com.example.springapp.model.User;
import java.util.List;

public interface UserService {
    User addUser(User user);
    List<User> getAllUsers();
    User getUserById(Long id);
    User updateUser(Long id, User user);
    boolean deleteUser(Long id);
}