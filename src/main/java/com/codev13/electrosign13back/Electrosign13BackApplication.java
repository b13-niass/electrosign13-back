package com.codev13.electrosign13back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
public class Electrosign13BackApplication {

    public static void main(String[] args) {
        SpringApplication.run(Electrosign13BackApplication.class, args);
    }

}
