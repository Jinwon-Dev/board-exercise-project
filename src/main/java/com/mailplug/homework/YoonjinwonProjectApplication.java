package com.mailplug.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class YoonjinwonProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(YoonjinwonProjectApplication.class, args);
    }

}
