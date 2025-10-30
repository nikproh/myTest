package ru.petshop;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Bootstrap extends SpringBootServletInitializer {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(Bootstrap.class)
                .run(args);
    }
}
