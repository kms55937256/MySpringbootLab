package com.rookies3.MySpringbootlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MySpringbootLabApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MySpringbootLabApplication.class);

        application.setWebApplicationType(WebApplicationType.SERVLET);
        application.run(args);

    }

}
