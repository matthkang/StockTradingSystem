package com.example.stocktradingsystem.model;

import javax.persistence.*;

@Entity // persistent java class
@Table(name = "schedule")
public class Schedule {
    @Id // primary key
    private String day;

    private Integer open_time;

    private Integer close_time;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getOpen_time() {
        return open_time;
    }

    public void setOpen_time(Integer open_time) {
        this.open_time = open_time;
    }

    public Integer getClose_time() {
        return close_time;
    }

    public void setClose_time(Integer close_time) {
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
