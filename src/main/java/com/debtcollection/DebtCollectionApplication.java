package com.debtcollection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DebtCollectionApplication {

    public static void main(String[] args) {

        System.out.println("Java Home: " + System.getProperty("java.home"));
        System.out.println("Java Version: " + System.getProperty("java.version"));
        SpringApplication.run(DebtCollectionApplication.class, args);
    }

}
