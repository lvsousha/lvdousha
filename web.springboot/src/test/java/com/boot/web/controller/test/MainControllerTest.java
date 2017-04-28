package com.boot.web.controller.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.boot.Application;
import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class MainControllerTest {
	
	MockMvc mvc;  
	  
    @Autowired  
    WebApplicationContext webApplicationConnect;  
  
  
    String expectedJson;  
  
    @Before
    public void setUp() throws JsonProcessingException {  
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationConnect).build();  
  
    } 
    
    @Test  
    public void testShow() throws Exception {  
    	JSONObject object = new JSONObject();
    	object.put("depositReserve", "100000");
    	object.put("deposit", "100000");
    	object.put("rate", "0.08");
    	object.put("maxApplyAmount", "10");
    	object.put("applyAmount", "100");
        String uri = "/";  
        MvcResult mvcResult = mvc.perform(
	        							MockMvcRequestBuilders
	        								.get(uri)
											.contentType(MediaType.APPLICATION_JSON)
											.accept(MediaType.APPLICATION_JSON)
//											.content(object.toString())
        							)  
                .andReturn();  
        String content = mvcResult.getResponse().getContentAsString();  
        System.out.println(content);
  
    } 

}
