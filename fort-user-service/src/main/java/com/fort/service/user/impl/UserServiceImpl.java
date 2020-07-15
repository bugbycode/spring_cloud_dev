package com.fort.service.user.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fort.mapper.role.RoleMapper;
import com.fort.mapper.user.UserMapper;
import com.fort.module.role.Role;
import com.fort.module.user.User;
import com.fort.service.user.UserService;
import com.util.page.Page;
import com.util.page.SearchResult;

@Service("userService")
public class UserServiceImpl implements UserService {

	private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RoleMapper roleMapper;
	
	@Override
	public SearchResult<User> query(String keyword, int offset, int limit) {
		SearchResult<User> sr = new SearchResult<User>();
		try {
			int totalCount = userMapper.count(keyword);
			Page page = new Page(limit,offset);
			page.setTotalCount(totalCount);
			
			List<User> userList = userMapper.query(keyword, offset, limit);
			if(!CollectionUtils.isEmpty(userList)) {
				for(User user : userList) {
					Long roleId = user.getRoleId();
					if(roleId == null || roleId == 0) {
						continue;
					}
					user.setRole(roleMapper.queryById(roleId));
				}
			}
			
			sr.setList(userList);
			
			sr.setPage(page);
			return sr;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new RuntimeException("自定义条件分页查询用户信息时发生异常，" + e.getMessage());
		}
	}

	@Override
	public int insert(User user) {
		try {
			Long roleId = user.getRoleId();
			if(roleId == null || roleId == 0) {
				throw new RuntimeException("请选择角色信息");
			}

			user.setType((byte)1);
			user.setCreateTime(new Date());
			int rows = userMapper.insert(user);
			//校验角色信息是否正确 先创建用户后再验证 测试事务是否生效
			Role role = roleMapper.queryById(roleId);
			if(role == null) {
				throw new RuntimeException("角色信息错误");
			}
			return rows;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new RuntimeException("新建一条用户信息时发生异常，" + e.getMessage());
		}
	}

	@Override
	public int updateById(User user) {
		try {
			return userMapper.updateById(user);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new RuntimeException("根据用户ID修改用户信息时发生异常，" + e.getMessage());
		}
	}

	@Override
	public int deleteById(long id) {
		
		User user = queryById(id);
		
		if(user == null || user.getType() == 0) {
			throw new AccessDeniedException("禁止删除内置用户");
		}
		
		try {
			return userMapper.deleteById(id);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new RuntimeException("根据用户ID删除用户信息时发生异常，" + e.getMessage());
		}
	}

	@Override
	public User queryById(long id) {
		try {
			return userMapper.queryById(id);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new RuntimeException("根据用户ID查询单一条用户信息时发生异常，" + e.getMessage());
		}
	}

	@Override
	public User findByUsername(String username) {
		try {
			return userMapper.queryByUserName(username);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new RuntimeException("根据用户名查询单一条用户信息时发生异常，" + e.getMessage());
		}
	}

}
