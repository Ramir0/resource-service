package dev.amir.resourceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({
        "dev.amir.resourceservice.framework",
        "dev.amir.resourceservice.application",
        "dev.amir.resourceservice.domain"
})
@EntityScan("dev.amir.resourceservice.framework")
@EnableJpaRepositories("dev.amir.resourceservice.framework")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
