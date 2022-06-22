package com.example.stocktradingsystem.controller;

import com.example.stocktradingsystem.model.Schedule;
import com.example.stocktradingsystem.model.Stock;
import com.example.stocktradingsystem.model.User;
import com.example.stocktradingsystem.model.UserStock;
import com.example.stocktradingsystem.repository.ScheduleRepository;
import com.example.stocktradingsystem.repository.StockRepository;
import com.example.stocktradingsystem.repository.UserRepository;
import com.example.stocktradingsystem.repository.UserStockRepository;
import com.example.stocktradingsystem.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class AppController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private UserStockRepository userStockRepository;

    @Autowired
    AppService appService;

    public void listMarketUserStocks(Principal principal, Model model){
        User user = appService.returnCurrUser(principal);
        model.addAttribute("currUser", user);

        if (appService.marketIsOpen()){
            String marketOpen = "open";
            model.addAttribute("marketStatus", marketOpen);
            // market
            List<Stock> listStocks = stockRepository.findAll();
            model.addAttribute("listStocks", listStocks);
        }
        else {
            String marketClosed = "closed";
            model.addAttribute("marketStatus", marketClosed);
        }

        // transaction history
        List<UserStock> userStocks = userStockRepository.findAllByUsername(user);
        model.addAttribute("userStocks", userStocks);

        // portfolio
        List<String> distinctStocks = userStockRepository.findDistinctStockTickers();
        // ticker, shares
        HashMap<String, Double> map = new HashMap<>();
        for (String ticker: distinctStocks){
            Double numBuys = userStockRepository.findNumBuys(ticker);
            Double numSells = userStockRepository.findNumSells(ticker);
            if (numBuys == null){
                numBuys = 0.0;
            }
            if (numSells == null){
                numSells = 0.0;
            }
            Double total = numBuys - numSells;
            // only add to map if shares is greater than 0
            if (total > 0){
                map.put(ticker, total);
            }
        }
        model.addAttribute("map", map);
    }

    @GetMapping("")
    public String viewHomePage(){
        return "index";
    }

    @GetMapping("/login")
    public String viewLoginPage(){
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "signup_form";
    }

    @GetMapping("/admin")
    public String getAdminPage() {
        return "admin";
    }

    @PostMapping("/admin")
    public String submitForm(@ModelAttribute("stock") Stock stock) {
        Double init_price = stock.getInit_price();
        stock.setHigh(init_price);
        stock.setLow(init_price);
        stockRepository.save(stock);
        return "add_success";
    }

    @PostMapping("/changeSchedule")
    public String changeSchedule(@RequestParam("day") String day,
                                 @RequestParam("open_time") String openTime,
                                 @RequestParam("close_time") String closeTime) throws ParseException {
        Schedule schedule = new Schedule();
        schedule.setDay(day);

        LocalTime open = LocalTime.parse(openTime);
        LocalTime close = LocalTime.parse(closeTime);

        schedule.setOpen_time(open);
        schedule.setClose_time(close);
        scheduleRepository.save(schedule);
        return "admin";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        user.setWallet(0.00);

        userRepository.save(user);
        return "register_success";
    }

    @GetMapping("/customer")
    public String customerPage(Model model, Principal principal) {
        listMarketUserStocks(principal, model);
        return "customer";
    }

    @PostMapping("/marketBuy")
    public String marketBuyStock(@RequestParam Stock thisStock, @RequestParam String marketBuyAmount, Principal principal, Model model) {
        User user = appService.returnCurrUser(principal);

        Double price = thisStock.getInit_price();
        if (!marketBuyAmount.isEmpty()){
            Double amount = Double.parseDouble(marketBuyAmount);
            Double totalPrice = price * amount;
            // check whether user has enough funds in wallet to buy
            if (appService.enoughBalance(user, totalPrice)){
                userStockRepository.save(new UserStock(user, thisStock, price, price, "buy", "market", new Date(), null, amount, "true"));
                // remove bought amount from funds
                Double currWallet = user.getWallet();
                currWallet -= totalPrice;
                user.setWallet(currWallet);
                userRepository.save(user);
            }
            else {
                return "insufficient_funds";
            }
        }

        listMarketUserStocks(principal, model);
        return "redirect:customer";
    }

    @PostMapping("/marketSell")
    public String marketSellStock(@RequestParam Stock thisStock, @RequestParam String marketSellAmount, Principal principal, Model model) {
        User user = appService.returnCurrUser(principal);
        Double price = thisStock.getInit_price();
        listMarketUserStocks(principal, model);

        if (!marketSellAmount.isEmpty()){
            Double amount = Double.parseDouble(marketSellAmount);

            // ensure that user has more stocks than they want to sell
            Double numBuys = userStockRepository.findNumBuys(thisStock.getTicker());
            Double numSells = userStockRepository.findNumSells(thisStock.getTicker());
            if (numBuys == null){
                numBuys = 0.0;
            }
            if (numSells == null){
                numSells = 0.0;
            }
            Double currShares = numBuys - numSells;

            if (amount <= currShares){
                userStockRepository.save(new UserStock(user, thisStock, price, price, "sell", "market", new Date(), null, amount, "true"));
                Double volume = thisStock.getVolume();
                volume += amount;
                thisStock.setVolume(volume);
                stockRepository.save(thisStock);

                // add sold amount to funds
                Double totalPrice = price * amount;
                Double currWallet = user.getWallet();
                currWallet += totalPrice;
                user.setWallet(currWallet);
                userRepository.save(user);
            }
            else {
                return "error_selling";
            }
        }
        return "redirect:customer";
    }

    @PostMapping("/limitBuy")
    public String limitBuyStock(@RequestParam Stock thisStock, @RequestParam String limitBuyPrice,
                                @RequestParam String limitBuyAmount, @RequestParam String limitBuyDate,
                                Principal principal, Model model) throws ParseException {
        User user = appService.returnCurrUser(principal);

        if (thisStock != null && !limitBuyPrice.isEmpty() &&
                !limitBuyAmount.isEmpty() && !limitBuyDate.isEmpty() &&
                appService.marketIsOpen()) {
            Double initPrice = thisStock.getInit_price();
            Double amount = Double.parseDouble(limitBuyAmount);
            Double desiredPrice = Double.parseDouble(limitBuyPrice);
            Double totalPrice = amount * desiredPrice;
            // check whether user has enough funds in wallet to buy
            if (appService.enoughBalance(user, totalPrice)){
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(limitBuyDate);
                userStockRepository.save(new UserStock(user, thisStock, initPrice, desiredPrice, "buy", "limit", new Date(), date, amount, "false"));
            }
            else {
                return "insufficient_funds";
            }
        }

        listMarketUserStocks(principal, model);
        return "redirect:customer";
    }

    @PostMapping("/limitSell")
    public String limitSellStock(@RequestParam Stock thisStock, @RequestParam String limitSellPrice,
                                @RequestParam String limitSellAmount, @RequestParam String limitSellDate,
                                Principal principal, Model model) throws ParseException {
        User user = appService.returnCurrUser(principal);

        listMarketUserStocks(principal, model);
        if (thisStock != null && !limitSellPrice.isEmpty() &&
                !limitSellAmount.isEmpty() && !limitSellDate.isEmpty() &&
                appService.marketIsOpen()) {
            Double initPrice = thisStock.getInit_price();
            Double amount = Double.parseDouble(limitSellAmount);
            Double desiredPrice = Double.parseDouble(limitSellPrice);
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(limitSellDate);

            // ensure that user has more stocks than they want to sell
            Double numBuys = userStockRepository.findNumBuys(thisStock.getTicker());
            Double numSells = userStockRepository.findNumSells(thisStock.getTicker());
            if (numBuys == null){
                numBuys = 0.0;
            }
            if (numSells == null){
                numSells = 0.0;
            }
            Double currShares = numBuys - numSells;

            if (amount <= currShares){
                userStockRepository.save(new UserStock(user, thisStock, initPrice, desiredPrice, "sell", "limit", new Date(), date, amount, "false"));
            }
            else {
                return "error_selling";
            }
        }
        return "redirect:customer";
    }

    @PostMapping("/cancelOrder")
    public String cancelLimitOrder(@RequestParam UserStock thisStock, Principal principal, Model model){
        Long id = thisStock.getId();
        userStockRepository.deleteById(id);

        listMarketUserStocks(principal, model);
        return "redirect:customer";
    }

    @PostMapping("/depositFunds")
    public String depositFunds(@RequestParam String depositAmount, Principal principal, Model model){
        User user = appService.returnCurrUser(principal);
        Double currAmount = user.getWallet();
        if (!depositAmount.isEmpty()){
            Double deposit = Double.parseDouble(depositAmount);
            currAmount += deposit;
            DecimalFormat df = new DecimalFormat("#.##");
            currAmount = Double.valueOf(df.format(currAmount));
            currAmount = appService.formatDecimal(currAmount);
            user.setWallet(currAmount);
            userRepository.save(user);
        }

        listMarketUserStocks(principal, model);
        return "redirect:customer";
    }

    @PostMapping("/withdrawFunds")
    public String withdrawFunds(@RequestParam String withdrawAmount, Principal principal, Model model){
        User user = appService.returnCurrUser(principal);
        Double currAmount = user.getWallet();
        if (!withdrawAmount.isEmpty()){
            Double deposit = Double.parseDouble(withdrawAmount);
            currAmount -= deposit;
            DecimalFormat df = new DecimalFormat("#.##");
            currAmount = Double.valueOf(df.format(currAmount));
            currAmount = appService.formatDecimal(currAmount);
            user.setWallet(currAmount);
            userRepository.save(user);
        }

        listMarketUserStocks(principal, model);
        return "redirect:customer";
    }
}