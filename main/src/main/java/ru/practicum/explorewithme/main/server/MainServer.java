package ru.practicum.explorewithme.main.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ru.practicum")
public class MainServer {

    public static void main(String[] args) {
        SpringApplication.run(MainServer.class, args);
    }
}
