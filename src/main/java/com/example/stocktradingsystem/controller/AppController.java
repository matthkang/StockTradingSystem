package com.example.stocktradingsystem.controller;

import com.example.stocktradingsystem.model.Schedule;
import com.example.stocktradingsystem.model.Stock;
import com.example.stocktradingsystem.model.User;
import com.example.stocktradingsystem.repository.ScheduleRepository;
import com.example.stocktradingsystem.repository.StockRepository;
import com.example.stocktradingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class AppController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

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

    @GetMapping("/stocks")
    public String listUsers(Model model) {
        List<Stock> listStocks = stockRepository.findAll();
        model.addAttribute("listStocks", listStocks);
        return "stocks";
    }

    @PostMapping("/buy")
    public String buyStock(@RequestParam Stock thisStock, Principal principal, Model model) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        Set<Stock> stocks = user.getStocks();
        stocks.add(thisStock);
        user.setStocks(stocks);

        userRepository.save(user);

        List<Stock> listStocks = stockRepository.findAll();
        model.addAttribute("listStocks", listStocks);
        return "stocks";
    }
}