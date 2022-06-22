package com.example.stocktradingsystem.service;

import com.example.stocktradingsystem.model.Stock;
import com.example.stocktradingsystem.model.User;
import com.example.stocktradingsystem.model.UserStock;
import com.example.stocktradingsystem.repository.ScheduleRepository;
import com.example.stocktradingsystem.repository.StockRepository;
import com.example.stocktradingsystem.repository.UserRepository;
import com.example.stocktradingsystem.repository.UserStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class AppService {
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private UserStockRepository userStockRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

    public User returnCurrUser(Principal principal){
        if (principal != null){
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            return user;
        }
        return null;
    }

    public boolean marketIsOpen() {
        String currDay = new SimpleDateFormat("EEE").format(new Date()).toLowerCase();

        LocalTime openTime = scheduleRepository.getOpenTimeByDay(currDay);
        LocalTime closedTime = scheduleRepository.getCloseTimeByDay(currDay);
        LocalTime currTime = LocalTime.now();

        // if curr hour is at or past opening hour
        // AND if curr hour is before closing hour
        if (currTime.isAfter(openTime) && currTime.isBefore(closedTime)){
            return true;
        }
        else {
            return false;
        }
    }

    // randomize stock prices executed every x seconds
    // only randomize when market is open
    @Scheduled(fixedRate = 30000)
    public void randomizePrice() throws ParseException {
        List<Stock> stocks = stockRepository.findAll();
        if (marketIsOpen()) {
            for (Stock stock : stocks) {
                Random r = new Random();
                // rand number from -0.05 to 0.05
                Double percentage = -0.05 + (0.05 - (-0.05)) * r.nextDouble();
                Double newPrice = stock.getInit_price() * (1 + percentage);
                newPrice = formatDecimal(newPrice);

                // update low or high
                if (newPrice < stock.getLow()) {
                    stock.setLow(newPrice);
                } else if (newPrice > stock.getHigh()) {
                    stock.setHigh(newPrice);
                }

                // update stock with new price
                stock.setInit_price(newPrice);
                stockRepository.save(stock);

                // debugging purposes
                System.out.println("stock: " + stock.getTicker());
                System.out.println("new price: " + newPrice);
                System.out.println();

                // update user_stocks with new price
                List<UserStock> userStocks = userStockRepository.findAll();
                for (UserStock userStock : userStocks) {
                    if (userStock.getStock().getTicker().equals(stock.getTicker())) {
                        userStock.setInitPrice(newPrice);
                        userStockRepository.save(userStock);
                    }
                }

                List<UserStock> limitStocks = userStockRepository.findAllByMarketLimit();
                for (UserStock limitStock : limitStocks) {
                    User user = limitStock.getUser();
                    // FOR A LIMIT BUY:
                    // if new price is equal to desired price with buffer of 2.5% of market price
                    // and if curr date is before or equal to expired date
                    // and buy has not been fulfilled yet
                    if ((Math.abs(newPrice - limitStock.getDesiredPrice()) <= 0.025 * newPrice) &&
                            new Date().compareTo(limitStock.getExpireDate()) <= 0 &&
                            limitStock.getFulfilled().equals("false") &&
                            limitStock.getBuyOrSell().equals("buy")) {
                        limitStock.setFulfilled("true");
                        userStockRepository.save(limitStock);

                        // remove bought amount from funds
                        Double totalPrice = limitStock.getDesiredPrice() * limitStock.getAmount();
                        // check whether user has enough funds in wallet to buy
                        if (enoughBalance(user, totalPrice)){
                            Double currWallet = user.getWallet();
                            currWallet -= totalPrice;
                            user.setWallet(currWallet);
                            userRepository.save(user);
                        }
                    }
                    // FOR A LIMIT SELL:
                    // if new price is equal to desired price with buffer of 2.5% of market price
                    // and if curr date is before or equal to expired date
                    // and sell has not been fulfilled yet
                    if ((Math.abs(newPrice - limitStock.getDesiredPrice()) <= 0.025 * newPrice) &&
                            new Date().compareTo(limitStock.getExpireDate()) <= 0 &&
                            limitStock.getFulfilled().equals("false") &&
                            limitStock.getBuyOrSell().equals("sell")) {
                        limitStock.setFulfilled("true");
                        userStockRepository.save(limitStock);

                        // add sold amount to funds
                        Double totalPrice = limitStock.getDesiredPrice() * limitStock.getAmount();
                        // check whether user has enough funds in wallet to buy
                        if (enoughBalance(user, totalPrice)){
                            Double currWallet = user.getWallet();
                            currWallet += totalPrice;
                            user.setWallet(currWallet);
                            userRepository.save(user);
                        }
                    }
                }
            }
        }
    }


    public Double formatDecimal(Double d){
        DecimalFormat df = new DecimalFormat("#.##");
        d = Double.valueOf(df.format(d));
        return d;
    }

    public boolean enoughBalance(User user, Double price){
        Double currWallet = user.getWallet();
        if (currWallet >= price){
            return true;
        }
        else return false;
    }
}