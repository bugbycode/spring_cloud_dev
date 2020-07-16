package com.fort.module.asset;

import java.io.Serializable;

/**
 * 设备账号实体信息类
 * @author zhigongzhang
 *
 */
public class Account implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7055861260737245137L;

	private long id;//主键
	
	private String name;//账号
	
	private String password;//密码
	
	private int type;// 账号类型 类型定义参考枚举类 com.fort.module.asset.AccountType
	
	private String typeLabel; //账号类型名称 用于界面人性化展示 类型定义参考枚举类 com.fort.module.asset.AccountType
	
	private long assetId;//设备ID

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTypeLabel() {
		return typeLabel;
	}

	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}

	public long getAssetId() {
		return assetId;
	}

	public void setAssetId(long assetId) {
		this.assetId = assetId;
	}
}
