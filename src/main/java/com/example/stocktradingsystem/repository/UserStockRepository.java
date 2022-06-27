package com.example.stocktradingsystem.repository;

import com.example.stocktradingsystem.model.User;
import com.example.stocktradingsystem.model.UserStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStockRepository extends JpaRepository<UserStock, Long> {
    @Query("SELECT u FROM UserStock u WHERE u.user = ?1")
    public List<UserStock> findAllByUsername(User user);

    @Query("SELECT u FROM UserStock u WHERE u.marketOrLimit = 'limit'")
    public List<UserStock> findAllByMarketLimit();

    @Query("SELECT DISTINCT(u.stock.ticker) FROM UserStock u WHERE u.user = ?1")
    public List<String> findDistinctStockTickers(User user);

    @Query("SELECT sum(u.amount) FROM UserStock u WHERE u.stock.ticker = ?1 AND u.fulfilled = 'true' AND u.buyOrSell = 'buy'")
    public Double findNumBuys(String ticker);

    @Query("SELECT sum(u.amount) FROM UserStock u WHERE u.stock.ticker = ?1 AND u.fulfilled = 'true' AND u.buyOrSell = 'sell'")
    public Double findNumSells(String ticker);

    @Query("SELECT sum(u.amount * u.desiredPrice) FROM UserStock u WHERE u.fulfilled = 'true' AND u.stock.ticker = ?1")
    public Double findEquity(String ticker);
}
