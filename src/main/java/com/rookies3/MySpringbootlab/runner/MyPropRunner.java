package com.rookies3.MySpringbootlab.runner;
import com.rookies3.MySpringbootlab.property.MyPropProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class MyPropRunner implements ApplicationRunner{
    @Value("${myprop.userName}")
    private String userName;


    @Value("${myprop.port}")
    private int port;

    @Autowired
    private MyPropProperties properties;

    //Logger 객체 생성
    private Logger logger = LoggerFactory.getLogger(MyPropRunner.class);


    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Properties myprop.userName = " + userName);
        logger.info("Properties myprop.port = " + port);

        logger.debug("MyPropProperties.getUserName() = " + properties.getUserName());
        logger.debug("MyPropProperties.getPort() = " + properties.getPort());
    }

}