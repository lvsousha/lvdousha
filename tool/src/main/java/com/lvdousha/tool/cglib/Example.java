package com.lvdousha.tool.cglib;

import java.util.Date;

public class Example {

	private String global1 = "g_string";
	private int i = 0;
	private Date date=new Date();
	private final String global2 = "g_final";
	private static String global3 = "g_static";
	
	public void test(){
		String partial1 = "p_string";
		final String partial2 = "p_final";
	}
	
	public String test2(){
		return "test2";
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
