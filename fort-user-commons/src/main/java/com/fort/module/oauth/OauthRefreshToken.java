package com.fort.module.oauth;

import java.io.Serializable;

public class OauthRefreshToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2974449585826977195L;
	
	private String tokenId;
	
	private byte[] token;
	
	private byte[] authentication;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public byte[] getToken() {
		return token;
	}

	public void setToken(byte[] token) {
		this.token = token;
	}

	public byte[] getAuthentication() {
		return authentication;
	}

	public void setAuthentication(byte[] authentication) {
		this.authentication = authentication;
	}

}