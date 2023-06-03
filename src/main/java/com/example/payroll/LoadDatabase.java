package com.example.payroll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.payroll.CommonTypes.OrderStatus;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository) {
        return args -> {
            log.info("Preloading " + employeeRepository.save(new Employee("Silan", "Yakut", "Doctor")));
            log.info("Preloading " + employeeRepository.save(new Employee("Rutkay", "Karabulak", "Sex machine")));
            log.info("Preloading " + orderRepository.save((new Order("Philipps AirFryer", OrderStatus.IN_PROGRESS))));
            log.info("Preloading " + orderRepository.save((new Order("Air Jordan", OrderStatus.COMPLETED))));
          };
    }

}
