package com.example.stocktradingsystem.model;

import javax.persistence.*;

@Entity // persistent java class
@Table(name = "user_stocks")
public class UserStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="username")
    private User user;

    @ManyToOne
    @JoinColumn(name="stock")
    private Stock stock;

    private Double boughtPrice;

    private String buyOrSell;

    public UserStock() {
    }

    public UserStock(User user, Stock stock, Double boughtPrice, String buyOrSell) {
        this.user = user;
        this.stock = stock;
        this.boughtPrice = boughtPrice;
        this.buyOrSell = buyOrSell;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Double getBoughtPrice() {
        return boughtPrice;
    }

    public void setBoughtPrice(Double boughtPrice) {
        this.boughtPrice = boughtPrice;
    }

    public String getBuyOrSell() {
        return buyOrSell;
    }

    public void setBuyOrSell(String buyOrSell) {
        this.buyOrSell = buyOrSell;
    }

    @Override
    public String toString() {
        return "UserStock{" +
                "id=" + id +
                ", user=" + user +
                ", stock=" + stock +
                ", boughtPrice=" + boughtPrice +
                ", buyOrSell='" + buyOrSell + '\'' +
                '}';
    }
}
