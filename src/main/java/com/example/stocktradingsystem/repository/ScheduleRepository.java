package com.example.stocktradingsystem.repository;

import com.example.stocktradingsystem.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT u.open_time FROM Schedule u WHERE u.day = ?1")
    public int getOpenTimeByDay(String day);

    @Query("SELECT u.close_time FROM Schedule u WHERE u.day = ?1")
    public int getCloseTimeByDay(String day);
}