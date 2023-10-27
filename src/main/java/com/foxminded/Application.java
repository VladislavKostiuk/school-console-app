package com.foxminded;

import com.foxminded.ui.SchoolApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.foxminded"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args).getBean(SchoolApplication.class).showMenu();
    }
}
