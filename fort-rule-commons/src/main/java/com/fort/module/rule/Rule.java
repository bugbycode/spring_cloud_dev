package com.fort.module.rule;

import java.io.Serializable;

/**
 * 授权规则
 * @author zhigongzhang
 *
 */
public class Rule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3468983633919983843L;
	
	private long id; //主键
	
	private long assetId;//设备ID
	
	private String assetName;//设备名称
	
	private String assetIp;//设备IP
	
	private String osName;//操作系统
	
	private String osVersion;//系统版本
	
	private String account;//从账号
	
	private int protocol;//协议
	
	private String username;//用户账号
	
	private String fullName;//用户姓名

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAssetId() {
		return assetId;
	}

	public void setAssetId(long assetId) {
		this.assetId = assetId;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getAssetIp() {
		return assetIp;
	}

	public void setAssetIp(String assetIp) {
		this.assetIp = assetIp;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getProtocol() {
		return protocol;
	}

	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}
