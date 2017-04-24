package com.lvdousha.jdbc.basic;

import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Properties;


/**
 * Hello world!
 *
 */
public class Main 
{
    public static void main( String[] args ) throws Exception{
    	String driverClassName = "com.mysql.jdbc.Driver";
    	String url = "jdbc:mysql://localhost:3306/lvdousha";
    	String userName = "root";
    	String password = "root";
    	Class.forName(driverClassName);
    	java.sql.Connection connection = DriverManager.getConnection(url,userName,password);
    	boolean autoCommit = connection.getAutoCommit();
    	if(autoCommit == true){
    		connection.setAutoCommit(false);
    	}
    	System.out.println(connection.getAutoCommit());
    	System.out.println(Calendar.getInstance());
    	
    	PreparedStatement prepareStatement = connection.prepareStatement("insert into user(name,password) value(?,?)");
    	for(int i=0;i<10000;i++){
    		prepareStatement.setString(1, "test"+i);
    		prepareStatement.setString(2, "test"+i);
    		prepareStatement.addBatch();
    	}
    	prepareStatement.executeBatch();
    	connection.commit();
    	prepareStatement.clearBatch();
    	System.out.println(Calendar.getInstance());
    	
    	prepareStatement = connection.prepareStatement("update user set name=? where name=?");
    	for(int i=0;i<10000;i++){
    		System.out.println(i);
    		prepareStatement.setString(1, "test"+i);
    		prepareStatement.setString(2, "test"+i);
    		prepareStatement.addBatch();
    	}
    	prepareStatement.executeBatch();
    	connection.commit();
    	System.out.println(Calendar.getInstance());
    	prepareStatement.close();
    	connection.close();
    	System.out.println(Calendar.getInstance());
    }
}
