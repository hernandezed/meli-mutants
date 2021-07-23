package com.meli.mutants;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MeliMutantsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeliMutantsApplication.class, args);
    }

}
