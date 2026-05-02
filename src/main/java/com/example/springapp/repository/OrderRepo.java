package com.example.springapp.repository;



import com.example.springapp.model.Order;
import com.example.springapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);
}
