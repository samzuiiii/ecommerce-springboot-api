package com.example.springapp.repository;

import com.example.springapp.model.Cart;
import com.example.springapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {

    Cart findByUser(User user);
}