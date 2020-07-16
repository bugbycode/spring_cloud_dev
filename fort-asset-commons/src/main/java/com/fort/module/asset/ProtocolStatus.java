package com.fort.module.asset;

/**
 * 协议状态枚举信息
 * 
 * @author zhigongzhang
 *
 */
public enum ProtocolStatus {
	
	ENABLED(1,"启用"),DISABLED(0,"禁用");

	private int status;
	
	private String label;//用于界面人性化展示

	private ProtocolStatus(int status, String label) {
		this.status = status;
		this.label = label;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
