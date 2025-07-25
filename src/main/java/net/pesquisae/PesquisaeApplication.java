package net.pesquisae;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class PesquisaeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PesquisaeApplication.class, args);
    }

}
