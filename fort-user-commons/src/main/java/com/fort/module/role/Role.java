package com.fort.module.role;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

/**
 * 角色实体信息类
 * @author 张志功
 *
 */
public class Role implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8866325231000129850L;

	private int id;
	
	private String name;
	
	private String description;

	private String grantedAuthority;
	
	private int type;
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	
	private Date createTime;
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	
	private Date updateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGrantedAuthority() {
		return grantedAuthority;
	}

	public void setGrantedAuthority(String grantedAuthority) {
		this.grantedAuthority = grantedAuthority;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public Set<String> grantList(){
		Set<String> set = new HashSet<String>();
		if(!StringUtils.isEmpty(this.grantedAuthority)) {
			set = StringUtils.commaDelimitedListToSet(this.grantedAuthority);
		}
		return set;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
