package com.lvdousha.jdbc.mybatis.mapper;

import com.lvdousha.jdbc.mybatis.model.User;

public interface UserMapper {
	 public User selectUser(int id);
	 public User selectUserByName(String name, String password);
}
