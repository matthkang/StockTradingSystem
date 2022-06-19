package com.example.stocktradingsystem.repository;

import com.example.stocktradingsystem.model.User;
import com.example.stocktradingsystem.model.UserStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStockRepository extends JpaRepository<UserStock, Long> {
    @Query("SELECT u FROM UserStock u WHERE u.user = ?1")
    public List<UserStock> findAllByUsername(User user);
}
