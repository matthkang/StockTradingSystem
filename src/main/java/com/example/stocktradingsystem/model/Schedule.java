package com.example.stocktradingsystem.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity // persistent java class
@Table(name = "schedule")
public class Schedule {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK generation strategy: auto-increment
    private Long id;

    @NotNull
    private int mon_open;
    @NotNull
    private int mon_close;
    @NotNull
    private int tues_open;
    @NotNull
    private int tues_close;
    @NotNull
    private int wed_open;
    @NotNull
    private int wed_close;
    @NotNull
    private int thurs_open;
    @NotNull
    private int thurs_close;
    @NotNull
    private int fri_open;
    @NotNull
    private int fri_close;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMon_open() {
        return mon_open;
    }

    public void setMon_open(int mon_open) {
        this.mon_open = mon_open;
    }

    public int getMon_close() {
        return mon_close;
    }

    public void setMon_close(int mon_close) {
        this.mon_close = mon_close;
    }

    public int getTues_open() {
        return tues_open;
    }

    public void setTues_open(int tues_open) {
        this.tues_open = tues_open;
    }

    public int getTues_close() {
        return tues_close;
    }

    public void setTues_close(int tues_close) {
        this.tues_close = tues_close;
    }

    public int getWed_open() {
        return wed_open;
    }

    public void setWed_open(int wed_open) {
        this.wed_open = wed_open;
    }

    public int getWed_close() {
        return wed_close;
    }

    public void setWed_close(int wed_close) {
        this.wed_close = wed_close;
    }

    public int getThurs_open() {
        return thurs_open;
    }

    public void setThurs_open(int thurs_open) {
        this.thurs_open = thurs_open;
    }

    public int getThurs_close() {
        return thurs_close;
    }

    public void setThurs_close(int thurs_close) {
        this.thurs_close = thurs_close;
    }

    public int getFri_open() {
        return fri_open;
    }

    public void setFri_open(int fri_open) {
        this.fri_open = fri_open;
    }

    public int getFri_close() {
        return fri_close;
    }

    public void setFri_close(int fri_close) {
        this.fri_close = fri_close;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", mon_open=" + mon_open +
                ", mon_close=" + mon_close +
                ", tues_open=" + tues_open +
                ", tues_close=" + tues_close +
                ", wed_open=" + wed_open +
                ", wed_close=" + wed_close +
                ", thurs_open=" + thurs_open +
                ", thurs_close=" + thurs_close +
                ", fri_open=" + fri_open +
                ", fri_close=" + fri_close +
                '}';
    }
}
