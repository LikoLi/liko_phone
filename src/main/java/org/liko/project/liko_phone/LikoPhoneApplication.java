package org.liko.project.liko_phone;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;

@SpringBootApplication
public class LikoPhoneApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(LikoPhoneApplication.class, args);
    }
}
