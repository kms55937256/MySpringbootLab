package com.rookies3.MySpringbootLab.runner;
import com.rookies3.MySpringbootLab.property.MyPropProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class MyPropRunner implements ApplicationRunner {
    private final String username;
    private final int port;
    private final MyPropProperties properties;
    private final Logger logger = LoggerFactory.getLogger(MyPropRunner.class);

    public MyPropRunner(
            @Value("${myprop.username}") String username,
            @Value("${myprop.port}") int port,
            MyPropProperties properties
    ) {
        this.username = username;
        this.port = port;
        this.properties = properties;
    }

    @Override
    public void run(ApplicationArguments args) {
        logger.info("Properties myprop.username = {}", username);
        logger.info("Properties myprop.port = {}", port);
        logger.debug("MyPropProperties.getUsername() = {}", properties.getUsername());
        logger.debug("MyPropProperties.getPort() = {}", properties.getPort());
    }
}