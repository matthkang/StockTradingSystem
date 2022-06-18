/*
package com.example.stocktradingsystem.model;

import javax.persistence.*;

@Entity // persistent java class
@Table(name = "user_stocks")
public class UserStock {
    @Id
    @ManyToOne
    private User user;

    @Id
    @ManyToOne
    private Stock stock;

    public UserStock() {
    }

    public UserStock(User user, Stock stock) {
        this.user = user;
        this.stock = stock;
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

    @Override
    public String toString() {
        return "UserStocks{" +
                ", user=" + user +
                ", stock=" + stock +
                '}';
    }

}
*/
