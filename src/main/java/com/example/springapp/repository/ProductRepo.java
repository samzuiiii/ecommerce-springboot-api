package com.example.springapp.repository;

import com.example.springapp.model.Product;
import com.example.springapp.model.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    List<Product> findByProductNameContaining(String name);

    @Query("SELECT p FROM Product p WHERE p.category.categoryName = :categoryName")
    List<Product> findByCategoryName(@Param("categoryName") String categoryName);

    @Query("SELECT oi.product FROM OrderItem oi " +
           "WHERE oi.order IN (" +
           "  SELECT oi2.order FROM OrderItem oi2 " +
           "  WHERE oi2.product.productId = :productId) " +
           "AND oi.product.productId != :productId " +
           "GROUP BY oi.product " +
           "ORDER BY COUNT(oi) DESC")
    List<Product> findRecommendations(
        @Param("productId") Long productId,
        Pageable pageable
    );
}