package com.example.stocktradingsystem.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity // persistent java class
@Table(name = "stocks")
public class Stock {
    @Id // primary key
    private String ticker;

    @NotNull
    private String co_name;

    @NotNull
    private Double volume;

    @NotNull
    private Double init_price;

    public Stock() {}

    public Stock(String ticker, String co_name, Double volume, Double init_price) {
        this.ticker = ticker;
        this.co_name = co_name;
        this.volume = volume;
        this.init_price = init_price;
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

    @Override
    public String toString() {
        return "Stock{" +
                "ticker='" + ticker + '\'' +
                ", co_name='" + co_name + '\'' +
                ", volume=" + volume +
                ", init_price=" + init_price +
                '}';
    }
}
