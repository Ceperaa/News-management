package ru.clevertec.newsManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NewsManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsManagementApplication.class, args);
    }

}
