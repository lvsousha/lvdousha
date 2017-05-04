package com.lvdousha.jdbc.mybatis;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.lvdousha.jdbc.mybatis.mapper.BlogMapper;
import com.lvdousha.jdbc.mybatis.model.Blog;

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
//    	try {
//    		Blog blog = (Blog) session.selectOne("com.lvdousha.jdbc.mybatis.mapper.BlogMapper.selectBlog", 101);
//    		BlogMapper mapper = session.getMapper(BlogMapper.class);
//    		Blog blog = mapper.selectBlog(101);
//    	} finally {
//    	  session.close();
//    	}
    }
}
