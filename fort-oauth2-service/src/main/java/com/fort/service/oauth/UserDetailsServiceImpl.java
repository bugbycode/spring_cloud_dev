package com.fort.service.oauth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fort.mapper.role.RoleMapper;
import com.fort.mapper.user.UserMapper;
import com.fort.module.role.Role;
import com.fort.module.user.User;
import com.fort.service.user.UserService;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserService,UserDetailsService {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RoleMapper roleMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			User user = userMapper.queryByUserName(username);
			if(user == null) {
				throw new UsernameNotFoundException("用户名或密码错误");
			}
			long roleId = user.getRoleId();
			if(roleId == 0) {
				throw new RuntimeException("用户未授权");
			}
			Role role = roleMapper.queryById(roleId);
			if(role == null) {
				throw new RuntimeException("用户未授权");
			}
			user.setRole(role);
			return user;
		} catch (Exception e) {
			throw new UsernameNotFoundException("用户名密码错误");
		}
	}

	@Override
	public User login(String username, String password) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("username", username);
		params.put("password", password);
		return userMapper.login(params);
	}

}
