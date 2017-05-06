package com.lvdousha.jdbc.mybatis;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.lvdousha.jdbc.mybatis.mapper.UserMapper;
import com.lvdousha.jdbc.mybatis.model.User;

/**
 * Hello world!
 *
 */
public class App{
    public static void main( String[] args ) throws Exception{
    	String resource = "mybatis-config.xml";
    	InputStream inputStream = Resources.getResourceAsStream(resource);
    	SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    	SqlSession session = sqlSessionFactory.openSession();
    	try {
    		UserMapper userMapper = session.getMapper(UserMapper.class);
    		User user = userMapper.selectUser(1);
    		System.out.println(user.getName());
    		user = userMapper.selectUserByName("lvdousha","123456");
    		System.out.println(user.getId());
    				
    	} finally {
    	  session.close();
    	}
    }
}
