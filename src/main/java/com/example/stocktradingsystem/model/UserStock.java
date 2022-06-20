package com.example.stocktradingsystem.model;

import javax.persistence.*;
import java.util.Date;

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

    private Double initPrice;

    private Double desiredPrice;

    private String buyOrSell;

    private String marketOrLimit;

    private Date boughtDate;
    private Date expireDate;

    private Double amount;

    private String fulfilled;

    public UserStock() {
    }

    public UserStock(User user, Stock stock, Double initPrice, Double desiredPrice, String buyOrSell, String marketOrLimit, Date boughtDate, Date expireDate, Double amount, String fulfilled) {
        this.user = user;
        this.stock = stock;
        this.initPrice = initPrice;
        this.desiredPrice = desiredPrice;
        this.buyOrSell = buyOrSell;
        this.marketOrLimit = marketOrLimit;
        this.boughtDate = boughtDate;
        this.expireDate = expireDate;
        this.amount = amount;
        this.fulfilled = fulfilled;
    }

    public Double getDesiredPrice() {
        return desiredPrice;
    }

    public void setDesiredPrice(Double desiredPrice) {
        this.desiredPrice = desiredPrice;
    }

    public String getMarketOrLimit() {
        return marketOrLimit;
    }

    public void setMarketOrLimit(String marketOrLimit) {
        this.marketOrLimit = marketOrLimit;
    }

    public Date getBoughtDate() {
        return boughtDate;
    }

    public void setBoughtDate(Date boughtDate) {
        this.boughtDate = boughtDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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

    public Double getInitPrice() {
        return initPrice;
    }

    public void setInitPrice(Double boughtPrice) {
        this.initPrice = boughtPrice;
    }

    public String getBuyOrSell() {
        return buyOrSell;
    }

    public void setBuyOrSell(String buyOrSell) {
        this.buyOrSell = buyOrSell;
    }

    public String isFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(String fulfilled) {
        this.fulfilled = fulfilled;
    }

    @Override
    public String toString() {
        return "UserStock{" +
                "id=" + id +
                ", user=" + user +
                ", stock=" + stock +
                ", initPrice=" + initPrice +
                ", desiredPrice=" + desiredPrice +
                ", buyOrSell='" + buyOrSell + '\'' +
                ", marketOrLimit='" + marketOrLimit + '\'' +
                ", boughtDate=" + boughtDate +
                ", expireDate=" + expireDate +
                ", amount=" + amount +
                ", fulfilled=" + fulfilled +
                '}';
    }
}
