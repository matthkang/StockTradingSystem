package com.example.stocktradingsystem.controller;

import com.example.stocktradingsystem.model.Schedule;
import com.example.stocktradingsystem.model.Stock;
import com.example.stocktradingsystem.model.User;
import com.example.stocktradingsystem.model.UserStock;
import com.example.stocktradingsystem.repository.ScheduleRepository;
import com.example.stocktradingsystem.repository.StockRepository;
import com.example.stocktradingsystem.repository.UserRepository;
import com.example.stocktradingsystem.repository.UserStockRepository;
import com.example.stocktradingsystem.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

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
    public String buyStock(@RequestParam Stock stock, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        String username = user.getUsername();

        userStockRepository.save(new UserStock(user, stock));

        return "stocks";
    }
}