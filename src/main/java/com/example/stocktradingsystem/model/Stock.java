package com.example.stocktradingsystem.model;

import javax.persistence.*;

@Entity // persistent java class
@Table(name = "stocks")
public class Stock {
    @Id // primary key
    private String ticker;

    private String co_name;

    private Double volume;

    private Double init_price;

    private Double high;

    private Double low;

    @ManyToOne
    private User user;

    public Stock() {
    }

    public Stock(String ticker, String co_name, Double volume, Double init_price, Double high, Double low) {
        this.ticker = ticker;
        this.co_name = co_name;
        this.volume = volume;
        this.init_price = init_price;
        this.high = high;
        this.low = low;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getCo_name() {
        return co_name;
    }

    public void setCo_name(String co_name) {
        this.co_name = co_name;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getInit_price() {
        return init_price;
    }

    public void setInit_price(Double init_price) {
        this.init_price = init_price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "ticker='" + ticker + '\'' +
                ", co_name='" + co_name + '\'' +
                ", volume=" + volume +
                ", init_price=" + init_price +
                ", high=" + high +
                ", low=" + low +
                ", user=" + user +
                '}';
    }
}
