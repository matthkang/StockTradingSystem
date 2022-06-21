package com.example.stocktradingsystem.loader;

import com.example.stocktradingsystem.model.Schedule;
import com.example.stocktradingsystem.model.User;
import com.example.stocktradingsystem.repository.ScheduleRepository;
import com.example.stocktradingsystem.repository.StockRepository;
import com.example.stocktradingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private UserRepository userRepository;

    public void run(ApplicationArguments args) {
        User user = new User("user", "user", "user", "$2a$10$t4yEICuRGn3f6UhBoMfTVOaA1dgyje.1WPqeuaAd0dCdWy6r56OrS","user@email.com" ,"ROLE_USER");
        User admin = new User("admin", "admin", "admin", "$2a$10$t4yEICuRGn3f6UhBoMfTVOaA1dgyje.1WPqeuaAd0dCdWy6r56OrS","admin@email.com" ,"ROLE_ADMIN");
        userRepository.save(user);
        userRepository.save(admin);

        scheduleRepository.save(new Schedule("mon", 9, 16));
        scheduleRepository.save(new Schedule("tue", 9, 16));
        scheduleRepository.save(new Schedule("wed", 9, 16));
        scheduleRepository.save(new Schedule("thu", 9, 16));
        scheduleRepository.save(new Schedule("fri", 9, 16));
    }
}
