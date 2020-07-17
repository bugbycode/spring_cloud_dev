package com.fort.module.asset;

import java.io.Serializable;

/**
 * 协议实体信息类
 * @author zhigongzhang
 *
 */
public class Protocol implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2789820282047439825L;

	private long id;//主键
	
	private String name;//协议名称
	
	private int type;//协议类型 类型定义参考枚举类 com.fort.module.asset.ProtocolType
	
	private String typeLabel;//协议类型名称 用于界面人性化展示 类型定义参考枚举类 com.fort.module.asset.ProtocolType
	
	private Integer port;//协议端口号
	
	private int status;//协议是否被启用 类型定义参考枚举类 com.fort.module.asset.ProtocolStatus
	
	private String statusLabel;//协议启用状态描述 用于界面人性化展示 类型定义参考枚举类 com.fort.module.asset.ProtocolStatus

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTypeLabel() {
		return typeLabel;
	}

	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}

	public String getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

	public long getAssetId() {
		return assetId;
	}

	public void setAssetId(long assetId) {
		this.assetId = assetId;
	}
}
