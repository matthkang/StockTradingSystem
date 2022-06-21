package com.example.stocktradingsystem.service;

import com.example.stocktradingsystem.model.Stock;
import com.example.stocktradingsystem.model.UserStock;
import com.example.stocktradingsystem.repository.StockRepository;
import com.example.stocktradingsystem.repository.UserStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class AppService {
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private UserStockRepository userStockRepository;

    // executed every 5 seconds
    @Scheduled(fixedRate = 15000)
    public void randomizePrice() {
        List<Stock> stocks = stockRepository.findAll();
        for (Stock stock : stocks) {
            Random r = new Random();
            // rand number from -0.15 to 0.15
            double percentage = -0.15 + (0.15 - (-0.15)) * r.nextDouble();
            Double newPrice = stock.getInit_price() * (1 + percentage);
            DecimalFormat df = new DecimalFormat("#.##");
            newPrice = Double.valueOf(df.format(newPrice));
            System.out.println("stock: " + stock.getTicker());
            System.out.println("new price: " + newPrice);
            System.out.println();

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