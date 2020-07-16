package com.fort.module.asset;

public enum ProtocolType {
	
	SSH(0,"SSH"),SFTP(1,"SFTP"),RDP(2,"RDP");

	private int type;
	
	private String label;//用于界面人性化展示
	
	ProtocolType(int type,String label){
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
