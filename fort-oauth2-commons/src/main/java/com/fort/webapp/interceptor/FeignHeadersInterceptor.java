package com.fort.webapp.interceptor;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fort.feign.oauth.OauthFeign;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * 传递头信息设置 此功能需要配置Hystrix隔离策略配置为 hystrix.command.default.execution.isolation.strategy: SEMAPHORE
 * @author zhigongzhang
 *
 */
public class FeignHeadersInterceptor implements RequestInterceptor {

	private final Logger logger = LoggerFactory.getLogger(FeignHeadersInterceptor.class);
	
	@Value("${spring.oauth.clientId}")
	private String clientId;
	
	@Value("${spring.oauth.clientSecret}")
	private String clientSecret;
	
	@Value("${spring.oauth.scope}")
	private String scope;
	
	@Autowired
	private OauthFeign oauthFeign;
	
	@Override
	public void apply(RequestTemplate template) {
		
		HttpServletRequest request = getHttpServletRequest();

		if (Objects.isNull(request)) {
			Map<String,Object> map = oauthFeign.checkClientToken("client_credentials", clientId, clientSecret, scope);

			if(map.containsKey("error") || !map.containsKey("access_token")) {
				throw new RuntimeException(map.get("error_description").toString());
			}
			String token = map.get("access_token").toString();
			template.header("Authorization", "Bearer " + token);
			return;
		}
		
		Map<String,Object> map = oauthFeign.checkClientToken("client_credentials", clientId, clientSecret, scope);
		
		if(map.containsKey("error") || !map.containsKey("access_token")) {
			throw new RuntimeException(map.get("error_description").toString());
		}
		String token = map.get("access_token").toString();
		
		/*String token = "8b95df3d-957a-4e25-90df-de407b14b4ca";*/
		logger.debug("Get token success ......");
		
		Map<String, String> headers = getHeaders(request);
		if (headers.size() > 0) {
			Iterator<Entry<String, String>> iterator = headers.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				String key = entry.getKey();
				String value = entry.getValue();
				if("authorization".equals(key.toLowerCase())) {
					value = "Bearer " + token;
				}
				template.header(key, value);
			}
			if(!(headers.containsKey("Authorization") || headers.containsKey("authorization"))) {
				template.header("Authorization", "Bearer " + token);
			}
		}
	}

	private HttpServletRequest getHttpServletRequest() {
		try {
			return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
		} catch (Exception e) {
			return null;
		}
	}

	private Map<String, String> getHeaders(HttpServletRequest request) {
		Map<String, String> map = new LinkedHashMap<>();
		Enumeration<String> enums = request.getHeaderNames();
		while (enums.hasMoreElements()) {
			String key = enums.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		return map;
	}
}
