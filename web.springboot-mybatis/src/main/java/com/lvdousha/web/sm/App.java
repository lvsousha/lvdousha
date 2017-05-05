package com.lvdousha.web.sm;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import com.alibaba.druid.pool.DruidDataSource;  
  

@SpringBootApplication
@ServletComponentScan
public class App {
      public static void main(String[] args) {
          SpringApplication.run(App.class, args);
      }
  	
      protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
          // TODO Auto-generated method stub
          return builder.sources(App.class);
      }
      
      @Autowired
      private Environment env;
      
      @Bean(destroyMethod = "close")
      public DataSource dataSource() {
    	  DruidDataSource datasource = new DruidDataSource();
    	  datasource.setUrl(env.getProperty("spring.datasource.url"));
    	  datasource.setUsername(env.getProperty("spring.datasource.username"));
    	  datasource.setPassword(env.getProperty("spring.datasource.password"));
    	  datasource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
    	  return datasource;
      }
      
      
      
      
      
      
      
      
      
      
} 