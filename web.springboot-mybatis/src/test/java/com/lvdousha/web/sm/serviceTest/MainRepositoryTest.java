package com.lvdousha.web.sm.serviceTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lvdousha.web.sm.App;
import com.lvdousha.web.sm.mapper.UserMapper;
import com.lvdousha.web.sm.model.User;

@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
public class MainRepositoryTest {
	
	@Autowired
	UserMapper userMapper;
	
	@Test
	public void mainTest(){
		User user = new User();
		user.setId(10001l);
		System.out.println(userMapper.selectOne(user).getName());
	}

}
