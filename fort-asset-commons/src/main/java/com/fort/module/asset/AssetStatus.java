package com.fort.module.asset;

public enum AssetStatus {

	ENABLED(1,"激活"),DISABLED(0,"锁定");

	private int status;
	
	private String label;//用于界面人性化展示

	private AssetStatus(int status, String label) {
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
