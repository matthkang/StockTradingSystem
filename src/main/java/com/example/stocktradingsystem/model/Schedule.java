package com.example.stocktradingsystem.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalTime;

@Entity // persistent java class
@Table(name = "schedule")
public class Schedule {
    @Id // primary key
    private String day;

    private LocalTime open_time;

    private LocalTime close_time;

    public Schedule() {
    }

    public Schedule(String day, LocalTime open_time, LocalTime close_time) {
        this.day = day;
        this.open_time = open_time;
        this.close_time = close_time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public LocalTime getOpen_time() {
        return open_time;
    }

    public void setOpen_time(LocalTime open_time) {
        this.open_time = open_time;
    }

    public LocalTime getClose_time() {
        return close_time;
    }

    public void setClose_time(LocalTime close_time) {
        this.close_time = close_time;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "day='" + day + '\'' +
                ", open_time=" + open_time +
                ", close_time=" + close_time +
                '}';
    }
}
