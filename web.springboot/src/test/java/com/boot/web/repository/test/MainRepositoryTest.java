package com.boot.web.repository.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.boot.Application;
import com.boot.web.model.User;
import com.boot.web.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class MainRepositoryTest {
	
	@Autowired
	UserRepository userRepository;
	
	@Test
	public void mainTest(){
		List<User> users = userRepository.findByName("test");
		System.out.println(users.size());
//		userRepository.
	}

}
