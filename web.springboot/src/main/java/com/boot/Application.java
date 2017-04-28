package com.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class Application {
      public static void main(String[] args) {
          SpringApplication.run(Application.class, args);
      }
  	
      protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
          // TODO Auto-generated method stub
          return builder.sources(Application.class);
      }
      
}
