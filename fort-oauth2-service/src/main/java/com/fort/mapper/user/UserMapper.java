package com.fort.mapper.user;

import java.util.Map;

import com.fort.module.user.User;

public interface UserMapper {

	public User queryByUserName(String username);
	
	public User login(Map<String,Object> params);
}
