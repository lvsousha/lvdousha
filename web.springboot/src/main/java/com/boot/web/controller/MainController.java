package com.boot.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class MainController {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
      @RequestMapping("/")
      @ResponseBody
      String home() {
    	  log.info("ddddd");
        return "Hello World的地方地方!";
      }
}
