package com.fort.webapp.controller.global;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常捕获
 * @author zhigongzhang
 *
 */
@RestControllerAdvice
public class GloblaController {

	/**
	 * 服务器内部错误 响应 500
	 * @param e
	 * @return
	 */
	@ExceptionHandler({Exception.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String,Object> handlerException(Exception e){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("error", "服务器内部错误");
		result.put("error_description", e.getLocalizedMessage());
		return result;
	}
	
	/**
	 * 服务器内部错误 响应 500
	 * @param e
	 * @return
	 */
	@ExceptionHandler({RuntimeException.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String,Object> handlerRuntimeException(RuntimeException e){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("error", "服务器内部错误");
		result.put("error_description", e.getLocalizedMessage());
		return result;
	}
	
	/**
	 * 无权访问 响应 403
	 * @param e
	 * @return
	 */
	@ExceptionHandler({AccessDeniedException.class})
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public Map<String,Object> handlerAccessDeniedException(AccessDeniedException e){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("error", "无权访问");
		result.put("error_description", e.getLocalizedMessage());
		return result;
	}
}
