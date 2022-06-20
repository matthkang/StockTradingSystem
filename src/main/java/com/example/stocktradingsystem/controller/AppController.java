package com.example.stocktradingsystem.controller;

import com.example.stocktradingsystem.model.Schedule;
import com.example.stocktradingsystem.model.Stock;
import com.example.stocktradingsystem.model.User;
import com.example.stocktradingsystem.model.UserStock;
import com.example.stocktradingsystem.repository.ScheduleRepository;
import com.example.stocktradingsystem.repository.StockRepository;
import com.example.stocktradingsystem.repository.UserRepository;
import com.example.stocktradingsystem.repository.UserStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public User returnCurrUser(Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        return user;
    }

    public void listMarketUserStocks(Principal principal, Model model){
        User user = returnCurrUser(principal);

        List<Stock> listStocks = stockRepository.findAll();
        model.addAttribute("listStocks", listStocks);

        List<UserStock> userStocks = userStockRepository.findAllByUsername(user);
        model.addAttribute("userStocks", userStocks);

        List<String> distinctStocks = userStockRepository.findDistinctStockTickers();
        HashMap<String, Double> map = new HashMap<>();
        for (String ticker: distinctStocks){
            Double numBuys = userStockRepository.findNumBuys(ticker);
            Double numSells = userStockRepository.findNumSells(ticker);
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
        stockRepository.save(stock);
        return "add_success";
    }

    @PostMapping("/changeSchedule")
    public String changeSchedule(@ModelAttribute("schedule") Schedule schedule) {
        scheduleRepository.save(schedule);
        return "schedule_success";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

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
        User user = returnCurrUser(principal);

        Double price = thisStock.getInit_price();
        if (!marketBuyAmount.isEmpty()){
            Double amount = Double.parseDouble(marketBuyAmount);
            userStockRepository.save(new UserStock(user, thisStock, price, price, "buy", "market", new Date(), null, amount, "true"));
        }

        listMarketUserStocks(principal, model);
        return "redirect:customer";
    }

    @PostMapping("/marketSell")
    public String marketSellStock(@RequestParam Stock thisStock, @RequestParam String marketSellAmount, Principal principal, Model model) {
        User user = returnCurrUser(principal);
        Double price = thisStock.getInit_price();
        listMarketUserStocks(principal, model);

        if (!marketSellAmount.isEmpty()){
            Double amount = Double.parseDouble(marketSellAmount);
            Double currShares = userStockRepository.findNumBuys(thisStock.getTicker()) - userStockRepository.findNumSells(thisStock.getTicker());

            if (amount <= currShares){
                userStockRepository.save(new UserStock(user, thisStock, price, price, "sell", "market", new Date(), null, amount, "true"));
                return "redirect:customer";
            }
        }
        return "error_selling";
    }

    @PostMapping("/limitBuy")
    public String limitBuyStock(@RequestParam Stock thisStock, @RequestParam String limitBuyPrice,
                                @RequestParam String limitBuyAmount, @RequestParam String limitBuyDate,
                                Principal principal, Model model) throws ParseException {
        User user = returnCurrUser(principal);

        if (thisStock != null && !limitBuyPrice.isEmpty() && !limitBuyAmount.isEmpty() && !limitBuyDate.isEmpty()) {
            Double initPrice = thisStock.getInit_price();
            Double amount = Double.parseDouble(limitBuyAmount);
            Double desiredPrice = Double.parseDouble(limitBuyPrice);
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(limitBuyDate);
            userStockRepository.save(new UserStock(user, thisStock, initPrice, desiredPrice, "buy", "limit", new Date(), date, amount, "false"));
        }

        listMarketUserStocks(principal, model);
        return "redirect:customer";
    }

    @PostMapping("/limitSell")
    public String limitSellStock(@RequestParam Stock thisStock, @RequestParam String limitSellPrice,
                                @RequestParam String limitSellAmount, @RequestParam String limitSellDate,
                                Principal principal, Model model) throws ParseException {
        User user = returnCurrUser(principal);

        listMarketUserStocks(principal, model);
        if (thisStock != null && !limitSellPrice.isEmpty() && !limitSellAmount.isEmpty() && !limitSellDate.isEmpty()) {
            Double initPrice = thisStock.getInit_price();
            Double amount = Double.parseDouble(limitSellAmount);
            Double desiredPrice = Double.parseDouble(limitSellPrice);
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(limitSellDate);
            Double currShares = userStockRepository.findNumBuys(thisStock.getTicker()) - userStockRepository.findNumSells(thisStock.getTicker());

            if (amount <= currShares){
                userStockRepository.save(new UserStock(user, thisStock, initPrice, desiredPrice, "sell", "limit", new Date(), date, amount, "false"));
                return "redirect:customer";
            }
        }
        return "error_selling";
    }

    @PostMapping("/cancelOrder")
    public String cancelLimitOrder(@RequestParam UserStock thisStock, Principal principal, Model model){
        Long id = thisStock.getId();
        userStockRepository.deleteById(id);

        listMarketUserStocks(principal, model);
        return "redirect:customer";
    }
}