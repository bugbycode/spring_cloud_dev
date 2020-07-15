package com.fort.service.user;

import com.fort.module.user.User;
import com.util.page.SearchResult;

public interface UserService {

	/**
	 * 自定义条件分页查询用户信息
	 * @param keyword 模糊查询条件
	 * @param offset 起始记录数
	 * @param limit 每页显示的条数
	 * @return
	 */
	public SearchResult<User> query(String keyword,int offset,int limit);
	
	/**
	 * 新建一条用户信息 
	 * @param user
	 * @return
	 */
	public int insert(User user);
	
	/**
	 * 根据用户ID修改用户信息
	 * @param user
	 * @return
	 */
	public int updateById(User user);
	
	/**
	 * 根据用户ID删除用户信息
	 * @param id 用户ID
	 * @return
	 */
	public int deleteById(long id);
	
	/**
	 * 根据用户ID查询单一条用户信息
	 * @param id 用户ID
	 * @return
	 */
	public User queryById(long id);
	
	/**
	 * 根据用户名查询单一条用户信息
	 * @param username 用户名
	 * @return
	 */
	public User findByUsername(String username);
}
