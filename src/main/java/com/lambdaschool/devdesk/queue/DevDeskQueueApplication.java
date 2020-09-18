package com.lambdaschool.devdesk.queue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DevDeskQueueApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevDeskQueueApplication.class, args);
    }

}
