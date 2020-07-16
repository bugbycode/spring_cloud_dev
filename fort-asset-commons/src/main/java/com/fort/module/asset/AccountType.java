package com.fort.module.asset;

public enum AccountType {

	ROOT(0,"管理员"),GENERAL(1,"普通账号");
	
	private int type;
	
	private String label;

	private AccountType(int type, String label) {
		this.type = type;
		this.label = label;
	}

	public int getType() {
		return type;
	}

	public String getLabel() {
		return label;
	}
}
