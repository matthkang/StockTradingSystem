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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        return user;
    }

    public boolean marketIsOpen(){
        String currDay = new SimpleDateFormat("EEE").format(new Date()).toLowerCase();

        Integer openTime = scheduleRepository.getOpenTimeByDay(currDay);
        Integer closedTime = scheduleRepository.getCloseTimeByDay(currDay);

        int currHour = LocalDateTime.now().getHour();

        // if curr hour is at or past opening hour
        // AND if curr hour is before closing hour
        if (currHour >= openTime && currHour < closedTime){
            return true;
        }
        else {
            return false;
        }
    }

    //public void updateHighLow()

    // randomize stock prices executed every x seconds
    @Scheduled(fixedRate = 15000)
    public void randomizePrice() {
        List<Stock> stocks = stockRepository.findAll();
        for (Stock stock : stocks) {
            Random r = new Random();
            // rand number from -0.05 to 0.05
            double percentage = -0.05 + (0.05 - (-0.05)) * r.nextDouble();
            Double newPrice = stock.getInit_price() * (1 + percentage);
            DecimalFormat df = new DecimalFormat("#.##");
            newPrice = Double.valueOf(df.format(newPrice));

            // update stock with new price
            stock.setInit_price(newPrice);
            stockRepository.save(stock);

            // debugging purposes
            System.out.println("stock: " + stock.getTicker());
            System.out.println("new price: " + newPrice);
            System.out.println();

            // update user_stocks with new price
            List<UserStock> userStocks = userStockRepository.findAll();
            for (UserStock userStock: userStocks){
                if (userStock.getStock().getTicker().equals(stock.getTicker())){
                    userStock.setInitPrice(newPrice);
                    userStockRepository.save(userStock);
                }
            }

            List<UserStock> limitStocks = userStockRepository.findAllByMarketLimit();
            for (UserStock limitStock: limitStocks){
                // FOR A LIMIT BUY:
                // if new price is less than or equal to desired price
                // and if curr date is before or equal to expired date
                // and buy has not been fulfilled yet
                if (newPrice <= limitStock.getDesiredPrice() &&
                        new Date().compareTo(limitStock.getExpireDate()) <= 0 &&
                        limitStock.isFulfilled().equals("false") &&
                        limitStock.getBuyOrSell().equals("buy")){
                    limitStock.setFulfilled("true");
                    userStockRepository.save(limitStock);
                }
                // FOR A LIMIT SELL:
                // if new price is greater than or equal to desired price
                // and if curr date is before or equal to expired date
                // and sell has not been fulfilled yet
                if (newPrice >= limitStock.getDesiredPrice() &&
                        new Date().compareTo(limitStock.getExpireDate()) <= 0 &&
                        limitStock.isFulfilled().equals("false") &&
                        limitStock.getBuyOrSell().equals("sell")){
                    limitStock.setFulfilled("true");
                    userStockRepository.save(limitStock);
                }
            }
        }
    }
}