package com.lambdaschool.devdesk.queue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DevDeskQueueApplication {

    private static String clientId = System.getenv("CLIENTID");
    private static String clientSecret = System.getenv("CLIENTSECRET");

    public static void main(String[] args) {

        if(clientId == null || clientSecret == null)
        {
            System.out.println("NO ENV VARS CONFIGURED, SYSTEM ERRORS MIGHT OCCUR");
        }
        SpringApplication.run(DevDeskQueueApplication.class, args);
    }

}
