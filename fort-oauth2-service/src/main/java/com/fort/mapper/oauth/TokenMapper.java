package com.fort.mapper.oauth;

import java.util.List;
import java.util.Map;

import com.fort.module.oauth.OauthAccessToken;
import com.fort.module.oauth.OauthRefreshToken;

public interface TokenMapper {
	
	public OauthAccessToken queryByTokenId(String tokenId);
	
	public OauthAccessToken queryByAuthenticationId(String authenticationId);
	
	public List<OauthAccessToken> queryByClientIdAndUserName(Map<String,Object> params);
	
	public List<OauthAccessToken> queryByClientId(String clientId);
	
	public void deleteByTokenId(String tokenId);
	
	public void deleteByRefreshToken(String refreshToken);
	
	public void insertAccessToken(OauthAccessToken accessToken);
	
	public void updateAccessToken(OauthAccessToken accessToken);
	
	public void insertRefreshToken(OauthRefreshToken refreshToken);
	
	public OauthRefreshToken queryRefreshTokenByTokenId(String tokenId);
	
	public void deleteRefreshByTokenId(String tokenId);
}
