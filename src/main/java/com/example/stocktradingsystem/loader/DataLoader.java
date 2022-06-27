package com.example.stocktradingsystem.loader;

import com.example.stocktradingsystem.model.Schedule;
import com.example.stocktradingsystem.model.Stock;
import com.example.stocktradingsystem.model.User;
import com.example.stocktradingsystem.repository.ScheduleRepository;
import com.example.stocktradingsystem.repository.StockRepository;
import com.example.stocktradingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StockRepository stockRepository;

    public void run(ApplicationArguments args) {
        User user = new User("user", "user", "user", "$2a$10$t4yEICuRGn3f6UhBoMfTVOaA1dgyje.1WPqeuaAd0dCdWy6r56OrS","user@email.com" ,"ROLE_USER", 0.00);
        User admin = new User("admin", "admin", "admin", "$2a$10$t4yEICuRGn3f6UhBoMfTVOaA1dgyje.1WPqeuaAd0dCdWy6r56OrS","admin@email.com" ,"ROLE_ADMIN", 0.00);
        if (!userRepository.existsById("user")){
            userRepository.save(user);
        }
        if (!userRepository.existsById("admin")){
            userRepository.save(admin);
        }

        if (!scheduleRepository.existsById("mon")){
            scheduleRepository.save(new Schedule("mon", LocalTime.parse("09:30"), LocalTime.parse("16:00")));
        }
        if (!scheduleRepository.existsById("tue")){
            scheduleRepository.save(new Schedule("tue", LocalTime.parse("09:30"), LocalTime.parse("16:00")));
        }
        if (!scheduleRepository.existsById("wed")) {
            scheduleRepository.save(new Schedule("wed", LocalTime.parse("09:30"), LocalTime.parse("16:00")));
        }
        if (!scheduleRepository.existsById("thu")){
            scheduleRepository.save(new Schedule("thu", LocalTime.parse("09:30"), LocalTime.parse("16:00")));
        }
        if (!scheduleRepository.existsById("fri")){
            scheduleRepository.save(new Schedule("fri", LocalTime.parse("09:30"), LocalTime.parse("16:00")));
        }

        if (!scheduleRepository.existsById("sat")){
            scheduleRepository.save(new Schedule("sat", LocalTime.parse("09:30"), LocalTime.parse("23:59")));
        }
        if (!scheduleRepository.existsById("sun")){
            scheduleRepository.save(new Schedule("sun", LocalTime.parse("09:30"), LocalTime.parse("23:59")));
        }

        if (!stockRepository.existsById("GOOG")){
            stockRepository.save(new Stock("GOOG", "Google", 0.0, 2233.45, 0.0, 0.0));
        }
    }
}
