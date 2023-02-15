package dev.amir.resourceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({
        "dev.amir.resourceservice.framework",
        "dev.amir.resourceservice.application",
        "dev.amir.resourceservice.domain"
})
@EnableJpaRepositories("dev.amir.resourceservice.framework.output.sql")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
