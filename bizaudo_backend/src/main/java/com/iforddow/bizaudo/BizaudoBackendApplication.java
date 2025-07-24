package com.iforddow.bizaudo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BizaudoBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BizaudoBackendApplication.class, args);
    }

}
