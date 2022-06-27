# Stock Trading System

## Table of contents
* [Description](#description)
* [Technologies](#technologies)
* [Setup](#setup)

## Description
- A stock trading platform where users can buy and sell stocks.
- Supports two types of users: customer of the stock trading platform and the
  administrator of the system. (The administrator is responsible for creating the stocks
  and setting the initial price).


## Technologies
Project is created with:
* Java version: 11
* Maven version: 3.8.5
* MySQL: 8.0.29

## Setup

Only thing that needs to be setup beforehand is to create the database.
```sql
CREATE DATABASE stocktrading;
```
Tables and mock data (users, stocks, schedule) will be preloaded when the application starts.