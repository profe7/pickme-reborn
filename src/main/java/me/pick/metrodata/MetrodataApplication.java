package me.pick.metrodata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@SpringBootApplication
public class MetrodataApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetrodataApplication.class, args);
        System.out.println("\n <================ Application started ================>\n");
    }
}
