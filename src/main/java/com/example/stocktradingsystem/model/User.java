package com.example.stocktradingsystem.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity // persistent java class
@Table(name = "users")
public class User {
    @Id // primary key
    private String username;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;


    @NotNull
    private String password;

    @NotNull
    @Column(unique=true)
    private String email;

    private String role;

    private Double wallet;

    @OneToMany(mappedBy = "user")
    private List<Stock> stocks = new ArrayList<>();

    public User () {
        this.role = "ROLE_USER";
    }

    public User(String username, String firstName, String lastName, String password, String email, String role, Double wallet) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.role = role;
        this.wallet = wallet;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean hasRole(String roleName) {
        if (this.getRole().equals(roleName)) {
            return true;
        }
        return false;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public Double getWallet() {
        return wallet;
    }

    public void setWallet(Double wallet) {
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", wallet=" + wallet +
                ", stocks=" + stocks +
                '}';
    }
}
