package com.fort.service.oauth;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import com.fort.mapper.oauth.ClientDetailsMapper;
import com.fort.module.oauth.OauthClientDetails;

@Service("clientService")
public class ClientService implements ClientDetailsService {

	private final Logger logger = LogManager.getLogger(ClientService.class);
	
	private JsonMapper mapper = createJsonMapper();
	
	@Autowired
	private ClientDetailsMapper clientDetailsMapper;
	
	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		try {
			OauthClientDetails jdbcClient = clientDetailsMapper.queryByClientId(clientId);
			if(jdbcClient == null) {
				throw new NoSuchClientException("客户端ID不存在");
			}
			BaseClientDetails client = new BaseClientDetails();
			client.setClientId(jdbcClient.getClientId());
			client.setClientSecret(jdbcClient.getClientSecret());
			String scope = jdbcClient.getScope();
			if(!StringUtils.isEmpty(scope) && StringUtils.hasText(scope)) {
				client.setScope(StringUtils.commaDelimitedListToSet(scope));
			}
			String resourceIds = jdbcClient.getResourceId();
			if(!StringUtils.isEmpty(resourceIds) && StringUtils.hasText(resourceIds)) {
				client.setResourceIds(StringUtils.commaDelimitedListToSet(resourceIds));
			}
			String authorizedGrantTypes = jdbcClient.getAuthorizedGrantTypes();
			if(!StringUtils.isEmpty(authorizedGrantTypes) && StringUtils.hasText(authorizedGrantTypes)) {
				client.setAuthorizedGrantTypes(StringUtils.commaDelimitedListToSet(authorizedGrantTypes));
			}
			String webServerRedirectUri = jdbcClient.getWebServerRedirectUri();
			if(!StringUtils.isEmpty(webServerRedirectUri) && StringUtils.hasText(webServerRedirectUri)) {
				client.setRegisteredRedirectUri(StringUtils.commaDelimitedListToSet(webServerRedirectUri));
			}
			String authorities = jdbcClient.getAuthorities();
			if(!StringUtils.isEmpty(authorities) && StringUtils.hasText(authorities)) {
				Set<String> grant = StringUtils.commaDelimitedListToSet(authorities);
				Set<GrantedAuthority> grantSet = new HashSet<GrantedAuthority>();
				if(!grant.isEmpty()) {
					for(String role : grant) {
						grantSet.add(new SimpleGrantedAuthority("ROLE_" + role));
					}
				}
				client.setAuthorities(grantSet);
			}
			client.setAccessTokenValiditySeconds(jdbcClient.getAccessTokenValidity());
			client.setRefreshTokenValiditySeconds(jdbcClient.getRefreshTokenValidity());
			String additionalInformation = jdbcClient.getAdditionalInformation();
			if(!StringUtils.isEmpty(additionalInformation) && StringUtils.hasText(additionalInformation)) {
				@SuppressWarnings("unchecked")
				Map<String,Object> additionalInformationMap = mapper.read(additionalInformation, Map.class);
				client.setAdditionalInformation(additionalInformationMap);
			}
			String autoApprove = jdbcClient.getAutoapprove();
			if(!StringUtils.isEmpty(autoApprove) && StringUtils.hasText(autoApprove)) {
				client.setAutoApproveScopes(StringUtils.commaDelimitedListToSet(autoApprove));
			}
			return client;
		}catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new NoSuchClientException("No client with requested id: " + clientId);
		}
	}

	interface JsonMapper {
		String write(Object input) throws Exception;

		<T> T read(String input, Class<T> type) throws Exception;
	}
	
	private static JsonMapper createJsonMapper() {
		if (ClassUtils.isPresent("org.codehaus.jackson.map.ObjectMapper", null)) {
			return new JacksonMapper();
		}
		else if (ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", null)) {
			return new Jackson2Mapper();
		}
		return new NotSupportedJsonMapper();
	}

	private static class JacksonMapper implements JsonMapper {
		private org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();

		@Override
		public String write(Object input) throws Exception {
			return mapper.writeValueAsString(input);
		}

		@Override
		public <T> T read(String input, Class<T> type) throws Exception {
			return mapper.readValue(input, type);
		}
	}

	private static class Jackson2Mapper implements JsonMapper {
		private com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();

		@Override
		public String write(Object input) throws Exception {
			return mapper.writeValueAsString(input);
		}

		@Override
		public <T> T read(String input, Class<T> type) throws Exception {
			return mapper.readValue(input, type);
		}
	}

	private static class NotSupportedJsonMapper implements JsonMapper {
		@Override
		public String write(Object input) throws Exception {
			throw new UnsupportedOperationException(
					"Neither Jackson 1 nor 2 is available so JSON conversion cannot be done");
		}

		@Override
		public <T> T read(String input, Class<T> type) throws Exception {
			throw new UnsupportedOperationException(
					"Neither Jackson 1 nor 2 is available so JSON conversion cannot be done");
		}
	}
}
