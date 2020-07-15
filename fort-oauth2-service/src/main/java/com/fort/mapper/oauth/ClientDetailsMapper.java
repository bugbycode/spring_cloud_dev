package com.fort.mapper.oauth;

import com.fort.module.oauth.OauthClientDetails;

public interface ClientDetailsMapper {

	public OauthClientDetails queryByClientId(String clientId);
}
