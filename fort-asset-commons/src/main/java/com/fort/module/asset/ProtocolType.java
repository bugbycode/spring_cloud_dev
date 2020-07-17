package com.fort.module.asset;

public enum ProtocolType {
	
	SSH(0,"SSH",22),SFTP(1,"SFTP",22),RDP(2,"RDP",3389);

	private int type;
	
	private String label;//用于界面人性化展示
	
	private int port;//默认端口
	
	ProtocolType(int type,String label,int port){
		this.type = type;
		this.label = label;
		this.port = port;
	}

	public int getType() {
		return type;
	}

	public String getLabel() {
		return label;
	}

	public int getPort() {
		return port;
	}
	
}
