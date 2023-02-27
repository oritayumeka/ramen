package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class Ec202110bApplication extends SpringBootServletInitializer{

        public static void main(String[] args) {
                SpringApplication.run(Ec202110bApplication.class, args);
        }
@Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
      return builder.sources(Ec202110bApplication.class);
    }


}

