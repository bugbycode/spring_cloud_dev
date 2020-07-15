package com.fort.webapp.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fort.module.user.User;
import com.fort.service.user.UserService;
import com.util.page.SearchResult;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/query")
	public SearchResult<User> query(
			@RequestParam(name = "queryParam", defaultValue = "")
			String keyword,
			@RequestParam(name = "offset", defaultValue = "0")
			int offset,
			@RequestParam(name = "limit", defaultValue = "20")
			int limit){
		return userService.query(keyword, offset, limit);
	}
	
	/**
	 * 新建一条用户信息 
	 * @param user
	 * @return
	 */
	@PostMapping("/insert")
	public int insert(@RequestBody User user) {
		return userService.insert(user);
	}
	
	/**
	 * 根据用户ID查询单一条用户信息
	 * @param id 用户ID
	 * @return
	 */
	@GetMapping("/queryById")
	public User queryById(long id) {
		return userService.queryById(id);
	}
	
	/**
	 * 根据用户ID删除用户信息
	 * @param id 用户ID
	 * @return
	 */
	@PostMapping("/deleteById")
	public int deleteById(long id) {
		return userService.deleteById(id);
	}
}
