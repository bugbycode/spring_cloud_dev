package com.fort.service.oauth;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fort.mapper.oauth.TokenMapper;
import com.fort.module.oauth.OauthAccessToken;
import com.fort.module.oauth.OauthRefreshToken;

@Service("tokenStoreService")
public class TokenStoreService implements TokenStore {

    private final Logger logger = LogManager.getLogger(TokenStoreService.class);

    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

    @Autowired
    private TokenMapper tokenMapper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return readAuthentication(token.getValue());
    }

//    @Override
//    public OAuth2Authentication readAuthentication(String token) {
//        OAuth2Authentication authentication = null;
//        try {
//            OauthAccessToken accessToken = tokenMapper.queryByTokenId(extractTokenKey(token));
//            if (accessToken != null) {
//                authentication = deserializeAuthentication(accessToken.getAuthentication());
//            }
//
//        } catch (IllegalArgumentException e) {
//            logger.warn("Failed to deserialize authentication for " + token, e);
//            removeAccessToken(token);
//        }
//        return authentication;
//    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        OAuth2Authentication authentication = null;
        try {
            OauthAccessToken accessToken = tokenMapper.queryByTokenId(extractTokenKey(token));
            if(accessToken != null) {
                authentication = deserializeAuthentication(accessToken.getAuthentication());
                String username = accessToken.getUserName();
                if(!StringUtils.isEmpty(username)) {
                    UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(username, "", getGrantedAuthority(username));
                    authentication = new OAuth2Authentication(authentication.getOAuth2Request(), userToken);
                }
            }
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to deserialize authentication for " + token, e);
            removeAccessToken(token);
        }
        return authentication;
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        String refreshToken = null;
        if (token.getRefreshToken() != null) {
            refreshToken = token.getRefreshToken().getValue();
        }

        OauthAccessToken accessToken = new OauthAccessToken();
        accessToken.setTokenId(extractTokenKey(token.getValue()));
        accessToken.setToken(serializeAccessToken(token));
        accessToken.setAuthentication(serializeAuthentication(authentication));
        accessToken.setAuthenticationId(authenticationKeyGenerator.extractKey(authentication));
        accessToken.setUserName(authentication.isClientOnly() ? null : authentication.getName());
        accessToken.setClientId(authentication.getOAuth2Request().getClientId());
        accessToken.setRefreshToken(extractTokenKey(refreshToken));

        if (readAccessToken(token.getValue()) != null) {
            //logger.error("=====================================================================================");
            //removeAccessToken(token.getValue());
            tokenMapper.updateAccessToken(accessToken);
        } else {
            tokenMapper.insertAccessToken(accessToken);
        }
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        OAuth2AccessToken oauthAccessToken = null;
        try {
            OauthAccessToken accessToken = tokenMapper.queryByTokenId(extractTokenKey(tokenValue));
            if (accessToken != null) {
                oauthAccessToken = deserializeAccessToken(accessToken.getToken());
            }
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to deserialize access token for " + tokenValue, e);
            removeAccessToken(tokenValue);
        }
        return oauthAccessToken;
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        removeAccessToken(token.getValue());
    }

    public void removeAccessToken(String tokenValue) {
        tokenMapper.deleteByTokenId(extractTokenKey(tokenValue));
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        OauthRefreshToken storeToken = new OauthRefreshToken();
        storeToken.setAuthentication(serializeAuthentication(authentication));
        storeToken.setToken(serializeRefreshToken(refreshToken));
        storeToken.setTokenId(extractTokenKey(refreshToken.getValue()));
        tokenMapper.insertRefreshToken(storeToken);
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        OAuth2RefreshToken refreshToken = null;
        try {
            OauthRefreshToken refresh = tokenMapper.queryRefreshTokenByTokenId(extractTokenKey(tokenValue));
            if (refresh != null) {
                refreshToken = deserializeRefreshToken(refresh.getToken());
            }

        } catch (IllegalArgumentException e) {
            logger.warn("Failed to deserialize access token for " + tokenValue, e);
            removeAccessToken(tokenValue);
        }
        return refreshToken;
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        return readAuthenticationForRefreshToken(token.getValue());
    }

    public OAuth2Authentication readAuthenticationForRefreshToken(String token) {
        OAuth2Authentication authentication = null;
        try {
            OauthRefreshToken refresh = tokenMapper.queryRefreshTokenByTokenId(extractTokenKey(token));
            if (refresh != null) {
                authentication = deserializeAuthentication(refresh.getAuthentication());
            }
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to deserialize access token for " + token, e);
            removeRefreshToken(token);
        }
        return authentication;
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        removeRefreshToken(token.getValue());
    }

    public void removeRefreshToken(String token) {
        tokenMapper.deleteRefreshByTokenId(extractTokenKey(token));
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        removeAccessTokenUsingRefreshToken(refreshToken.getValue());
    }

    public void removeAccessTokenUsingRefreshToken(String refreshToken) {
        tokenMapper.deleteByRefreshToken(extractTokenKey(refreshToken));
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        OAuth2AccessToken accessToken = null;
        String key = authenticationKeyGenerator.extractKey(authentication);
        try {
            OauthAccessToken oauthToken = tokenMapper.queryByAuthenticationId(key);
            if (oauthToken != null) {
                accessToken = deserializeAccessToken(oauthToken.getToken());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage());
        }
        if (accessToken != null) {
            OAuth2Authentication auth = readAuthentication(accessToken.getValue());
            if (auth == null || !key.equals(authenticationKeyGenerator.extractKey(auth))) {
                removeAccessToken(accessToken.getValue());
                // Keep the store consistent (maybe the same user is represented by this authentication but the details have
                // changed)
                storeAccessToken(accessToken, authentication);
            }
        }
        return accessToken;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("clientId", clientId);
        params.put("userName", userName);
        List<OAuth2AccessToken> accessTokens = new ArrayList<OAuth2AccessToken>();
        List<OauthAccessToken> list = tokenMapper.queryByClientIdAndUserName(params);
        if (!list.isEmpty()) {
            for (OauthAccessToken token : list) {
                try {
                    accessTokens.add(deserializeAccessToken(token.getToken()));
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e.getLocalizedMessage());
                    //tokenMapper.deleteByTokenId(token.getTokenId());
                }
            }
            accessTokens = removeNulls(accessTokens);
        }
        return accessTokens;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        List<OAuth2AccessToken> accessTokens = new ArrayList<OAuth2AccessToken>();
        List<OauthAccessToken> list = tokenMapper.queryByClientId(clientId);
        if (!list.isEmpty()) {
            for (OauthAccessToken token : list) {
                try {
                    accessTokens.add(deserializeAccessToken(token.getToken()));
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e.getLocalizedMessage());
                    //tokenMapper.deleteByTokenId(token.getTokenId());
                }
            }
            accessTokens = removeNulls(accessTokens);
        }
        return accessTokens;
    }

    private List<OAuth2AccessToken> removeNulls(List<OAuth2AccessToken> accessTokens) {
        List<OAuth2AccessToken> tokens = new ArrayList<OAuth2AccessToken>();
        for (OAuth2AccessToken token : accessTokens) {
            if (token != null) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    protected String extractTokenKey(String value) {
        if (value == null) {
            return null;
        }
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
        }

        try {
            byte[] bytes = digest.digest(value.getBytes("UTF-8"));
            return String.format("%032x", new BigInteger(1, bytes));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
        }
    }

    protected byte[] serializeAccessToken(OAuth2AccessToken token) {
        return SerializationUtils.serialize(token);
    }

    protected byte[] serializeRefreshToken(OAuth2RefreshToken token) {
        return SerializationUtils.serialize(token);
    }

    protected byte[] serializeAuthentication(OAuth2Authentication authentication) {
        return SerializationUtils.serialize(authentication);
    }

    protected OAuth2AccessToken deserializeAccessToken(byte[] token) {
        return SerializationUtils.deserialize(token);
    }

    protected OAuth2RefreshToken deserializeRefreshToken(byte[] token) {
        return SerializationUtils.deserialize(token);
    }

    protected OAuth2Authentication deserializeAuthentication(byte[] authentication) {
        return SerializationUtils.deserialize(authentication);
    }


    /**
     * 获取用户权限
     * @param username
     * @return
     */
    private Collection<? extends GrantedAuthority> getGrantedAuthority(String username){
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return userDetails == null ? new HashSet<GrantedAuthority>() : userDetails.getAuthorities();
    }

}
