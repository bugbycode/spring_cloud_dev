package com.util.exception;

public enum FortError {

	SERVER_ERROR(1000,"自定义错误消息");
	
	private int code;
	
	private String message;

	private FortError(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public String toString() {
		return String.format("%d-%s", this.code,this.message);
	}
}
