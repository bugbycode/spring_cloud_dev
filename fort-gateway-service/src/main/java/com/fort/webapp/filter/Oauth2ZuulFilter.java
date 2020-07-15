package com.fort.webapp.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UrlPathHelper;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * oauth2 认证过滤器 负责校验验证码、判断是否自动附加客户端私钥以及客户端ID信息、自动识别token传递方式
 * 
 * @author zhigongzhang
 *
 */
public class Oauth2ZuulFilter extends ZuulFilter {
	
	private final UrlPathHelper urlPathHelper = new UrlPathHelper();

	private final String ACCESS_TOKEN_KEY = "access_token";
	
	@Value("${spring.oauth.resolePath}")
	private String oauthResolePath;
	
	@Value("${spring.oauth.clientId}")
	private String gatewayClientId;
	
	@Value("${spring.oauth.clientSecret}")
	private String gatewayClientSecret;
	
	@Value("${spring.oauth.scope}")
	private String gatewayScope;
	
	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		
		String requestURI = this.urlPathHelper
				.getPathWithinApplication(request);
		
		Enumeration<String> en = request.getHeaderNames();
		
		String access_token = "";
		
		while(en.hasMoreElements()) {
			String key = en.nextElement();
			if(key.toLowerCase().equals("authorization")) {
				access_token = request.getHeader(key);
			}
		}
		
		if(!StringUtils.isEmpty(access_token)) {
			return null;
		}
		
		access_token = request.getParameter("access_token");
		
		//如果是获取token的请求则需要验证码
		if(requestURI.endsWith(oauthResolePath)) {
			//客户端ID和私钥
			String clientId = request.getParameter("client_id");
			//String clientSecret = request.getParameter("client_secret");
			if(StringUtils.isEmpty(clientId)) {
				HttpSession session = request.getSession();
				Object codeObj = session.getAttribute("rand");
				String sessionImgCode = codeObj == null ? "" : codeObj.toString();
				//验证码
				String imgCode = request.getParameter("imgCode");
				if(StringUtils.isEmpty(imgCode) || !imgCode.toUpperCase().equals(sessionImgCode)) {
					HttpServletResponse response = ctx.getResponse();
					response.setCharacterEncoding("UTF-8");
					response.setContentType("application/json;charset=UTF-8");
					ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
					ctx.setSendZuulResponse(false);
					ctx.setResponse(response);
					
					//{error=invalid_token, error_description=Token has expired}
					
					JSONObject json = new JSONObject();
					json.put("error", "invalid_code");
					json.put("error_description", "验证码错误");
					ctx.setResponseBody(json.toString());
				} else {
					//验证码正确则自动附加client_id和client_secret
					List<String> paramClientList = new ArrayList<String>();
					paramClientList.add(gatewayClientId);
					
					List<String> paramSecretList = new ArrayList<String>();
					paramSecretList.add(gatewayClientId);
					
					List<String> paramScopeList = new ArrayList<String>();
					paramScopeList.add(gatewayScope);
					
					Map<String,List<String>> params = ctx.getRequestQueryParams();
					params.put("client_id", paramClientList);
					params.put("client_secret", paramSecretList);
					params.put("scope", paramScopeList);
				}
			} 
			return null;
		}
		
		
		if(StringUtils.isEmpty(access_token)){
			
			String contentType = request.getContentType();
			
			if(contentType != null && contentType.contains("application/json;")) {
				
				StringBuilder builder = new StringBuilder();
				BufferedReader br = null;
				try {
					
					br = request.getReader();
					String line = null;
					while((line = br.readLine()) != null) {
						builder.append(line);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if(br != null) {
							br.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				String body = builder.toString();
				if(!StringUtils.isEmpty(body)) {
					try {
						JSONObject json = new JSONObject(body);
						if(json.get(ACCESS_TOKEN_KEY) != null) {
							access_token = json.getString(ACCESS_TOKEN_KEY);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		if(!StringUtils.isEmpty(access_token)){
			ctx.getZuulRequestHeaders().put("authorization", String.format("Bearer %s", access_token));
		}
		
		return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return 1;
	}

}
