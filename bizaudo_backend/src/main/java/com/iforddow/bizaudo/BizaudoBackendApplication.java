package com.iforddow.bizaudo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication
@EnableCaching
public class BizaudoBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BizaudoBackendApplication.class, args);
    }

}
