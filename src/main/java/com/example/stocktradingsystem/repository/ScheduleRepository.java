package com.example.stocktradingsystem.repository;

import com.example.stocktradingsystem.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, String> {
    @Query("SELECT u.open_time FROM Schedule u WHERE u.day = ?1")
    public LocalTime getOpenTimeByDay(String day);

    @Query("SELECT u.close_time FROM Schedule u WHERE u.day = ?1")
    public LocalTime getCloseTimeByDay(String day);
}