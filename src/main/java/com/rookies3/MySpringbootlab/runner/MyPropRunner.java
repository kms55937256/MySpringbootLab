package com.rookies3.MySpringbootlab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyPropRunner implements CommandLineRunner {

    @Value("${myprop.username}")
    private String username;

    @Value("${myprop.port}")
    private int port;

    @Override
    public void run(String... args) {
        System.out.println("Username: " + username);
        System.out.println("Port: " + port);
    }
}