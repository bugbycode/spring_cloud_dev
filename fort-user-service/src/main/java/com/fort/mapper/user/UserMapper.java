package com.fort.mapper.user;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.fort.module.user.User;

/**
 * 用户信息管理接口
 * @author zhigongzhang
 *
 */
public interface UserMapper {

	/**
	 * 自定义条件分页查询用户信息
	 * @param keyword 模糊查询条件
	 * @param offset 起始记录数
	 * @param limit 每页显示的条数
	 * @return
	 */
	public List<User> query(@Param("keyword") String keyword,@Param("offset") int offset,@Param("limit") int limit);
	
	/**
	 * 自定义条件统计用户总记录数
	 * @param keyword 模糊查询条件
	 * @return
	 */
	public int count(@Param("keyword") String keyword);
	
	/**
	 * 新建一条用户信息
	 * @param user 用户信息
	 * @return
	 */
	public int insert(User user);
	
	/**
	 * 根据用户ID修改用户信息
	 * @param user 用户信息
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
	 * 根据用户名查询用户信息
	 * @param username 用户名
	 * @return
	 */
	public User queryByUserName(String username);
	
}
