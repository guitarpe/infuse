package br.infuse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"br.infuse.application.*"})
public class InfuseApplication{
    public static void main(String[] args) {
        SpringApplication.run(InfuseApplication.class, args);
    }
}
